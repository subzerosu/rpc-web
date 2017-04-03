package cane.brothers.rpc.service.quartz;

import cane.brothers.rpc.RpcUtils;
import cane.brothers.rpc.data.quartz.RpcTaskOperation;
import cane.brothers.rpc.quartz.RpcTicketJob;
import org.quartz.*;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

@Service
public class RpcQuartzJobService implements JobService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${cane.brothers.quartz.job.name}")
    private String defaultJobName;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    private static final long IN_HOURS = 60 * 60 * 1000l;

    /**
     * configure job bean
     *
     * @throws SchedulerException
     */
    private JobDetail getOrCreateJobDetail() {
        JobDetail job = null;
        JobKey jobKey = new JobKey(defaultJobName, RpcUtils.getGroupName());
        try {
            job = schedulerFactory.getScheduler().getJobDetail(jobKey);

            if (job == null) {
                JobDetailFactoryBean jobDetail = createJobDetailFactory(jobKey);
                job = jobDetail.getObject();
            }

            if (!schedulerFactory.getScheduler().checkExists(jobKey)) {
                schedulerFactory.setJobDetails(job);
            }
        }
        catch (SchedulerException ex) {
            logger.error("Unable create and set job details", ex);
        }

        return job;
    }

    private JobDetailFactoryBean createJobDetailFactory(JobKey jobKey) {
        JobDetailFactoryBean jobDetail = null;
        if (jobKey != null) {
            jobDetail = new JobDetailFactoryBean();
            jobDetail.setName(jobKey.getName());
            jobDetail.setGroup(jobKey.getGroup());
            jobDetail.setJobClass(RpcTicketJob.class);
            // job has to be durable to be stored in DB
            jobDetail.setDurability(true);
            jobDetail.afterPropertiesSet();
        }
        return jobDetail;
    }

    private Trigger getOrCreateTrigger(TriggerKey triggerKey, long repeatInterval, JobDetail jobDetail) {
        Trigger trigger = null;

        try {
            trigger = schedulerFactory.getScheduler().getTrigger(triggerKey);

            if (trigger == null) {
                SimpleTriggerFactoryBean triggerFactory = createTriggerFactory(triggerKey, repeatInterval, jobDetail);
                trigger = triggerFactory.getObject();
            }

            if (!schedulerFactory.getScheduler().checkExists(triggerKey)) {
                schedulerFactory.setTriggers(trigger);
            }
        }
        catch (SchedulerException ex) {
            logger.error("Unable create and set trigger", ex);
        }

        return trigger;
    }

    private SimpleTriggerFactoryBean createTriggerFactory(TriggerKey triggerKey, long repeatInterval,
            JobDetail jobDetail)
            throws SchedulerException {
        // TriggerBuilder<Trigger>.
        SimpleTriggerFactoryBean trigger = null;
        if (triggerKey != null) {
            trigger = new SimpleTriggerFactoryBean();

            trigger.setJobDetail(jobDetail);
            trigger.setStartDelay(3000);
            trigger.setRepeatInterval(repeatInterval * IN_HOURS);
            trigger.setName(triggerKey.getName());
            trigger.setGroup(triggerKey.getGroup());
            trigger.afterPropertiesSet();
        }
        return trigger;
    }

    @Override
    public boolean createJob(TriggerKey triggerKey, long repeatInterval) {
        JobDetail jobDetail = getOrCreateJobDetail();
        Trigger trigger = getOrCreateTrigger(triggerKey, repeatInterval,
                jobDetail);
        return (trigger == null ? false : true);
    }

    @Async
    @Override
    public Future<Boolean> startJob(TriggerKey triggerKey, long newInterval) {
        JobDetail jobDetail = getOrCreateJobDetail();
        Trigger trigger = getOrCreateTrigger(triggerKey, newInterval, jobDetail);

        // TODO check if Job already runnig
        try {
            List<? extends Trigger> assTriggers = schedulerFactory.getScheduler().getTriggersOfJob(jobDetail.getKey());
            for (Trigger t : assTriggers) {
                logger.debug(t.toString());
            }

            if (checkJobExistence(jobDetail)) {
                // сохранить Job тригер в БД, запустить тригер для Job
                Date d = schedulerFactory.getScheduler().scheduleJob(jobDetail, trigger);
                if (d != null) {
                    logger.info("Задание " + trigger + " запущено. " + d.toString());
                }
            }
            // для существующей Job добавить новый тригер, сохранив его в БД
            else {
                Date d = schedulerFactory.getScheduler().scheduleJob(trigger);
                if (d != null) {
                    logger.info("Задание " + trigger + " перезапущено. " +
                            d.toString());
                }
            }
        }
        catch (SchedulerException ex) {
            logger.error("Cannot schedule job", ex);
        }

        try {
            // стартуем шедулер если не запущен
            if (!schedulerFactory.getScheduler().isStarted()) {
                schedulerFactory.getScheduler().start();
            }
            return new AsyncResult<>(new Boolean(true));
        }
        catch (SchedulerException ex) {
            logger.error("Cannot start sheduler", ex);
        }
        return new AsyncResult<>(new Boolean(false));
    }


    @Async
    @Override
    public Future<Boolean> stopJob(TriggerKey triggerKey) {
        try {
            // удаляем у Job триггер, чистим за ним в БД
            if (schedulerFactory.getScheduler().unscheduleJob(triggerKey)) {
                logger.error("Job {} was stoped", triggerKey);
                return new AsyncResult<>(new Boolean(true));
            }
            else {
                logger.error("Cannot stop {}", triggerKey);
                return new AsyncResult<>(new Boolean(false));
            }
        }
        catch (SchedulerException ex) {
            logger.error("Cannot start sheduler", ex);
        }
        return new AsyncResult<>(new Boolean(false));
    }

    @Override
    public boolean resetJob(TriggerKey triggerKey, Long newInterval) {
        JobDetail jobDetail = getOrCreateJobDetail();
        Trigger newTrigger = getOrCreateTrigger(triggerKey, newInterval, jobDetail);

        try {
            if (checkJobExistence(jobDetail)) {
                // нельзя назначить работу с одним и тем же триггером дважды
                schedulerFactory.getScheduler().scheduleJob(jobDetail, newTrigger);
            }
            else {
                // перезапускаем работу с новым триггером
                schedulerFactory.getScheduler().rescheduleJob(triggerKey, newTrigger);
            }
            return true;
        }
        catch (SchedulerException ex) {
            logger.error("Cannot start reschedule job", ex);
        }
        return false;
    }

    /**
     *
     * @param jobDetail
     * @return вернет true если есть запись о Job в БД
     * @throws SchedulerException
     */
    private boolean checkJobExistence(JobDetail jobDetail) throws SchedulerException {
        return jobDetail != null && !schedulerFactory.getScheduler().checkExists(jobDetail.getKey());
    }


    @Override
    public Set<TriggerKey> getTriggersByGroup(String triggerGroup) {
        try {
            return schedulerFactory.getScheduler().getTriggerKeys(GroupMatcher.groupEquals(triggerGroup));
        }
        catch (SchedulerException ex) {
            logger.error("Cannot fetch triggers by group name", ex);
        }
        return null;
    }

    @Override
    public Trigger getJobTrigger(TriggerKey triggerKey) {
        try {
            Trigger trigger = schedulerFactory.getScheduler().getTrigger(triggerKey);
            return trigger;
        }
        catch (SchedulerException ex) {
            logger.error("Unable create and set trigger", ex);
        }
        return null;
    }

    @Override
    public RpcTaskOperation getJobState(TriggerKey triggerKey) {
        // TriggerState
        try {
            TriggerState state = schedulerFactory.getScheduler().getTriggerState(triggerKey);
            logger.info("TriggerState " + state);

            if (state == TriggerState.NORMAL) {
                return RpcTaskOperation.START;
            }
            else if (state == TriggerState.PAUSED) {
                return RpcTaskOperation.PAUSE;
            }
            else if (state == TriggerState.BLOCKED || state == TriggerState.COMPLETE) {
                return RpcTaskOperation.STOP;
            }
        }
        catch (SchedulerException ex) {
            logger.error("Cannot fetch triggers by group name", ex);
        }
        return RpcTaskOperation.UNDEFINED;
    }

}

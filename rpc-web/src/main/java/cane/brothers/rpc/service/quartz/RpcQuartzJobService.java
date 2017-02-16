package cane.brothers.rpc.service.quartz;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Service;

import cane.brothers.rpc.RpcUtils;
import cane.brothers.rpc.quartz.RpcTicketJob;

@Service
public class RpcQuartzJobService implements JobService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${cane.brothers.quartz.job.name}")
    private String defaultJobName;

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    /**
     * configure job bean
     *
     * @throws SchedulerException
     */
    // @Override
    public JobDetail createJobDetail() {
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

    // @Override
    public Trigger createTrigger(TriggerKey triggerKey, long repeatInterval) {
        Trigger trigger = null;

        try {
            trigger = schedulerFactory.getScheduler().getTrigger(triggerKey);

            if (trigger == null) {
                SimpleTriggerFactoryBean triggerFactory = createTriggerFactory(triggerKey, repeatInterval);
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

    private SimpleTriggerFactoryBean createTriggerFactory(TriggerKey triggerKey, long repeatInterval)
            throws SchedulerException {
        SimpleTriggerFactoryBean trigger = null;
        if (triggerKey != null) {
            trigger = new SimpleTriggerFactoryBean();

            trigger.setJobDetail(createJobDetail());
            trigger.setStartDelay(3000);
            trigger.setRepeatInterval(repeatInterval * 1000L);
            trigger.setName(triggerKey.getName());
            trigger.setGroup(triggerKey.getGroup());
            trigger.afterPropertiesSet();
        }
        return trigger;
    }

    @Override
    public boolean startJob(TriggerKey triggerKey, long repeatInterval) {

        try {
            JobDetail jobDetail = createJobDetail();
            Trigger trigger = createTrigger(triggerKey, repeatInterval);

            if (jobDetail != null && !schedulerFactory.getScheduler().checkExists(jobDetail.getKey())) {
                // нельзя назначить работу с одним и тем же триггером дважды
                schedulerFactory.getScheduler().scheduleJob(trigger);
            }
            else {
                logger.warn("Задание уже запущенно и выполняется");
                return false;
            }

            // стартуем шедулер если не запущен
            if (!schedulerFactory.getScheduler().isStarted()) {
                schedulerFactory.getScheduler().start();
            }
            return true;
        }
        catch (SchedulerException ex) {
            logger.error("Cannot start sheduler", ex);
        }
        return false;
    }

    @Override
    public boolean stopJob(TriggerKey triggerKey) {
        try {
            if (schedulerFactory.getScheduler().checkExists(triggerKey)) {
                schedulerFactory.getScheduler().unscheduleJob(triggerKey);
                return true;
            }
            else {
                logger.error("Cannot stop {}", triggerKey);
            }
        }
        catch (SchedulerException ex) {
            logger.error("Cannot start sheduler", ex);
        }
        return false;
    }

}

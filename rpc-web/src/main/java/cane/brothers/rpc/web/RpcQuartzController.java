package cane.brothers.rpc.web;

import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;

import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cane.brothers.rpc.data.quartz.SimpleQuartzDto;

/**
 *
 * @author cane
 */
@RestController
@RequestMapping(value = "/api/rpc/quartz")
public class RpcQuartzController {

    private static final Logger log = LoggerFactory.getLogger(RpcQuartzController.class);

    @Value("${quartz.trigger.simple.name}")
    private String triggerName;

    @Value("${quartz.trigger.simple.interval}")
    private String triggerInterval;

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    // @Autowired
    // private SimpleTriggerFactoryBean triggerFactory;

    @Autowired
    private JobDetailFactoryBean jobFactory;

    @Autowired
    private TriggerKey triggerKey;

    @GetMapping("/start")
    public ResponseEntity<SimpleQuartzDto> startTask() throws SchedulerException, ParseException {
        // use default interval
        SimpleQuartzDto interval = new SimpleQuartzDto((Short.valueOf(triggerInterval)));

        // Trigger trigger = getNewTrigger(interval);
        Trigger trigger = schedulerFactory.getScheduler().getTrigger(triggerKey);

        if (trigger != null) {
            // стартуем шедулер если не запущен
            if (!schedulerFactory.getScheduler().isStarted()) {
                schedulerFactory.getScheduler().start();
            }
        }
        else {
            // нельзя назначить работу с одним и тем же триггером дважды
            if (!schedulerFactory.getScheduler().checkExists(triggerKey)) {
                trigger = getNewTrigger(interval);
                schedulerFactory.getScheduler().scheduleJob(trigger);
            }
        }

        return new ResponseEntity<SimpleQuartzDto>(interval, HttpStatus.OK);
    }

    @GetMapping("/stop")
    public ResponseEntity<String> stopTask() throws SchedulerException {
        String result = null;

        if (schedulerFactory.getScheduler().checkExists(triggerKey)) {
            schedulerFactory.getScheduler().unscheduleJob(triggerKey);
            result = "Job with trigger " + triggerKey + " was unscheduled";
        }

        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    // @GetMapping()
    // public ResponseEntity<SimpleQuartzDto> getSimpleTask() {
    // Trigger trigger = schedulerFactory.getScheduler().getTrigger(triggerKey);
    // SimpleQuartzDto interval = null;
    //
    // if(trigger != null) {
    // interval = new SimpleQuartzDto(
    // (short) (trigger.getObject().getRepeatInterval() / 1000L));
    // }
    // SimpleQuartzDto
    // return new ResponseEntity<SimpleQuartzDto>(interval, HttpStatus.OK);
    // }

    @PutMapping()
    public ResponseEntity<SimpleQuartzDto> updateSimpleTask(@RequestBody SimpleQuartzDto interval)
            throws SchedulerException, ParseException {
        log.info("new interval: " + interval);

        Trigger trigger = getNewTrigger(interval);
        schedulerFactory.getScheduler().rescheduleJob(trigger.getKey(), trigger);
        return new ResponseEntity<SimpleQuartzDto>(interval, HttpStatus.OK);
    }

    // private Trigger getTrigger(SimpleQuartzDto cron, boolean useOld) throws
    // ParseException {
    // //log.info("old trigger: " + oldTriggerFactory.getObject());
    // return (useOld ? updateOldTrigger(cron) : getNewTrigger(cron));
    // }

    // update old trigger, call afterPropertiesSet() to rebuild trigger
    // private Trigger updateOldTrigger(SimpleQuartzDto entry) throws
    // ParseException {
    // oldTriggerFactory.setRepeatInterval(entry.getIntervalInHours() * 1000L);
    // oldTriggerFactory.afterPropertiesSet();
    //
    // return oldTriggerFactory.getObject();
    // }

    // new trigger must have same name
    private Trigger getNewTrigger(SimpleQuartzDto entry) {
        Trigger trigger = newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(repeatSecondlyForever(entry.getIntervalInHours()))
                .forJob(jobFactory.getObject()).build();

        return trigger;
    }
}

package cane.brothers.rpc.web;

import java.text.ParseException;

import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cane.brothers.rpc.RpcUtils;
import cane.brothers.rpc.service.quartz.JobService;

/**
 *
 * @author cane
 */
@RestController
@RequestMapping(value = "/api/rpc/quartz")
public class RpcQuartzController {

    private static final Logger log = LoggerFactory.getLogger(RpcQuartzController.class);

    @Value("${cane.brothers.quartz.trigger.interval}")
    private Long defaultTriggerInterval;

    @Value("${cane.brothers.quartz.trigger.name}")
    private String defaultTriggerName;

    @Autowired
    private JobService jobService;

    @GetMapping("/start")
    public ResponseEntity<String> startTask() throws SchedulerException, ParseException {
        // TODO real trigger name
        TriggerKey triggerKey = new TriggerKey(defaultTriggerName, RpcUtils.getGroupName());
        if (jobService.startJob(triggerKey, defaultTriggerInterval)) {
            return new ResponseEntity<String>("job have been started.", HttpStatus.OK);
        }
        return new ResponseEntity<String>("failed to start job", HttpStatus.ACCEPTED);
    }

    @GetMapping("/stop")
    public ResponseEntity<String> stopTask() throws SchedulerException {
        // TODO real trigger name
        TriggerKey triggerKey = new TriggerKey(defaultTriggerName, RpcUtils.getGroupName());
        if (jobService.stopJob(triggerKey)) {
            return new ResponseEntity<String>("job have been stoped.", HttpStatus.OK);
        }
        return new ResponseEntity<String>("failed to stop job", HttpStatus.ACCEPTED);
    }

    /*
     * @PutMapping() public ResponseEntity<SimpleQuartzDto>
     * updateSimpleTask(@RequestBody SimpleQuartzDto interval) throws
     * SchedulerException, ParseException { log.info("new interval: " +
     * interval);
     *
     * Trigger trigger = getNewTrigger(interval);
     * schedulerFactory.getScheduler().rescheduleJob(trigger.getKey(), trigger);
     * return new ResponseEntity<SimpleQuartzDto>(interval, HttpStatus.OK); }
     */
}

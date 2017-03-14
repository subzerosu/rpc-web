package cane.brothers.rpc.service.quartz;

import java.util.Set;

import org.quartz.Trigger;
import org.quartz.TriggerKey;

public interface JobService {

    boolean createJob(TriggerKey triggerKey, long repeatInterval);

    boolean startJob(TriggerKey triggerKey);

    boolean stopJob(TriggerKey triggerKey);

    public Trigger getJobTrigger(TriggerKey triggerKey);

    // Trigger getJobTrigger(TriggerKey triggerKey);
    Set<TriggerKey> getTriggersByGroup(String triggerGroup);
}

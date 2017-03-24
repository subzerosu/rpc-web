package cane.brothers.rpc.service.quartz;

import java.util.Set;

import org.quartz.Trigger;
import org.quartz.TriggerKey;

import cane.brothers.rpc.data.quartz.RpcTaskOperation;

public interface JobService {

    boolean createJob(TriggerKey triggerKey, long repeatInterval);

    boolean startJob(TriggerKey triggerKey, long newInterval);

    boolean stopJob(TriggerKey triggerKey);

    boolean resetJob(TriggerKey triggerKey, Long newInterval);

    public Trigger getJobTrigger(TriggerKey triggerKey);

    // Trigger getJobTrigger(TriggerKey triggerKey);
    Set<TriggerKey> getTriggersByGroup(String triggerGroup);

    RpcTaskOperation getJobState(TriggerKey triggerKey);
}

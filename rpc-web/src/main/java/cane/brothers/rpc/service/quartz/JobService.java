package cane.brothers.rpc.service.quartz;

import org.quartz.TriggerKey;

public interface JobService {

    boolean startJob(TriggerKey triggerKey, long repeatInterval);

    boolean stopJob(TriggerKey triggerKey);
}

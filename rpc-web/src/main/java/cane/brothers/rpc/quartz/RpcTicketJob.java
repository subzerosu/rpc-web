package cane.brothers.rpc.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.SimpleTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author cane
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RpcTicketJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(RpcTicketJob.class);

    @Autowired
    private RpcTicketTask task;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (context.getTrigger() instanceof SimpleTrigger) {
            log.info("Simple Quartz Job started");
        }
        task.execute();
    }
}

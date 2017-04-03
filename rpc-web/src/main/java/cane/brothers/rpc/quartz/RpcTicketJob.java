package cane.brothers.rpc.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;

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
        try {
            ApplicationContext ctx = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");
            if(ctx != null) {
                OAuth2ClientContext oAuth2ClientContext = ctx.getBean(OAuth2ClientContext.class);
                if (oAuth2ClientContext != null) {
                    log.info(oAuth2ClientContext.toString());
                }
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        if (context.getTrigger() instanceof SimpleTrigger) {
            log.info("Simple Quartz Job started");
        }
        task.execute();
    }
}

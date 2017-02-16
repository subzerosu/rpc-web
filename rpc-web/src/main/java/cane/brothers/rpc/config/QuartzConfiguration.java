package cane.brothers.rpc.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import cane.brothers.rpc.quartz.AutowiringSpringBeanJobFactory;
import cane.brothers.rpc.quartz.RpcTicketJob;

@Configuration
public class QuartzConfiguration {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${quartz.trigger.simple.name}")
    private String simpleTriggerName;

    @Value("${quartz.trigger.simple.interval}")
    private Long simpleTriggerInterval;

    @PostConstruct
    public void init() {
        logger.debug("QuartzConfig initialized.");
    }

    // register all triggers and details here
    @Bean
    public SchedulerFactoryBean schedulerFactory(DataSource dataSource, ApplicationContext applicationContext)
            throws IOException, SchedulerException {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

        // allow to update triggers in DB when updating settings in config file
        schedulerFactory.setOverwriteExistingJobs(true);

        schedulerFactory.setDataSource(dataSource);

        // custom job factory of spring with DI support for @Autowired!
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactory.setJobFactory(jobFactory);

        schedulerFactory.setQuartzProperties(quartzProperties());
        schedulerFactory.setTriggers(simpleTriggerFactory().getObject());
        schedulerFactory.setJobDetails(jobDetailFactory(getTriggerGroup()).getObject());

        schedulerFactory.setAutoStartup(false);
        return schedulerFactory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        Properties properties = null;
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        }
        catch (IOException e) {
            logger.warn("Cannot load quartz.properties.");
        }

        return properties;
    }

    // configure job bean
    @Bean
    public JobDetailFactoryBean jobDetailFactory(String triggerGroup) {
        JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();

        jobDetail.setName("rpcTicket");
        jobDetail.setGroup(triggerGroup);
        jobDetail.setJobClass(RpcTicketJob.class);

        // job has to be durable to be stored in DB
        jobDetail.setDurability(true);
        return jobDetail;
    }

    // Job is scheduled after every 3 sec
    @Bean
    public SimpleTriggerFactoryBean simpleTriggerFactory() {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(jobDetailFactory(getTriggerGroup()).getObject());
        trigger.setStartDelay(3000);
        trigger.setRepeatInterval(simpleTriggerInterval * 1000L);
        trigger.setName(simpleTriggerName);
        trigger.setGroup(getTriggerGroup());
        return trigger;
    }

    /**
     * trigger name and trigger group name
     *
     * @return
     */
    @Bean
    public TriggerKey getTriggerKey(String triggerGroup) {
        return new TriggerKey(simpleTriggerName, triggerGroup);
    }

    @Bean
    public String getTriggerGroup() {
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // String triggerGroup = authentication.getName() + "Group";
        return "RpcGroup";
    }
}
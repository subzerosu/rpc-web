package cane.brothers.rpc.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import cane.brothers.rpc.quartz.AutowiringSpringBeanJobFactory;

@Configuration
public class QuartzConfiguration {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        logger.debug("QuartzConfig initialized.");
    }

    // register all triggers and details here
    @Bean
    public SchedulerFactoryBean schedulerFactory(DataSource dataSource, ApplicationContext applicationContext)
            throws SchedulerException {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

        // allow to update triggers in DB when updating settings in config file
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setDataSource(dataSource);
        // custom job factory of spring with DI support for @Autowired!
        schedulerFactory.setJobFactory(jobFactory());
        schedulerFactory.setQuartzProperties(quartzProperties());

        // schedulerFactory.setTriggers(simpleTriggerFactory().getObject());
        // schedulerFactory.setJobDetails(jobDetailFactory(getTriggerGroup()).getObject());

        schedulerFactory.setAutoStartup(false);
        return schedulerFactory;
    }

    @Bean
    public SpringBeanJobFactory jobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    private Properties quartzProperties() {
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

}
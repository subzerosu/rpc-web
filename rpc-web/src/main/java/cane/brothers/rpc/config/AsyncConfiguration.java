package cane.brothers.rpc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by mniedre on 30.03.2017.
 */
@EnableAsync(proxyTargetClass = true)
@Configuration
public class AsyncConfiguration {

//    @Bean
//    @Qualifier(value="taskExecutor")
//    public AsyncTaskExecutor taskExecutor(){
//        return new SimpleThreadPoolTaskExecutor();
//    }
}

package com.naas.admin_service.features.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class JobTaskSchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler jobTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);                        // tuỳ bạn
        scheduler.setThreadNamePrefix("job-scheduler-"); // dễ debug log
        scheduler.initialize();
        return scheduler;
    }
}

package com.authmodule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    // configures a thread pool for use in the service layer
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        log.info("Initializing thread pool...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        // helps identify these threads in the logs
        executor.setThreadNamePrefix("authmodule-");
        executor.initialize();
        return executor;
    }
}
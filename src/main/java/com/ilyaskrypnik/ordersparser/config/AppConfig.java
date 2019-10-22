package com.ilyaskrypnik.ordersparser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig {

    @Value("${maxPoolSize}")
    private int maxPoolSize;

    @Value("${corePoolSize}")
    private int corePoolSize;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setThreadNamePrefix("Async");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
}

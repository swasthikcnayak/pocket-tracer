package com.pocket.services.expense.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExpenseConfiguration {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    // Minimum threads
        executor.setMaxPoolSize(10);   // Maximum threads
        executor.setQueueCapacity(100); // Task queue size
        executor.setThreadNamePrefix("Expense-Analytics-Executor");
        executor.initialize();
        return executor;
    }
}

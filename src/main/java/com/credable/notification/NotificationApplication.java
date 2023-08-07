package com.credable.notification;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
//@EnableCaching
@ComponentScan("com.credable.notification")
public class NotificationApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger( NotificationApplication.class);

    public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
    }
    
    @Bean
    RestTemplate restTemplate() {
      return new RestTemplate();
    }

    @Bean(name = "notificationThread")
    @Primary
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.initialize();
        executor.setThreadNamePrefix("Async-");
        return executor;
    }
}

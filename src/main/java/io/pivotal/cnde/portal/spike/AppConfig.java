package io.pivotal.cnde.portal.spike;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class AppConfig {

    @Bean
    public JobIdGenerator jobIdGenerator() {
        return new JobIdGenerator(new AtomicInteger());
    }
}

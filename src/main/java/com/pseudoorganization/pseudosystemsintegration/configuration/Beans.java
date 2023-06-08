package com.pseudoorganization.pseudosystemsintegration.configuration;

import com.pseudoorganization.pseudosystemsintegration.services.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean
    public HelloService helloService() {
        return new HelloService();
    }
}

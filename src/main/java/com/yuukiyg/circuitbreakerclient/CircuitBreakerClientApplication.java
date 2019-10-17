package com.yuukiyg.circuitbreakerclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.ConfigurableApplicationContext;

import com.yuukiyg.circuitbreakerclient.app.SendRestService;
@EnableHystrix

@SpringBootApplication
public class CircuitBreakerClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(
                CircuitBreakerClientApplication.class, args);
        SendRestService service = context.getBean(SendRestService.class);
        service.execute();
    }

}
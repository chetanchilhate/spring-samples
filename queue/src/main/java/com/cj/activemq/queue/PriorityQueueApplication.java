package com.cj.activemq.queue;

import com.cj.activemq.queue.client.rest.JolokiaClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PriorityQueueApplication {

  public static void main(String[] args) {
    SpringApplication.run(PriorityQueueApplication.class, args);
  }

  @Bean
  ApplicationRunner applicationRunner(JolokiaClient jolokiaClient) {
    return args -> {
      int health = jolokiaClient.health();
      if (200 != health) {
        throw new RuntimeException("jolokia health check failed");
      }

    };
  }

}

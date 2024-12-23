package com.cj.activemq.queue.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@ToString
@Configuration
@Setter(AccessLevel.PACKAGE)
@ConfigurationProperties(prefix = "jolokia")
public class JolokiaConfig {
  private String endpoint;
  private String authorization;
}


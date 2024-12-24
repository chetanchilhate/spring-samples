package com.cj.activemq.queue.config;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  private final String jolokiaEndpoint;
  private final ActiveMQProperties activeMQProperties;

  public RestClientConfig(@Value("${jolokia.endpoint}") String jolokiaEndpoint, ActiveMQProperties activeMQProperties) {
    this.jolokiaEndpoint = jolokiaEndpoint;
    this.activeMQProperties = activeMQProperties;
  }

  @Bean
  public RestClient jolokiaRestClient() {
    var authorization = activeMQProperties.getUser() + ":" + activeMQProperties.getPassword();
    var encodedAuthorization = "Basic " + Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8));

    return RestClient.builder()
        .baseUrl(jolokiaEndpoint)
        .defaultHeader(HttpHeaders.ORIGIN, jolokiaEndpoint)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.AUTHORIZATION, encodedAuthorization)
        .build();
  }

}

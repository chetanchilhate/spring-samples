package com.cj.activemq.queue.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@AllArgsConstructor
public class RestClientConfig {

  private final JolokiaConfig jolokiaConfig;

  @Bean
  public RestClient jolokiaRestClient() {
    return RestClient.builder()
        .baseUrl(jolokiaConfig.getEndpoint())
        .defaultHeader(HttpHeaders.ORIGIN, jolokiaConfig.getEndpoint())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.AUTHORIZATION, jolokiaConfig.getAuthorization())
        .build();
  }

}

package com.cj.oauth2client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  WebClient oauth2WebClient(WebClient.Builder builder, OAuth2AuthorizedClientManager authorizedClientManager) {
    final var oauth = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
    oauth.setDefaultClientRegistrationId("wso2");
    return builder.filter(oauth).build();
  }

  @Bean
  RestClient restClient(RestClient.Builder builder, OAuth2AuthorizedClientManager authorizedClientManager) {
    var interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
    return builder.requestInterceptor(interceptor).build();
  }

}

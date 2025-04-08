package com.cj.oauth2client.controller;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import java.net.URI;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
class PriceController {

  private final WebClient webClient;
  private final RestClient restClient;

  @Value("${api.smart-price.endpoint}")
  private String smartPriceEndpoint;

  PriceController(WebClient webClient, RestClient restClient) {
    this.webClient = webClient;
    this.restClient = restClient;
  }

  @GetMapping("/v1/price")
  Mono<JSONObject> getSmartPrice(@RequestParam("customer") String customer, @RequestParam("material") String material) {
    var smartPriceUri = buildSmartPriceUri(customer, material);
    return webClient.get().uri(smartPriceUri).retrieve().bodyToMono(JSONObject.class);
  }

  @GetMapping("/v2/price")
  JSONObject getSmartPrice2(@RequestParam("customer") String customer, @RequestParam("material") String material) {
    var smartPriceUri = buildSmartPriceUri(customer, material);
    return restClient.get().uri(smartPriceUri).attributes(clientRegistrationId("wso2")).retrieve().body(JSONObject.class);
  }

  private URI buildSmartPriceUri(String customer, String material) {
    return UriComponentsBuilder.fromUriString(smartPriceEndpoint).buildAndExpand(customer, material).toUri();
  }

}

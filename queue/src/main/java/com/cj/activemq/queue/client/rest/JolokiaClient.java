package com.cj.activemq.queue.client.rest;

import com.cj.activemq.queue.util.OrderUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@AllArgsConstructor
public class JolokiaClient {

  private final RestClient jolokiaRestClient;

  public int health() {
    var response = jolokiaRestClient.get().retrieve().body(String.class);
    var gson = new GsonBuilder().create();
    var jsonObject = gson.fromJson(response, JsonObject.class);
    log.info("jolokia response : {}", gson.toJson(jsonObject));
    return jsonObject.getAsJsonPrimitive("status").getAsInt();
  }

  public void deleteMessage(String destination, String messageId) {
    var requestBody = OrderUtils.createRemoveMessageRequestBody(destination, messageId);

    var response = jolokiaRestClient.post().body(requestBody).retrieve().body(String.class);
    var gson = new GsonBuilder().create();
    var jsonObject = gson.fromJson(response, JsonObject.class);

    var status = jsonObject.getAsJsonPrimitive("status").getAsInt();
    log.info("message id : {} deleted : {}", messageId, status == 200);
  }

}

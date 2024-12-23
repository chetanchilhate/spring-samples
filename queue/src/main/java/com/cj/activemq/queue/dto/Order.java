package com.cj.activemq.queue.dto;

import com.google.gson.Gson;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class Order {

  private static final Gson GSON = new Gson();

  private String id;
  private String customer;
  private long volume;
  private double value;

  public static Order fromJson(String json) {
    return GSON.fromJson(json, Order.class);
  }

  public static Order fromJmsMessage(Message message) {
    if (!(message instanceof TextMessage textMessage)) {
      throw new RuntimeException("invalid message");
    }
    try {
      return fromJson(textMessage.getText());
    } catch (JMSException ex) {
      log.warn("caused by : {}", ex.getMessage());
      throw new RuntimeException("invalid message");
    }
  }

}

package com.cj.activemq.queue.listener;

import com.cj.activemq.queue.dto.Order;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderListener {

  public static final List<Order> RECEIVED_ORDERS = new ArrayList<>();

  private final String destination;

  public OrderListener(@Value("${activemq.destination}") String destination) {
    this.destination = destination;
  }

  @JmsListener(destination = "${activemq.destination}")
  public void listenMessage(Message<Order> orderMessage) throws InterruptedException {
    log.info("received order : {}", orderMessage.getPayload());
    var order = orderMessage.getPayload();
    order.setPriority((int)orderMessage.getHeaders().get("jms_priority"));
    Thread.sleep(4700L);
    RECEIVED_ORDERS.add(order);
  }

  @PostConstruct
  private void printQueueName() {
    log.info("listening on queue : {}", destination);
  }

}

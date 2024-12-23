package com.cj.activemq.queue.listener;

import com.cj.activemq.queue.dto.Order;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderListener {

  public static final List<Order> RECEIVED_ORDERS = new ArrayList<>();

  @JmsListener(destination = "order-queue")
  public void listenMessage(Order order) throws InterruptedException {
    log.info("received order : {}", order);
    Thread.sleep(2000L);
    RECEIVED_ORDERS.add(order);
  }

}

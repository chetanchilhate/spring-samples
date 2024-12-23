package com.cj.activemq.queue.controller;

import com.cj.activemq.queue.browser.JmsMessageBrowser;
import com.cj.activemq.queue.dto.Order;
import com.cj.activemq.queue.listener.OrderListener;
import com.cj.activemq.queue.producer.OrderProducer;
import com.cj.activemq.queue.util.OrderUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final OrderProducer orderProducer;
  private final JmsMessageBrowser jmsMessageBrowser;

  private final String destination;

  public OrderController(OrderProducer orderProducer, JmsMessageBrowser jmsMessageBrowser, @Value("${activemq.destination}") String destination) {
    this.orderProducer = orderProducer;
    this.jmsMessageBrowser = jmsMessageBrowser;
    this.destination = destination;
  }

  @PostMapping("/publish")
  public String publishMessage(@RequestBody Order order) {
    orderProducer.sendTo(destination, order);
    return "Success";
  }

  @GetMapping("/publish-bulk/{count}")
  public String publishBulkMessage(@PathVariable int count) {
    OrderListener.RECEIVED_ORDERS.clear();
    OrderUtils.generateRandomOrders(count).forEach(order -> orderProducer.sendTo(destination, order));
    return "Success";
  }

  @GetMapping("/priority/{orderId}")
  public String prioritizeOrder(@PathVariable String orderId) {
    jmsMessageBrowser.browseMessages(destination, orderId).forEach(message -> orderProducer.prioritizeMessage(destination, message));
    return "Success";
  }

  @GetMapping("/active")
  public List<Order> getActiveOrders() {
    var activeOrders = new ArrayList<Order>();
    for (var message : jmsMessageBrowser.browseMessages(destination)) {
      try {
        activeOrders.add(Order.fromJmsMessage(message));
      } catch (RuntimeException ex) {
        log.warn("caused by : {}", ex.getMessage());
      }
    }
    return activeOrders;
  }

  @GetMapping("/received")
  public List<Order> getReceivedOrders() {
    return OrderListener.RECEIVED_ORDERS;
  }

}

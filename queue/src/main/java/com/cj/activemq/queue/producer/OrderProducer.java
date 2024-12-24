package com.cj.activemq.queue.producer;

import com.cj.activemq.queue.client.rest.JolokiaClient;
import com.cj.activemq.queue.dto.Order;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.DeliveryMode;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class OrderProducer {

  private final JmsTemplate jmsTemplate;
  private final JolokiaClient jolokiaClient;
  private final ConnectionFactory connectionFactory;

  public void sendTo(String destination, Order order) {
    jmsTemplate.convertAndSend(destination, order, message -> {
      message.setJMSCorrelationID(order.getId());
      return message;
    });
    log.info("Producer> Message Sent");
  }

  public void prioritizeMessage(String destination, Message message) {
    try {
      var messageId = message.getJMSMessageID();
      sendOnPriority(destination, message);
      deleteExistingMessage(destination, messageId);
    } catch (JMSException ex) {
      throw new RuntimeException("Failed to prioritize message", ex);
    }
  }

  public void sendOnPriority(String destination, Message message) {
    try (var connection = connectionFactory.createConnection()) {
      connection.start();
      var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      var queue = session.createQueue(destination);
      var producer = session.createProducer(queue);
      var oneHourTtl = 1000L * 3600L;
      producer.send(message, DeliveryMode.PERSISTENT, 9, oneHourTtl);
      log.info("Producer> Message Sent On Priority");
    } catch (JMSException e) {
      throw new RuntimeException("Failed to send message", e);
    }
  }

  public void deleteExistingMessage(String destination, String messageId) {
    jolokiaClient.deleteMessage(destination, messageId);
  }

}

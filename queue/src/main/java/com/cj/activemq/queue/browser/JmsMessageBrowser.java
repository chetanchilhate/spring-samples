package com.cj.activemq.queue.browser;

import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class JmsMessageBrowser {

  private final JmsTemplate jmsTemplate;

  public List<Message> browseMessages(String queueName, String correlationId) {

    var selector = "JMSCorrelationID = '" + correlationId + "'";

    var selectedMessages = jmsTemplate.browseSelected(queueName, selector, collectTextMessages());

    log.info("found messages : {}", !selectedMessages.isEmpty());

    return selectedMessages;
  }

  public List<Message> browseMessages(String queueName) {

    return jmsTemplate.browse(queueName, collectTextMessages());
  }

  private static BrowserCallback<List<Message>> collectTextMessages() {
    return (session, browser) -> {

      var messages = new ArrayList<Message>();
      var enumeration = browser.getEnumeration();

      while (enumeration.hasMoreElements()) {
        var rawMessage = enumeration.nextElement();

        if (!(rawMessage instanceof TextMessage textMessage)) {
          continue;
        }
        messages.add(textMessage);
      }

      return messages;
    };
  }

}

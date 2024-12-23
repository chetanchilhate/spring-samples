package com.cj.activemq.queue.util;

import com.cj.activemq.queue.dto.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class OrderUtils {

  private static final List<String> FIRST_NAMES = List.of("John", "Jane", "Alex", "Emily", "Chris", "Katie", "Michael", "Sarah", "David", "Laura");
  private static final List<String> LAST_NAMES = List.of("Smith", "Johnson", "Brown", "Williams", "Jones", "Garcia", "Martinez", "Miller", "Davis");

  public static List<Order> generateRandomOrders(int count) {
    var random = new Random();
    var randomFullNames = generateRandomFullNames(random);
    var orders = new ArrayList<Order>();
    for (int i = 0; i < count; i++) {
      orders.add(generateRandomOrder(random, randomFullNames));
    }
    return orders;
  }

  private static Order generateRandomOrder(Random random, List<String> randomFullNames) {
    var order = new Order();

    var id = random.nextInt(10000);
    order.setId(String.format("%05d", id));

    order.setCustomer(randomFullNames.get(random.nextInt(randomFullNames.size())));

    var value = random.nextDouble(10, 10000);
    var roundOffValue = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
    order.setValue(roundOffValue.doubleValue());

    var volume = random.nextInt(1000);
    order.setVolume(volume);

    return order;
  }

  private static List<String> generateRandomFullNames(Random random) {
    return IntStream.range(0, 100).mapToObj(i -> {
      var firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
      var lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
      return firstName + " " + lastName;
    }).toList();
  }

  public static String createRemoveMessageRequestBody(String destination, String messageId) {
    return """
        {
            "type": "exec",
            "mbean": "org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=%s",
            "operation": "removeMessage(java.lang.String)",
            "arguments": [
                "%s"
            ]
        }
        """.formatted(destination, messageId);
  }

}

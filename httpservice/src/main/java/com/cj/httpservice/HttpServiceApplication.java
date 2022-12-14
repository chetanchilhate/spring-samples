package com.cj.httpservice;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@ConfigurationProperties(prefix = "todo")
record TodoProps(String baseUrl) {}

@SpringBootApplication
@EnableConfigurationProperties(TodoProps.class)
public class HttpServiceApplication {

  private static final Logger log = LoggerFactory.getLogger(HttpServiceApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(HttpServiceApplication.class, args);
  }

  @Bean
  @ConditionalOnProperty(value = "todo.base-url")
  TodoClient todoClient(TodoProps props) {

    var webClient = WebClient.builder().baseUrl(props.baseUrl()).build();

    var factory = HttpServiceProxyFactory.builder().clientAdapter(WebClientAdapter.forClient(webClient)).build();

    return factory.createClient(TodoClient.class);
  }

  @Bean
  @ConditionalOnBean(TodoClient.class)
  ApplicationRunner applicationRunner(TodoClient todoClient) {
    return args -> {
      log.info(todoClient.todos().toString());
      log.info(todoClient.get(4L).toString());
      log.info(todoClient.createTodo(new Todo(null, "redis hashmap", false, 1L)).toString());
    };
  }

}

@HttpExchange("/todos")
interface TodoClient {

  @GetExchange
  List<Todo> todos();

  @GetExchange("/{todoId}")
  Todo get(@PathVariable("todoId") Long id);

  @PostExchange
  Todo createTodo(@RequestBody Todo todo);

}

record Todo(Long id, String title, boolean completed, Long userId) {}

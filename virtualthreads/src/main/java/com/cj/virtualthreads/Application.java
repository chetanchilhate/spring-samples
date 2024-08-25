package com.cj.virtualthreads;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  RestClient creatRestClient() {
    return RestClient.create();
  }

}

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class TodoController {

  RestClient restClient;

  @GetMapping("api/v1/todo")
  Todo getTodo() {
    return restClient.get().uri("http://localhost:3000/todo")
        .retrieve()
        .body(Todo.class);
  }

}

record Todo(int id, String todo) {
}

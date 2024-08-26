package com.cj.virtualthreads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  RestClient creatRestClient() {
    return RestClient.create();
  }

  @Bean
  WebClient createWebClient() {
    return WebClient.create();
  }

}

@RestController
class TodoController {

  private static final String SLOW_TODO_URL = "http://localhost:3000/slow-todo";
  private final WebClient webClient;
  private final RestClient restClient;

  TodoController(WebClient webClient, RestClient restClient) {
    this.webClient = webClient;
    this.restClient = restClient;
  }

  @GetMapping("api/virtual/todo")
  Todo getTodo() {
    return restClient.get().uri(SLOW_TODO_URL).retrieve().body(Todo.class);
  }

  @GetMapping("api/reactive/todo")
  Mono<Todo> getTodoReactive() {
    return webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class);
  }

  @GetMapping("api/block/todo")
  Todo getTodoBlock() {
    return webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class).block();
  }

  @GetMapping("api/virtual/todos")
  List<Todo> getTodos() {
    var todo1 = restClient.get().uri(SLOW_TODO_URL).retrieve().body(Todo.class);

    var todo2 = restClient.get().uri(SLOW_TODO_URL).retrieve().body(Todo.class);

    return List.of(todo1, todo2);
  }

  @GetMapping("api/reactive/todos")
  Mono<List<Todo>> getTodosReactive() {
    var todo1 = webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class);

    var todo2 = webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class);

    return Mono.zip(todo1, todo2).map(tuple -> List.of(tuple.getT1(), tuple.getT2()));
  }

  @GetMapping("api/block/todos")
  List<Todo> getTodosBlock() {
    var todo1 = webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class);

    var todo2 = webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class);

    return Mono.zip(todo1, todo2).map(tuple -> List.of(tuple.getT1(), tuple.getT2())).block();
  }

  @GetMapping("api/concurrency/todos")
  List<Todo> getConcurrencyTodos() {
    try (final var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      var todo1 = restClient.get().uri(SLOW_TODO_URL).retrieve().body(Todo.class);
      var todo2 = restClient.get().uri(SLOW_TODO_URL).retrieve().body(Todo.class);
      var subTaskTodo1 = scope.fork(() -> todo1);
      var subTaskTodo2 = scope.fork(() -> todo2);
      scope.join();
      return List.of(subTaskTodo1.get(), subTaskTodo2.get());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}

record Todo(int id, String todo) {
}

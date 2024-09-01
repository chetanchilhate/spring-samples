package com.cj.virtualthreads;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
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

  @GetMapping("api/rest-client/todo")
  Todo getTodoRestClient() {
    return restClient.get().uri(SLOW_TODO_URL).retrieve().body(Todo.class);
  }

  @GetMapping("api/web-client/todo")
  Mono<Todo> getTodoWebClient() {
    return webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class);
  }

  @GetMapping("api/web-client-block/todo")
  Todo getTodoWebClientBlock() {
    return webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class).block();
  }

  @GetMapping("api/rest-client/todos")
  List<Todo> getTodosRestClient() {
    var todo1 = restClient.get().uri(SLOW_TODO_URL).retrieve().body(Todo.class);

    var todo2 = restClient.get().uri(SLOW_TODO_URL).retrieve().body(Todo.class);

    Objects.requireNonNull(todo1);
    Objects.requireNonNull(todo2);

    return List.of(todo1, todo2);
  }

  @GetMapping("api/web-client/todos")
  Mono<List<Todo>> getTodosWebClient() {
    var todo1 = webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class);

    var todo2 = webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class);

    return Mono.zip(todo1, todo2).map(tuple -> List.of(tuple.getT1(), tuple.getT2()));
  }

  @GetMapping("api/web-client-block/todos")
  List<Todo> getTodosBlock() {
    var todo1 = webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class);

    var todo2 = webClient.get().uri(SLOW_TODO_URL).retrieve().bodyToMono(Todo.class);

    return Mono.zip(todo1, todo2).map(tuple -> List.of(tuple.getT1(), tuple.getT2())).block();
  }

  @GetMapping("api/http-client/todos")
  List<Todo> getTodosHttpClient() {

    try (var client = HttpClient.newHttpClient()) {

      var request = HttpRequest.newBuilder().uri(URI.create(SLOW_TODO_URL)).GET().build();

      var response1 = client.send(request, HttpResponse.BodyHandlers.ofString());
      var response2 = client.send(request, HttpResponse.BodyHandlers.ofString());

      return List.of(Todo.fromJson(response1), Todo.fromJson(response2));

    } catch (IOException | InterruptedException e) {
      return List.of();
    }
  }

  @GetMapping("api/http-client-async/todos")
  List<Todo> getTodosHttpClientAsync() {

    try (var client = HttpClient.newHttpClient()) {

      var request = HttpRequest.newBuilder().uri(URI.create(SLOW_TODO_URL)).GET().build();

      var response1 = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
      var response2 = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

      return List.of(Todo.fromJson(response1), Todo.fromJson(response2));
    }
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

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private static Todo fromJson(String json) {
    return GSON.fromJson(json, Todo.class);
  }

  public static Todo fromJson(HttpResponse<String> json) {
    return fromJson(json.body());
  }

  public static Todo fromJson(CompletableFuture<HttpResponse<String>> json) {
    return fromJson(json.join());
  }

}

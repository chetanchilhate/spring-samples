package com.cj.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class HelloController {

  @PostMapping(value = "/call")
  public ResponseEntity<Object> postCall(@RequestBody Object data) {
    log.info(data.toString());
    return ResponseEntity.ok(data);
  }

  @PostMapping("/hello")
  public ResponseEntity<Data> postData(@RequestBody ReqData data) {
    log.info(data.toString());
    return ResponseEntity.ok(new Data(data.name()));
  }

  @PostMapping("/foo")
  public ResponseEntity<Foo> postFoo(@RequestBody FooData data) {
    log.info(data.toString());
    return ResponseEntity.ok(new Foo(data.name(), data.address()));
  }

}

record Data(String name) {}
record ReqData(String path, String name) {}

record Foo(String name, String address) {}
record FooData(String path, String name, String address) {}

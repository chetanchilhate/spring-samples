package com.cj.oauth2client.utils;

import static com.cj.oauth2client.utils.ErrorStrategy.collectErrors;
import static com.cj.oauth2client.utils.ErrorStrategy.failFast;
import static com.cj.oauth2client.utils.ErrorStrategy.skipErrors;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import reactor.core.publisher.Mono;


public class DemoApp {
  public static void main(String[] args) {

    List<Mono<String>> monos = Arrays.asList(Mono.just("A").delayElement(Duration.ofMillis(300)),
        Mono.error(new RuntimeException("Boom")),
        Mono.just("C").delayElement(Duration.ofMillis(100)));


    ReactorUtils.combineMonos(monos, skipErrors(), 2).subscribe(res -> System.out.println("SkipErrors => " + res));

    ReactorUtils.combineMonos(monos, collectErrors(), 3).subscribe(res -> System.out.println("CollectErrors => " + res));

    ReactorUtils.combineMonos(monos, failFast(), 2)
        .subscribe(res -> System.out.println("FailFast => " + res), err -> System.err.println("FailFast Error => " + err));

    try {
      Thread.sleep(1000);
    } catch (InterruptedException ignored) {
    }
  }
}

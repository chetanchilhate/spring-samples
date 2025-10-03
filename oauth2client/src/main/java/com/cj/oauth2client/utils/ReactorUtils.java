package com.cj.oauth2client.utils;

import java.util.Comparator;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactorUtils {

  private static final int DEFAULT_MAX_CONCURRENCY = 4;

  public static <T> Mono<ResultWithErrors<T>> combineMonos(List<Mono<T>> monos, ErrorStrategy<T> strategy) {
    return combineMonos(Flux.fromIterable(monos), strategy, DEFAULT_MAX_CONCURRENCY);
  }

  public static <T> Mono<ResultWithErrors<T>> combineMonos(Flux<Mono<T>> monoFlux, ErrorStrategy<T> strategy) {
    return combineMonos(monoFlux, strategy, DEFAULT_MAX_CONCURRENCY);
  }

  public static <T> Mono<ResultWithErrors<T>> combineMonos(List<Mono<T>> monos, ErrorStrategy<T> strategy, int maxConcurrency) {
    return combineMonos(Flux.fromIterable(monos), strategy, maxConcurrency);
  }

  public static <T> Mono<ResultWithErrors<T>> combineMonos(Flux<Mono<T>> monoFlux, ErrorStrategy<T> strategy, int maxConcurrency) {
    return monoFlux.index()
        .flatMap(tuple -> strategy.apply(tuple.getT1(), tuple.getT2()), maxConcurrency)
        .sort(Comparator.comparingLong(OrderedResult::index))
        .collect(ResultWithErrors::new, (acc, orderedResult) -> orderedResult.collect(acc));
  }

}

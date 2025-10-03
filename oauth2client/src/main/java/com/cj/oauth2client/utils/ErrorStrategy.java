package com.cj.oauth2client.utils;

import reactor.core.publisher.Mono;

/* ErrorStrategy is a functional interface that defines how to handle errors when processing Monos.
 * It provides different strategies for dealing with errors, such as failing fast, skipping errors, or collecting errors.
 *
 * @param <T> the type of the value emitted by the Mono
 */
@FunctionalInterface
public interface ErrorStrategy<T> {

  /**
   * A fail-fast error strategy that returns an OrderedResult immediately upon encountering a Mono.
   * If the Mono emits a value, it will be included in the OrderedResult; if it errors, the error will be ignored.
   *
   * @param <T> the type of the value emitted by the Mono
   * @return an ErrorStrategy that fails fast
   */
  static <T> ErrorStrategy<T> failFast() {
    return (index, mono) -> mono.map(v -> new OrderedResult<>(index, v, null));
  }

  /**
   * Skips errors and returns an OrderedResult for each Mono.
   * If an error occurs, it will not be included in the OrderedResult.
   *
   * @param <T> the type of the value emitted by the Mono
   * @return an ErrorStrategy that skips errors
   */
  static <T> ErrorStrategy<T> skipErrors() {
    return (index, mono) -> mono.map(v -> new OrderedResult<>(index, v, null)).onErrorResume(e -> Mono.empty());
  }

  /**
   * Collects errors and results, returning an OrderedResult for each Mono.
   * If an error occurs, it will be included in the OrderedResult.
   *
   * @param <T> the type of the value emitted by the Mono
   * @return an ErrorStrategy that collects errors
   */
  static <T> ErrorStrategy<T> collectErrors() {
    return (index, mono) -> mono.map(v -> new OrderedResult<>(index, v, null)).onErrorResume(e -> Mono.just(new OrderedResult<>(index, null, e)));
  }

  /**
   * Applies the error strategy to the given index and Mono.
   *
   * @param index the index of the Mono in the original list
   * @param mono  the Mono to apply the strategy to
   * @return a Mono that emits an OrderedResult containing the index, value, or error
   */

  Mono<OrderedResult<T>> apply(long index, Mono<T> mono);
}

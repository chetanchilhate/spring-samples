package com.cj.oauth2client.utils;

/* This class represents an ordered result from processing a Mono.
 * It contains the index of the Mono, the value if it was successful, or an error if it occurred.
 * The collect method is used to add the result or error to a ResultWithErrors instance.
 *
 * @param <T> the type of the value emitted by the Mono
 */
public record OrderedResult<T>(long index, T value, Throwable error) {

  void collect(ResultWithErrors<T> result) {
    if (error != null) {
      result.addError(error);
    } else {
      result.addResult(value);
    }
  }

}

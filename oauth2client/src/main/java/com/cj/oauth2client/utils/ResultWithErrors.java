package com.cj.oauth2client.utils;

import java.util.ArrayList;
import java.util.List;

/* This class is used to collect results and errors from asynchronous operations.
 * It allows you to keep track of successful results and any errors that occurred during processing.
 */
public record ResultWithErrors<T>(List<T> results, List<Throwable> errors) {

  public ResultWithErrors() {
    this(new ArrayList<>(), new ArrayList<>());
  }

  public void addResult(T result) {
    this.results.add(result);
  }

  public void addError(Throwable error) {
    this.errors.add(error);
  }

  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  @Override
  public String toString() {
    return "Results=" + results + ", Errors=" + errors;
  }
}

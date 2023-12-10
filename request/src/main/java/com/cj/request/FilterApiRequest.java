package com.cj.request;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(1)
@Component
public class FilterApiRequest implements Filter {

  @Value("${endpoint.filter.paths}")
  private List<String> filterPaths;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    var wrappedRequest = new RequestWrapper((HttpServletRequest) request);
    var requestURI = wrappedRequest.getRequestURI();
    if (filterPaths.contains(wrappedRequest.getPath())) {
      var version = "v1/";
      var trimmedRequestURI = requestURI.substring(0, requestURI.lastIndexOf(version) + version.length());
      var modifiedRequestURI = trimmedRequestURI.concat(wrappedRequest.getPath());
      log.info("requestURI : {}", requestURI);
      log.info("modifiedRequestURI : {}", modifiedRequestURI);
      wrappedRequest.getRequestDispatcher(modifiedRequestURI)
                    .forward(wrappedRequest, response);
    } else {
      chain.doFilter(wrappedRequest, response);
    }

  }

}




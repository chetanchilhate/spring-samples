package com.cj.httpservice;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "todo")
public record TodoProps(String baseUrl) {}

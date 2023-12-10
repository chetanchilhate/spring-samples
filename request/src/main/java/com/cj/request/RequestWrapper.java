package com.cj.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {
  private final String path;
  private final String body;

  public RequestWrapper(HttpServletRequest request) throws IOException {
    // So that other request method behave just like before
    super(request);

    var stringBuilder = new StringBuilder();

    try(var inputStream = request.getInputStream();
        var bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
        char[] charBuffer = new char[128];
        int bytesRead;
        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
          stringBuilder.append(charBuffer, 0, bytesRead);
        }
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
    // Store request body content in 'requestBody' variable
    var requestBody = stringBuilder.toString();
    var objectMapper = new ObjectMapper();
    var jsonNode = objectMapper.readTree(requestBody);
    var pathNode = jsonNode.get("path");

    path = pathNode.asText();
    // Finally store updated request body content in 'body' variable
    body = jsonNode.toString();

  }

  public String getPath() {
    return path;
  }

  public String getBody() {
    return body;
  }

  @Override
  public ServletInputStream getInputStream() {
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
    return new ServletInputStream() {
      public int read() {
        return byteArrayInputStream.read();
      }

      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setReadListener(ReadListener listener) {
        //do nothing
      }
    };
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(this.getInputStream()));
  }

}
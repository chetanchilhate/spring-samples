package com.cj.ai.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class ChatApplication {

  static void main(String[] args) {
    SpringApplication.run(ChatApplication.class, args);
  }

  @Bean
  ApplicationRunner init(ChatModel chatModel) {
    return args -> {
      log.info("Chat Application started...");

      var response = chatModel.call(new Prompt("who am i?"));

      log.info("response : {}", response);
    };
  }

}

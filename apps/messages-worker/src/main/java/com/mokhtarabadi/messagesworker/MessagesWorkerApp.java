package com.mokhtarabadi.messagesworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class MessagesWorkerApp {

  public static void main(String[] args) {
    SpringApplication.run(MessagesWorkerApp.class, args);
  }
}

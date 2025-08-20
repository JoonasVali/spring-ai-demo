package com.github.joonasvali.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

  private final ChatService chatService;

  public MyController(ChatService chatService) {
    this.chatService = chatService;
  }

  @GetMapping("/")
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("Service is running");
  }

  @GetMapping("/joke")
  public ResponseEntity<String> getJoke(@RequestParam(required = false) String subject) {
    try {
      String joke = chatService.generateJoke(subject);
      return ResponseEntity.ok(joke);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to generate joke: " + e.getMessage());
    }
  }
}
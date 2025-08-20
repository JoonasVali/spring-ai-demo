package com.github.joonasvali.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling chat-related endpoints.
 * Provides endpoints for generating jokes and custom AI responses.
 */
@RestController
public class MyController {

    private final ChatService chatService;

    /**
     * Constructs the controller with the required ChatService.
     * 
     * @param chatService the service for handling chat operations
     */
    public MyController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Health check endpoint.
     * 
     * @return HTTP 200 OK status
     */
    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is running");
    }

    /**
     * Generates a joke about the specified subject.
     * 
     * @param subject the topic for the joke (optional, defaults to programming)
     * @return the generated joke
     */
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

    /**
     * Generates a programming joke (default topic).
     * 
     * @return the generated programming joke
     */
    @GetMapping("/joke/programming")
    public ResponseEntity<String> getProgrammingJoke() {
        try {
            String joke = chatService.generateProgrammingJoke();
            return ResponseEntity.ok(joke);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate programming joke: " + e.getMessage());
        }
    }

    /**
     * Generates a custom AI response based on the provided prompt.
     * 
     * @param prompt the custom prompt for the AI
     * @return the generated response
     */
    @GetMapping("/chat")
    public ResponseEntity<String> getCustomResponse(@RequestParam String prompt) {
        try {
            String response = chatService.generateCustomResponse(prompt);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid prompt: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate response: " + e.getMessage());
        }
    }
}
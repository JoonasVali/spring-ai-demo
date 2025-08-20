package com.github.joonasvali.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ChatService {

  private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
  private static final String DEFAULT_JOKE_SUBJECT = "programming";

  private final ChatClient chatClient;

  public ChatService(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  public String generateJoke(String subject) {
    try {
      String jokeSubject = StringUtils.hasText(subject) ? subject.trim() : DEFAULT_JOKE_SUBJECT;
      logger.debug("Generating joke about: {}", jokeSubject);

      Prompt prompt = new Prompt("Tell a funny joke about " + jokeSubject + ".");
      ChatResponse response = chatClient.prompt(prompt).call().chatResponse();

      if (response == null) {
        throw new ChatServiceException("Invalid response from AI service");
      }

      String joke = response.getResult().getOutput().getText();
      logger.debug("Successfully generated joke about: {}", jokeSubject);

      return joke;

    } catch (Exception e) {
      logger.error("Error generating joke about '{}': {}", subject, e.getMessage(), e);
      throw new ChatServiceException("Failed to generate joke: " + e.getMessage(), e);
    }
  }
}
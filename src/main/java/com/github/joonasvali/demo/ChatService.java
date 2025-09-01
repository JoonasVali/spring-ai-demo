package com.github.joonasvali.demo;

import com.github.joonasvali.demo.model.Joke;
import com.github.joonasvali.demo.tools.RandomNumberTools;
import com.github.joonasvali.demo.tools.RandomTopicTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ChatService {

  private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
  private static final String DEFAULT_JOKE_SUBJECT = "random topic";

  private final ChatClient chatClient;

  @Autowired
  private RandomNumberTools randomNumberTools;

  @Autowired
  private RandomTopicTools randomTopicTools;

  public ChatService(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  public Joke generateJoke(String subject) {
    try {
      String jokeSubject = StringUtils.hasText(subject) ? subject.trim() : DEFAULT_JOKE_SUBJECT;
      logger.debug("Generating joke about: {}", jokeSubject);

      Prompt prompt = new Prompt("Tell a funny joke about " + jokeSubject + ".");
      Joke joke = chatClient.prompt(prompt).tools(randomNumberTools, randomTopicTools).call().entity(Joke.class);

      logger.debug("Successfully generated joke about: {}", jokeSubject);

      return joke;

    } catch (Exception e) {
      logger.error("Error generating joke about '{}': {}", subject, e.getMessage(), e);
      throw new ChatServiceException("Failed to generate joke: " + e.getMessage(), e);
    }
  }
}
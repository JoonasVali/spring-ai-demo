package com.github.joonasvali.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.joonasvali.demo.ChatServiceException;

/**
 * Service for handling AI chat interactions.
 * Provides methods to generate jokes and other AI-powered content.
 */
@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private static final String DEFAULT_JOKE_SUBJECT = "programming";
    
    private final ChatClient chatClient;

    /**
     * Constructs a ChatService with the provided ChatClient.
     * 
     * @param chatClient the ChatClient instance
     * @throws IllegalArgumentException if the chatClient is null
     */
    public ChatService(ChatClient chatClient) {
        if (chatClient == null) {
            throw new IllegalArgumentException("ChatClient cannot be null");
        }
        this.chatClient = chatClient;
        logger.info("ChatService initialized successfully");
    }

    /**
     * Generates a joke about the specified subject.
     * 
     * @param subject the topic for the joke (if null or empty, defaults to "programming")
     * @return the generated joke text
     * @throws ChatServiceException if there's an error generating the joke
     */
    public String generateJoke(String subject) {
        try {
            String jokeSubject = StringUtils.hasText(subject) ? subject.trim() : DEFAULT_JOKE_SUBJECT;
            logger.debug("Generating joke about: {}", jokeSubject);
            
            Prompt prompt = new Prompt("Tell a funny joke about " + jokeSubject + ".");
            ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
            
            if (response == null || response.getResult() == null || response.getResult().getOutput() == null) {
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

    /**
     * Generates a joke about programming (default topic).
     * 
     * @return the generated programming joke
     */
    public String generateProgrammingJoke() {
        return generateJoke(DEFAULT_JOKE_SUBJECT);
    }

    /**
     * Generates a custom response with a specific prompt.
     * 
     * @param customPrompt the custom prompt for the AI
     * @return the generated response text
     * @throws IllegalArgumentException if the prompt is null or empty
     * @throws ChatServiceException if there's an error generating the response
     */
    public String generateCustomResponse(String customPrompt) {
        if (!StringUtils.hasText(customPrompt)) {
            throw new IllegalArgumentException("Custom prompt cannot be null or empty");
        }
        
        try {
            logger.debug("Generating custom response for prompt: {}", customPrompt);
            
            Prompt prompt = new Prompt(customPrompt.trim());
            ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
            
            if (response == null || response.getResult() == null || response.getResult().getOutput() == null) {
                throw new ChatServiceException("Invalid response from AI service");
            }
            
            String result = response.getResult().getOutput().getText();
            logger.debug("Successfully generated custom response");
            
            return result;
            
        } catch (Exception e) {
            logger.error("Error generating custom response for prompt '{}': {}", customPrompt, e.getMessage(), e);
            throw new ChatServiceException("Failed to generate custom response: " + e.getMessage(), e);
        }
    }
}
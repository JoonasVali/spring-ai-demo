package com.github.joonasvali.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for AI chat services.
 * Provides centralized configuration for ChatClient and related beans.
 */
@Configuration
public class ChatConfiguration {

    /**
     * Creates and configures a ChatClient bean.
     * 
     * @param chatModel the chat model to use for AI interactions
     * @return configured ChatClient instance
     */
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .build();
    }
}

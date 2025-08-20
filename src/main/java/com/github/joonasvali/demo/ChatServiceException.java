package com.github.joonasvali.demo;

/**
 * Custom exception for ChatService operations.
 * Provides specific error handling for AI chat-related failures.
 */
public class ChatServiceException extends RuntimeException {

    /**
     * Constructs a ChatServiceException with the specified detail message.
     * 
     * @param message the detail message
     */
    public ChatServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a ChatServiceException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ChatServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

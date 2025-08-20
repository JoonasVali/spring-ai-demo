# Spring AI Demo - Refactored ChatService

This project demonstrates a refactored Spring AI ChatService that follows best practices for enterprise applications.

## Overview

The `ChatService` has been completely refactored to provide:

- **Better Error Handling**: Custom exceptions and proper error messages
- **Input Validation**: Parameter validation and null checks
- **Comprehensive Logging**: Debug and error logging throughout
- **Clean Architecture**: Separation of concerns and better method organization
- **Unit Testing**: Comprehensive test coverage
- **Documentation**: JavaDoc comments for all public methods

## Features

### ChatService Methods

1. **`generateJoke(String subject)`** - Generates a joke about a specific subject
   - If subject is null or empty, defaults to "programming"
   - Includes proper error handling and logging

2. **`generateProgrammingJoke()`** - Generates a programming joke (convenience method)

3. **`generateCustomResponse(String customPrompt)`** - Generates custom AI responses
   - Validates input parameters
   - Handles various error scenarios

### API Endpoints

- **`GET /`** - Health check endpoint
- **`GET /joke?subject={topic}`** - Generate joke about specific topic
- **`GET /joke/programming`** - Generate programming joke
- **`GET /chat?prompt={text}`** - Generate custom AI response

## Architecture Improvements

### 1. Exception Handling
- Custom `ChatServiceException` for AI-related errors
- Proper exception propagation and logging
- Input validation with `IllegalArgumentException`

### 2. Logging
- SLF4J logging framework
- Debug logging for successful operations
- Error logging with stack traces
- Informational logging for service initialization

### 3. Configuration
- Dedicated `ChatConfiguration` class
- Bean-based ChatClient configuration
- Centralized configuration management

### 4. Testing
- Comprehensive unit tests with Mockito
- Tests for all public methods
- Error scenario testing
- Mock-based testing for external dependencies

## Code Quality Improvements

- **Clean Code Principles**: Descriptive method names and clear intent
- **Single Responsibility**: Each method has a single, clear purpose
- **Dependency Injection**: Constructor-based injection instead of field injection
- **Immutability**: Final fields and proper encapsulation
- **Documentation**: Comprehensive JavaDoc for all public APIs

## Running the Application

1. Ensure you have Java 23+ and Maven installed
2. Configure your AI service credentials in `application.properties`
3. Run: `mvn spring-boot:run`
4. Access the API at `http://localhost:8080`

## Testing

Run the test suite with:
```bash
mvn test
```

## Dependencies

- Spring Boot 3.5.4
- Spring AI 1.0.1
- JUnit 5 with Mockito for testing
- SLF4J for logging

## Best Practices Implemented

1. **Constructor Injection**: Proper dependency injection
2. **Input Validation**: Null checks and parameter validation
3. **Error Handling**: Custom exceptions and proper error propagation
4. **Logging**: Comprehensive logging for debugging and monitoring
5. **Testing**: Unit tests with proper mocking
6. **Documentation**: JavaDoc for all public methods
7. **Configuration**: Centralized configuration management
8. **Clean Architecture**: Separation of concerns and single responsibility

## Future Enhancements

- Add rate limiting for API endpoints
- Implement caching for repeated requests
- Add metrics and monitoring
- Support for different AI models
- Async processing for long-running requests

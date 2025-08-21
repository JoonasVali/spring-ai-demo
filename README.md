# Spring AI Demo Project

This is a demonstration project showcasing Spring AI integration with OpenAI, featuring AI tool calling capabilities 
and structured output.

## Overview

This project demonstrates how to use Spring AI to:
- Integrate with OpenAI's language models
- Create custom AI tools using the `@Tool` annotation
- Enable AI models to call external tools during conversations
- Generate structured outputs

## Features

### AI Tool Integration
- **RandomNumberTool**: A custom tool that generates random numbers within specified limits
- Uses Spring AI's `@Tool` annotation for automatic tool discovery
- Demonstrates how AI models can call external functions

### OpenAI Integration
- Configured with Spring AI starter for OpenAI
- Supports chat completions with tool calling
- Automatic tool schema generation and validation

### Existing Functionality
- **ChatService**: Handles AI chat interactions
- **Joke Generation**: Generates jokes on various subjects
- **Health Check**: Basic service status endpoint

## Project Structure

```
src/main/java/com/github/joonasvali/demo/
├── DemoApplication.java          # Main Spring Boot application
├── MyController.java             # REST controller for API endpoints
├── ChatService.java              # Service for AI chat interactions
├── ChatServiceException.java     # Custom exception handling
└── RandomNumberTool.java         # AI tool for random number generation
```

## Configuration

### Prerequisites
- Java 23+
- Maven 3.6+
- OpenAI API key

### Environment Setup
Set your OpenAI API key as an environment variable:
```bash
export OPENAI_API_KEY=your_api_key_here
```

Or add it to `application.properties`:
```properties
spring.ai.openai.api-key=your_api_key_here
```

## Running the Application

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd spring-ai-demo
   ```

2. **Set your OpenAI API key**
   ```bash
   export OPENAI_API_KEY=your_api_key_here
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API**
   - Health check: `GET http://localhost:8080/`
   - Generate joke: `GET http://localhost:8080/joke?subject=programming`

## AI Tool Usage

The `RandomNumberTool` is automatically available to AI models through Spring AI's tool calling system. When an AI model needs to generate a random number, it can call this tool with:

- **Function**: `generateRandomNumber`
- **Parameters**: 
  - `min` (integer): Minimum value (inclusive)
  - `max` (integer): Maximum value (inclusive)
- **Returns**: Random integer between min and max

## Dependencies

- **Spring Boot**: 3.5.4
- **Spring AI**: 1.0.1
- **OpenAI Integration**: Spring AI starter for OpenAI models
- **Java Version**: 23

## Key Spring AI Features Demonstrated

1. **Tool Calling**: AI models can invoke custom tools during conversations
2. **Automatic Schema Generation**: Tool schemas are automatically generated from `@Tool` annotations
3. **Seamless Integration**: Tools are automatically discovered and registered
4. **Error Handling**: Proper exception handling and logging for tool operations

## Development

### Adding New Tools
To add new AI tools, simply:
1. Create a new class with `@Component` annotation
2. Add methods with `@Tool` annotation
3. Spring AI will automatically discover and register them

### Testing
Run the test suite with:
```bash
mvn test
```

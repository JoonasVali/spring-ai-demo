package com.github.joonasvali.demo;

import com.github.joonasvali.demo.model.Joke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
  private final VectorDatabaseService vectorDatabaseService;

  public DatabaseInitializer(VectorDatabaseService vectorDatabaseService) {
    this.vectorDatabaseService = vectorDatabaseService;
  }

  @Override
  public void run(String... args) throws Exception {
    try {
      // Check if database is already initialized
      if (vectorDatabaseService.isDatabaseInitialized()) {
        logger.info("Database already initialized, skipping initialization...");
        return;
      }

      logger.info("Initializing vector database with sample joke...");

      Joke sampleJoke = new Joke(
          "The Programmer's Life",
          "Why do programmers prefer dark mode? Because light attracts bugs!",
          "Programming Humor"
      );

      Joke sampleJoke2 = new Joke(
          "Debugging Fun",
          "Why do Java developers wear glasses? Because they don't see sharp.",
          "Programming Humor"
      );

      Joke sampleJoke3 = new Joke(
          "Tech Support",
          "How many programmers does it take to change a light bulb? None, that's a hardware problem.",
          "Programming Humor"
      );

      Joke sampleJoke4 = new Joke(
          "Code Review",
          "Why do programmers hate nature? It has too many bugs.",
          "Programming Humor"
      );


      vectorDatabaseService.addJoke(sampleJoke);
      vectorDatabaseService.addJoke(sampleJoke2);
      vectorDatabaseService.addJoke(sampleJoke3);
      vectorDatabaseService.addJoke(sampleJoke4);

    } catch (Exception e) {
      logger.error("❌ Failed to initialize vector database with sample joke: {}", e.getMessage(), e);
      // Don't throw the exception to prevent application startup failure
      // The application can still run without the initial joke
    }
  }


}

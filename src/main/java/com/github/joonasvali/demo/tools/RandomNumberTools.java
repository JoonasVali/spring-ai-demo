package com.github.joonasvali.demo.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomNumberTools {

  private static final Logger logger = LoggerFactory.getLogger(RandomNumberTools.class);
  private final Random random = new Random();

  @Tool(name = "generateRandomNumber", description = "Generates a random integer number within the specified range (inclusive)")
  public int generateRandomNumber(int min, int max) {
    logger.debug("Random number tool called with min: {} and max: {}", min, max);

    if (min >= max) {
      logger.warn("Invalid range provided: min ({}) >= max ({}), throwing exception", min, max);
      throw new IllegalArgumentException("Minimum value must be less than maximum value");
    }

    // Generate random number between min and max (inclusive)
    int result = random.nextInt(max - min + 1) + min;
    logger.info("Generated random number: {} (range: {} to {})", result, min, max);

    return result;
  }
}

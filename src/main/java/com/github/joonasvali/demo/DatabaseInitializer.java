package com.github.joonasvali.demo;

import com.github.joonasvali.demo.model.Joke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.milvus.MilvusSearchRequest;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private final MilvusVectorStore vectorStore;

    public DatabaseInitializer(MilvusVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // Check if database is already initialized
            if (isDatabaseAlreadyInitialized()) {
                logger.info("Database already initialized, skipping initialization...");
                return;
            }

            logger.info("Initializing vector database with sample joke...");
            
            // Create a sample joke document
            Joke sampleJoke = new Joke(
                "The Programmer's Life",
                "Why do programmers prefer dark mode? Because light attracts bugs!",
                "Programming Humor"
            );
            
            Document jokeDocument = new Document(
                sampleJoke.content(),
                Map.of(
                    "title", sampleJoke.title(),
                    "topic", sampleJoke.topic(),
                    "type", "joke",
                    "subject", "programming"
                )
            );
            
            // Add the joke to the vector database
            vectorStore.add(List.of(jokeDocument));
            
            logger.info("✅ Successfully initialized vector database with sample joke: '{}'", sampleJoke.title());
            
        } catch (Exception e) {
            logger.error("❌ Failed to initialize vector database with sample joke: {}", e.getMessage(), e);
            // Don't throw the exception to prevent application startup failure
            // The application can still run without the initial joke
        }
    }

    /**
     * Check if the database is already initialized by looking for existing documents
     * @return true if documents already exist, false otherwise
     */
    private boolean isDatabaseAlreadyInitialized() {
        try {
            // Search for any existing documents with a simple query
            MilvusSearchRequest searchRequest = MilvusSearchRequest.milvusBuilder()
                .query("programming")
                .topK(1)
                .build();
            
            var results = vectorStore.similaritySearch(searchRequest);
            
            // If we find any results, the database is already initialized
            return !results.isEmpty();
        } catch (Exception e) {
            logger.warn("Could not check if database is already initialized: {}", e.getMessage());
            // If we can't check, assume it's not initialized and proceed
            return false;
        }
    }
}

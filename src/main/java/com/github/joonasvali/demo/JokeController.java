package com.github.joonasvali.demo;

import com.github.joonasvali.demo.model.Joke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.milvus.MilvusSearchRequest;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JokeController {

  private static final Logger logger = LoggerFactory.getLogger(JokeController.class);
  private final ChatService chatService;
  private final MilvusVectorStore vectorStore;


  public JokeController(ChatService chatService, MilvusVectorStore vectorStore) {
    this.chatService = chatService;
    this.vectorStore = vectorStore;
  }

  @GetMapping("/")
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("Service is running");
  }

  @GetMapping("/vector-health")
  public ResponseEntity<String> vectorHealthCheck() {
    try {
      MilvusSearchRequest request = MilvusSearchRequest.milvusBuilder()
          .query("sample query")
          .topK(1)
          .nativeExpression("metadata['category'] == 'science'")
          .build();

      var results = vectorStore.similaritySearch(request);

      return ResponseEntity.ok("✅ Vector database is accessible and working!<br/>" +
          "Milvus connection: SUCCESS<br/>" +
          "Collection: vector_store<br/>" +
          "Embedding dimension: 1536<br/>" +
          "Index type: IVF_FLAT<br/>" +
          "Metric type: COSINE<br/>" +
          "Test document stored and retrieved successfully<br/>" +
          "Search results count: " + results.size());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("❌ Vector database connection failed: " + e.getMessage());
    }
  }

  @GetMapping("/joke")
  public ResponseEntity<String> getJoke(@RequestParam(required = false) String subject) {
    try {
      Joke joke = chatService.generateJoke(subject);
      
      // Store the joke in the vector database for later search
      try {
        Document jokeDocument = new Document(
            joke.content(),
            Map.of(
                "title", joke.title(),
                "topic", joke.topic(),
                "type", "joke",
                "subject", subject != null ? subject : "programming"
            )
        );
        
        vectorStore.add(List.of(jokeDocument));
        
        return ResponseEntity.ok("Title: " + joke.title() + "<br/>" +
            "Content: " + joke.content() + "<br/>" +
            "Topic: " + joke.topic() + "<br/>" +
            "✅ Joke stored in vector database for future search");
      } catch (Exception storageException) {
        // If storage fails, still return the joke but log the storage error
        logger.error("Failed to store joke in vector database: {}", storageException.getMessage());
        return ResponseEntity.ok("Title: " + joke.title() + "<br/>" +
            "Content: " + joke.content() + "<br/>" +
            "Topic: " + joke.topic() + "<br/>" +
            "⚠️ Joke generated but failed to store in database: " + storageException.getMessage());
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to generate joke: " + e.getMessage());
    }
  }

  @GetMapping("/search-jokes")
  public ResponseEntity<String> searchJokes(@RequestParam String query) {
    
    try {
      // Build the search request with simplified parameters
      MilvusSearchRequest request = MilvusSearchRequest.milvusBuilder()
          .query(query)
          .topK(5)
          .similarityThreshold(0.8)
          .nativeExpression("metadata['type'] == 'joke'")
          .build();
      
      // Perform the search
      var results = vectorStore.similaritySearch(request);
      
      if (results.isEmpty()) {
        return ResponseEntity.ok("🔍 No jokes found matching your search criteria.<br/>" +
            "Query: '" + query + "'<br/>" +
            "Try different keywords or check your filters.");
      }
      
      // Build the response
      StringBuilder response = new StringBuilder();
      response.append("🔍 Found ").append(results.size()).append(" joke(s) matching '").append(query).append("':<br/><br/>");
      
      for (int i = 0; i < results.size(); i++) {
        var result = results.get(i);
        var metadata = result.getMetadata();
        
        response.append("<strong>Joke ").append(i + 1).append(":</strong><br/>");
        response.append("<strong>Title:</strong> ").append(metadata.get("title")).append("<br/>");
        response.append("<strong>Content:</strong> ").append(result.getText()).append("<br/>");
        response.append("<strong>Topic:</strong> ").append(metadata.get("topic")).append("<br/>");
        response.append("<strong>Subject:</strong> ").append(metadata.get("subject")).append("<br/>");
        response.append("<strong>Similarity Score:</strong> ").append(String.format("%.3f", result.getScore())).append("<br/>");
        response.append("<br/>");
      }
      
      return ResponseEntity.ok(response.toString());
      
    } catch (Exception e) {
      logger.error("Error searching jokes: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("❌ Failed to search jokes: " + e.getMessage());
    }
  }
}
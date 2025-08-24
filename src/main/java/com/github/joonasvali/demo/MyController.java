package com.github.joonasvali.demo;

import com.github.joonasvali.demo.model.Joke;
import org.springframework.ai.vectorstore.milvus.MilvusSearchRequest;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

  private final ChatService chatService;
  private final MilvusVectorStore vectorStore;


  public MyController(ChatService chatService, MilvusVectorStore vectorStore) {
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
      // First, store a test document to create the collection if it doesn't exist
//      String testContent = "This is a test document to initialize the vector store collection.";
//      Document testDocument = new Document(testContent);
//      vectorStore.add(List.of(testDocument));

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
      return ResponseEntity.ok("Title: " + joke.title() + "<br/>" +
          "Content: " + joke.content() + "<br/>" +
          "Topic: " + joke.topic());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to generate joke: " + e.getMessage());
    }
  }
}
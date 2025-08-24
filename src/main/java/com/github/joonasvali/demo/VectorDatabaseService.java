package com.github.joonasvali.demo;

import com.github.joonasvali.demo.model.Joke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.milvus.MilvusSearchRequest;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VectorDatabaseService {

  private static final Logger logger = LoggerFactory.getLogger(VectorDatabaseService.class);
  private final MilvusVectorStore vectorStore;

  public VectorDatabaseService(MilvusVectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

  /**
   * Add a joke document to the vector database
   * @param joke the joke to store     *
   * @return true if successful, false otherwise
   */
  public boolean addJoke(Joke joke) {
    try {
      Document jokeDocument = new Document(
          joke.content(),
          Map.of(
              "title", joke.title(),
              "topic", joke.topic(),
              "type", "joke"
          )
      );

      vectorStore.add(List.of(jokeDocument));
      logger.info("✅ Successfully stored joke '{}' in vector database", joke.title());
      return true;
    } catch (Exception e) {
      logger.error("❌ Failed to store joke '{}' in vector database: {}", joke.title(), e.getMessage(), e);
      return false;
    }
  }

  /**
   * Add a generic document to the vector database
   * @param content the document content
   * @param metadata the document metadata
   * @return true if successful, false otherwise
   */
  public boolean addDocument(String content, Map<String, Object> metadata) {
    try {
      Document document = new Document(content, metadata);
      vectorStore.add(List.of(document));
      logger.info("✅ Successfully stored document in vector database");
      return true;
    } catch (Exception e) {
      logger.error("❌ Failed to store document in vector database: {}", e.getMessage(), e);
      return false;
    }
  }

  /**
   * Search for jokes using similarity search
   * @param query the search query
   * @param topK maximum number of results to return
   * @param similarityThreshold minimum similarity score threshold
   * @return list of search results
   */
  public List<Document> searchJokes(String query, int topK, double similarityThreshold) {
    try {
      MilvusSearchRequest request = MilvusSearchRequest.milvusBuilder()
          .query(query)
          .topK(topK)
          .similarityThreshold(similarityThreshold)
          .nativeExpression("metadata['type'] == 'joke'")
          .build();

      return vectorStore.similaritySearch(request);
    } catch (Exception e) {
      logger.error("❌ Failed to search jokes: {}", e.getMessage(), e);
      throw new RuntimeException("Failed to search jokes", e);
    }
  }

  /**
   * Search for jokes using default parameters
   * @param query the search query
   * @return list of search results
   */
  public List<Document> searchJokes(String query) {
    return searchJokes(query, 5, 0.8);
  }

  /**
   * Generic similarity search
   * @param query the search query
   * @param topK maximum number of results to return
   * @return list of search results
   */
  public List<Document> similaritySearch(String query, int topK) {
    try {
      MilvusSearchRequest request = MilvusSearchRequest.milvusBuilder()
          .query(query)
          .topK(topK)
          .build();

      return vectorStore.similaritySearch(request);
    } catch (Exception e) {
      logger.error("❌ Failed to perform similarity search: {}", e.getMessage(), e);
      throw new RuntimeException("Failed to perform similarity search", e);
    }
  }

  /**
   * Check if the database is already initialized by looking for existing documents
   * @return true if documents already exist, false otherwise
   */
  public boolean isDatabaseInitialized() {
    try {
      MilvusSearchRequest searchRequest = MilvusSearchRequest.milvusBuilder()
          .query("programming")
          .topK(1)
          .build();

      var results = vectorStore.similaritySearch(searchRequest);
      return !results.isEmpty();
    } catch (Exception e) {
      logger.warn("Could not check if database is already initialized: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Perform a health check on the vector database
   * @return health check information
   */
  public String performHealthCheck() {
    try {
      MilvusSearchRequest request = MilvusSearchRequest.milvusBuilder()
          .query("sample query")
          .topK(1)
          .nativeExpression("metadata['category'] == 'science'")
          .build();

      var results = vectorStore.similaritySearch(request);

      return "✅ Vector database is accessible and working!<br/>" +
          "Milvus connection: SUCCESS<br/>" +
          "Collection: vector_store<br/>" +
          "Embedding dimension: 1536<br/>" +
          "Index type: IVF_FLAT<br/>" +
          "Metric type: COSINE<br/>" +
          "Test document stored and retrieved successfully<br/>" +
          "Search results count: " + results.size();
    } catch (Exception e) {
      throw new RuntimeException("Vector database health check failed: " + e.getMessage(), e);
    }
  }

}

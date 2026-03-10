package com.sa.orm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.orm.bean.SearchRequest;

public class SearchRequestFactory {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Parses a JSON string into a SearchRequest object.
   * Handles nested groups and criteria automatically.
   *
   * @param jsonString The raw JSON string from the client.
   * @return A populated SearchRequest object, or null if parsing fails.
   */
  public static SearchRequest createFromJson(String jsonString) throws JsonProcessingException, JsonMappingException {
    if (jsonString == null || jsonString.trim().isEmpty()) {
      return new SearchRequest(); // Return default object
    }

    // Jackson automatically handles the recursion in FilterGroup
    return objectMapper.readValue(jsonString, SearchRequest.class);
  }
}
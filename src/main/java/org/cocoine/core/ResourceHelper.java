package org.cocoine.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class ResourceHelper {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private ResourceHelper() {
  }

  public static JsonNode readJson(String resourcePath) {
    try (InputStream stream = open(resourcePath)) {
      return MAPPER.readTree(stream);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read JSON resource: " + resourcePath, e);
    }
  }

  public static Map<String, String> readStringMap(String resourcePath) {
    try (InputStream stream = open(resourcePath)) {
      return MAPPER.readValue(stream, MAPPER.getTypeFactory()
          .constructMapType(HashMap.class, String.class, String.class));
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read JSON map resource: " + resourcePath, e);
    }
  }

  private static InputStream open(String resourcePath) {
    InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
    if (stream == null) {
      throw new IllegalArgumentException("Resource not found: " + resourcePath);
    }
    return stream;
  }
}

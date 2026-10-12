package org.cocoine.framework.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public final class JsonLoader {
  public static final ObjectMapper MAPPER = new ObjectMapper();

  private JsonLoader() {
  }

  public static JsonNode readTree(String classpathLocation) {
    try (InputStream stream = resource(classpathLocation)) {
      return MAPPER.readTree(stream);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read JSON resource: " + classpathLocation, e);
    }
  }

  public static Map<String, Object> readMap(String classpathLocation) {
    try (InputStream stream = resource(classpathLocation)) {
      return MAPPER.readValue(stream, new TypeReference<Map<String, Object>>() {
      });
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read JSON resource: " + classpathLocation, e);
    }
  }

  public static InputStream resource(String classpathLocation) {
    InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathLocation);
    if (stream == null) {
      throw new IllegalArgumentException("Classpath resource not found: " + classpathLocation);
    }
    return stream;
  }
}

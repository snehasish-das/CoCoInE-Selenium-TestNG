package org.cocoine.framework.data;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.cocoine.framework.util.JsonLoader;
import org.cocoine.framework.util.RandomData;
import org.testng.annotations.DataProvider;

public final class JsonDataProvider {
  private JsonDataProvider() {
  }

  @DataProvider(name = "sampleApiData", parallel = true)
  public static Iterator<Object[]> sampleApiData() {
    return iterator("testData/sample-api-data.json");
  }

  @DataProvider(name = "sampleUiData", parallel = true)
  public static Iterator<Object[]> sampleUiData() {
    return iterator("testData/sample-ui-data.json");
  }

  public static Iterator<Object[]> iterator(String resource) {
    JsonNode root = JsonLoader.readTree(resource);
    JsonNode records = root.has("records") ? root.path("records") : root;
    return objectIterator(records);
  }

  private static Iterator<Object[]> objectIterator(JsonNode records) {
    return new Iterator<>() {
      private final Iterator<JsonNode> source = records.elements();

      @Override
      public boolean hasNext() {
        return source.hasNext();
      }

      @Override
      public Object[] next() {
        return new Object[] {replaceDynamicValues(JsonLoader.MAPPER.convertValue(source.next(), Map.class))};
      }
    };
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Object> replaceDynamicValues(Map<String, Object> source) {
    Map<String, Object> result = new LinkedHashMap<>();
    source.forEach((key, value) -> {
      if (value instanceof String) {
        result.put(key, replaceToken((String) value));
      } else if (value instanceof Map) {
        result.put(key, replaceDynamicValues((Map<String, Object>) value));
      } else {
        result.put(key, value);
      }
    });
    return result;
  }

  private static String replaceToken(String value) {
    return value
        .replace("{{RANDOM_STRING}}", RandomData.alphaNumeric(8))
        .replace("{{CURRDATE}}", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
  }
}

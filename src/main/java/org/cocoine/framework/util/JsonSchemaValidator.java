package org.cocoine.framework.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.util.Set;

public final class JsonSchemaValidator {
  private JsonSchemaValidator() {
  }

  public static void validateResource(String jsonResource, String schemaResource) {
    JsonNode json = JsonLoader.readTree(jsonResource);
    JsonNode schema = JsonLoader.readTree(schemaResource);
    Set<ValidationMessage> errors = JsonSchemaFactory
        .getInstance(SpecVersion.VersionFlag.V202012)
        .getSchema(schema)
        .validate(json);
    if (!errors.isEmpty()) {
      throw new IllegalArgumentException("JSON schema validation failed: " + errors);
    }
  }
}

package org.cocoine.framework.logging;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FrameworkLogger {
  private static final Logger LOGGER = LoggerFactory.getLogger("cocoine-framework");

  private FrameworkLogger() {
  }

  public static void info(String message) {
    LOGGER.info("[{}][thread:{}] {}", Instant.now(), Thread.currentThread().getId(), maskSecrets(message));
  }

  public static String maskSecrets(String value) {
    if (value == null) {
      return "";
    }
    return value.replaceAll("(?i)(authorization|token|api[-_]?key|password)\\s*[:=]\\s*[^,\\s}]+", "$1=***");
  }
}

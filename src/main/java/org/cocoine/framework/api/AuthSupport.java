package org.cocoine.framework.api;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import org.cocoine.framework.config.FrameworkConfig;
import org.cocoine.framework.util.JsonLoader;

public final class AuthSupport {
  private AuthSupport() {
  }

  public static String apiKey(String profile) {
    String envName = profile.toUpperCase().replace('-', '_');
    return System.getenv().getOrDefault("API_KEY_" + envName, FrameworkConfig.user(profile, "apiKey"));
  }

  public static String gorestToken() {
    return System.getenv("GOREST_TOKEN");
  }

  public static String unsignedJwt(Map<String, Object> claims) {
    String header = base64Url(Map.of("alg", "none", "typ", "JWT"));
    String payload = base64Url(claims);
    return header + "." + payload + ".";
  }

  public static String shortLivedUnsignedJwt(String subject) {
    return unsignedJwt(Map.of("sub", subject, "iat", Instant.now().getEpochSecond()));
  }

  private static String base64Url(Object value) {
    try {
      byte[] json = JsonLoader.MAPPER.writeValueAsBytes(value);
      return Base64.getUrlEncoder().withoutPadding().encodeToString(json);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to create JWT segment", e);
    }
  }

  public static String basicToken(String username, String password) {
    return Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
  }
}

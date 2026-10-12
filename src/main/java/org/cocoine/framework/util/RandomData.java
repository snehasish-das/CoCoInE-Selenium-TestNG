package org.cocoine.framework.util;

import java.security.SecureRandom;

public final class RandomData {
  private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final SecureRandom RANDOM = new SecureRandom();

  private RandomData() {
  }

  public static String alphaNumeric(int length) {
    StringBuilder builder = new StringBuilder(length);
    for (int index = 0; index < length; index++) {
      builder.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
    }
    return builder.toString();
  }
}

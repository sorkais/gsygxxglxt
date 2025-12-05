package com.company.hrms.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {
  public static String generateSaltBase64(int length) {
    byte[] salt = new byte[length];
    new SecureRandom().nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  public static String hashPasswordBase64(String password, String saltBase64) {
    try {
      byte[] salt = Base64.getDecoder().decode(saltBase64);
      PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      byte[] hash = skf.generateSecret(spec).getEncoded();
      return Base64.getEncoder().encodeToString(hash);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean verify(String password, String saltBase64, String expectedHashBase64) {
    String h = hashPasswordBase64(password, saltBase64);
    return constantTimeEquals(h, expectedHashBase64);
  }

  private static boolean constantTimeEquals(String a, String b) {
    if (a.length() != b.length()) return false;
    int r = 0;
    for (int i = 0; i < a.length(); i++) r |= a.charAt(i) ^ b.charAt(i);
    return r == 0;
  }
}


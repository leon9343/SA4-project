package ch.usi.inf.bsc.sa4.lab02spring.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Utility class for password hashing.
 */
public final class PasswordHashingUtils {
  /**
   * The password encoder.
   */
  private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  // Make default constructor private to prevent initialization without the use of singleton getInstance()
  private PasswordHashingUtils() {
  }

  /**
   * Returns the hashed version of a password.
   *
   * @param password a string representing the password.
   * @return a non-null string representing the password hash.
   * @throws IllegalArgumentException if the password is null.
   * @spec.modifies nothing.
   */
  public static String hashPassword(String password) {
    if (password == null) {
      throw new IllegalArgumentException("password cannot be null");
    }
    return passwordEncoder.encode(password);
  }

  /**
   * Checks if a password matches a hash.
   *
   * @param password a password
   * @param hash     a hash
   * @return <code>true</code> iff the password matches the hash.
   * @throws IllegalArgumentException if the password or the hash are null.
   */
  public static boolean passwordMatches(String password, String hash) {
    if (password == null) {
      throw new IllegalArgumentException("password cannot be null");
    }
    if (hash == null) {
      throw new IllegalArgumentException("hash cannot be null");
    }
    return passwordEncoder.matches(password, hash);
  }

}

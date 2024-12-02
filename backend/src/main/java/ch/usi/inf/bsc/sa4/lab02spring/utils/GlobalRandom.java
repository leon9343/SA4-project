package ch.usi.inf.bsc.sa4.lab02spring.utils;

import java.security.SecureRandom;

/**
 * Class for the GlobalRandom
 */
public final class GlobalRandom {
  /**
   * The random
   */
  private static final SecureRandom random = new SecureRandom();

  private GlobalRandom() {
    throw new UnsupportedOperationException("This class should not be instantiated");
  }

  /**
   * Get the random
   *
   * @return the random
   */
  public static SecureRandom getRandom() {
    return random;
  }
}

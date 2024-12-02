package ch.usi.inf.bsc.sa4.lab02spring.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTests {


  @Test
  @DisplayName("Constructor throws IllegalArgumentException for null username")
  void testConstructorThrowsForNullUsername() {
    assertThrows(IllegalArgumentException.class, () -> new User("1111", null, "Britney Spears", "britney.spears@gmail.com"),
            "Expected IllegalArgumentException for null username");
  }

  @Test
  @DisplayName("Constructor throws IllegalArgumentException for invalid email format")
  void testConstructorThrowsForInvalidEmail() {
    assertThrows(IllegalArgumentException.class, () -> new User("1111", "britney", "Britney Spears", "britney.spears.gmail.com"), "Expected IllegalArgumentException for invalid email format");
  }

  @Test
  @DisplayName("Constructor throws IllegalArgumentException for username length")
  void testConstructorThrowsForUsernameLength() {
    assertThrows(IllegalArgumentException.class, () -> new User("1111", "bb", "Britney Spears", "britney.spears@gmail.com"), "Expected IllegalArgumentException for short username");
  }

  @Test
  @DisplayName("setUsername throws IllegalArgumentException for invalid length")
  void testSetUsernameInvalidLength() {
    User user = new User("1111", "britney", "Britney Spears", "britney.spears@gmail.com");
    assertThrows(IllegalArgumentException.class, () -> {
      user.setUsername("bb");
    }, "Expected IllegalArgumentException for username with too few characters");
  }

  @Test
  @DisplayName("setName throws IllegalArgumentException for null name")
  void testSetNameNull() {
    User user = new User("1111", "britney", "Britney Spears", "britney.spears@gmail.com");
    assertThrows(IllegalArgumentException.class, () -> {
      user.setName(null);
    }, "Expected IllegalArgumentException for null name");
  }

  @Test
  @DisplayName("setEmail throws IllegalArgumentException for invalid email")
  void testSetEmailInvalid() {
    User user = new User("1111", "britney", "Britney Spears", "britney.spears@gmail.com");
    assertThrows(IllegalArgumentException.class, () -> {
      user.setEmail("britney.spears@");
    }, "Expected IllegalArgumentException for invalid email");
  }
}


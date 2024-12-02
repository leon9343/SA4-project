package ch.usi.inf.bsc.sa4.lab02spring.service;

import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DisplayName("UserService Tests")
public class UserServiceTests {
  @Autowired
  UserService userService;

  @DisplayName("after creating a new userId")
  @Nested
  class WhenCreatingANewUser {

    User user;

    @BeforeEach
    void setup() {
      user = new User("1234", "Brad", "Pitt", "brad.pitt@usi.ch");
      userService.storeUser(user);
    }

    @DisplayName("The new userId should not be null")
    @Test
    void testNotNull() {
      assertNotNull(this.user);
    }

    @DisplayName("the new userId should have the correct name")
    @Test
    void testCreateUser() {
      assertEquals("Pitt", user.getName());
    }

    @Test
    void testUserContained() {
      List<User> allUsers = userService.getAllUsers();
      var userExists = allUsers.stream().anyMatch(user ->
              user.getId().equals(user.getId()) // Compare IDs instead of comparing objects
      );
      assertTrue(userExists, "The new userId should be in the list of all users");
    }
  }
}


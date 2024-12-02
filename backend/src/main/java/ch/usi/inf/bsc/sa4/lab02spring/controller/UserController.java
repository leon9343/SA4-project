package ch.usi.inf.bsc.sa4.lab02spring.controller;

import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.UserDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import ch.usi.inf.bsc.sa4.lab02spring.service.UserService;
import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
  private static final String EMAIL = "email";
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Retrieves the list of all existing users.
   *
   * @return ResponseEntity containing a list of DTOs representing the existing users.
   */
  @GetMapping
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    List<UserDTO> listUserDTO = userService.getAllUsers()
            .stream()
            .map(UserDTO::new)
            .toList();
    return ResponseEntity.ok(listUserDTO);
  }

  /**
   * Retrieves the user with the specified userId.
   *
   * @param userId The ID of the user to retrieve.
   * @return ResponseEntity containing the DTO representing the user if found,
   * otherwise returns HTTP status 404 NOT FOUND.
   */
  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> getUser(@PathVariable String userId) {
    User user = userService.getUser(userId);
    UserDTO userDTO = new UserDTO(user);
    return ResponseEntity.ok(userDTO);
  }

  @GetMapping(path = "/login")
  public ResponseEntity<UserDTO> createOrRetrieveUserFromOAuth(OAuth2AuthenticationToken authentication) {
    if (authentication == null) {
      // If authentication token is null, return forbidden status
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    String userId = (String) authentication.getPrincipal().getAttributes().get("sub");
    try {
      // If user already exists, return user information
      User user = userService.getUser(userId);
      final var userDTO = new UserDTO(user);
      return ResponseEntity.ok(userDTO);
    } catch (UserNotFoundException e) {
      // If user doesn't exist, create a new user
      final var createdUser = new User(
              userId,
              (String) authentication.getPrincipal().getAttributes().get("preferred_username"),
              (String) authentication.getPrincipal().getAttributes().get("name"),
              (String) authentication.getPrincipal().getAttributes().get(EMAIL)
      );
      userService.storeUser(createdUser);
      final var createdUserDTO = new UserDTO(createdUser);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
    }
  }
}

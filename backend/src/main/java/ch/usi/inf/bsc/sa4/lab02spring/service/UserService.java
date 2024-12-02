package ch.usi.inf.bsc.sa4.lab02spring.service;

import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import ch.usi.inf.bsc.sa4.lab02spring.repository.UserRepository;
import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.UnauthorizedUserException;
import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service class to support userId management.
 */
@Service
public class UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Retrieves all existing users.
   *
   * @return A list containing all existing users.
   */
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Persists changes made to a user identified by its ID.
   *
   * @param user The user to be updated.
   * @return The updated user.
   * @spec.modifies the persisted userId.
   */
  public User storeUser(User user) {
    return userRepository.save(user);
  }

  /**
   * Retrieves a user by its ID.
   *
   * @param userId The ID of the user to retrieve.
   * @return The user with the given ID.
   * @throws UserNotFoundException If no user is found with the given ID.
   * @spec.requires userId != null
   */
  public User getUser(String userId) throws UserNotFoundException {
    validateUser(userId);
    return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
  }

  /**
   * Retrieves the currently logged-in user.
   *
   * @return an optional containing the logged-in user if there is one, otherwise an empty optional.
   */
  public Optional<User> getLoggedInUser() {

    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }

    if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
      String userId = (String) oauthToken.getPrincipal().getAttributes().get("sub");
      if (userId != null) {
        return userRepository.findById(userId);
      }
    }

    return Optional.empty();
  }

  /**
   * Validates if the provided user ID corresponds to the logged-in user.
   *
   * @param userId The ID of the user to validate.
   * @throws UserNotFoundException     If the user is not logged in.
   * @throws UnauthorizedUserException If the provided user ID does not match the logged-in user.
   */
  public void validateUser(String userId) throws UserNotFoundException, UnauthorizedUserException {
    User loggedInUser = getLoggedInUser()
            .orElseThrow(() -> new UserNotFoundException("User not logged-in"));
    if (!userId.equals(loggedInUser.getId())) {
      throw new UnauthorizedUserException("Provided user ID does not match the logged-in user");
    }
  }

}

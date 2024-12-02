package ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user is not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

  /**
   * Constructor for the UserNotFoundException
   *
   * @param message the error message
   */
  public UserNotFoundException(String message) {
    super(message);
  }

}

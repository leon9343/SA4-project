package ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user is not authorized.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UnauthorizedUserException extends RuntimeException {

  /**
   * Constructor for the UnauthorizedUserException
   *
   * @param message the error message
   */
  public UnauthorizedUserException(String message) {
    super(message);
  }

}

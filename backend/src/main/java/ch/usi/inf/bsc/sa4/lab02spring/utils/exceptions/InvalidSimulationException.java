package ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a simulation is invalid.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidSimulationException extends RuntimeException {

  /**
   * Constructor for the InvalidSimulationException
   *
   * @param message the error message
   */
  public InvalidSimulationException(IllegalArgumentException message) {
    super(message);
  }

}

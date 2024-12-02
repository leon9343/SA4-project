package ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a simulation is not found.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class SimulationNotFoundException extends RuntimeException {

  /**
   * Constructor for the SimulationNotFoundException
   *
   * @param message the error message
   */
  public SimulationNotFoundException(String message) {
    super(message);
  }

}

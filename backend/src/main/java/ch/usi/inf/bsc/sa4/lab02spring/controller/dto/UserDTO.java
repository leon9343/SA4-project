package ch.usi.inf.bsc.sa4.lab02spring.controller.dto;

import ch.usi.inf.bsc.sa4.lab02spring.model.User;

/**
 * An output DTO representing a userId's public information.
 *
 * @param id       the userId's id.
 * @param username the userId's username.
 * @param name     the userId's name.
 * @param email    the userId's email.
 */
public record UserDTO(String id, String username, String name, String email) {
  public UserDTO(User user) {
    this(user.getId(), user.getUsername(), user.getName(), user.getEmail());
  }
}

package ch.usi.inf.bsc.sa4.lab02spring.controller.dto;

/**
 * An input DTO used to create a new userId.
 *
 * @param id       the userId's id
 * @param username the userId's username
 * @param name     the userId's name
 * @param email    the userId's email
 */
public record CreateUserDTO(String id, String username, String name, String email) {

}

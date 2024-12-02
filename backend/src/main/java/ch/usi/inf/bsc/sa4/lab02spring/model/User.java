package ch.usi.inf.bsc.sa4.lab02spring.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.regex.Pattern;

/**
 * Represents a user in the system
 */
@Document(collection = "users")
public class User {
  /**
   * Regular expression for email validation
   */
  public static final String EMAIL_REGEX = "^[\\p{L}\\p{N}._%+-]+@[\\p{L}\\p{N}.-]+\\.\\p{L}{2,}$";
  /**
   * Pattern for email validation
   */
  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
  /**
   * Minimum length of the name
   */
  private static final int MIN_NAME_LENGTH = 3;
  /**
   * Maximum length of the name
   */
  private static final int MAX_NAME_LENGTH = 50;
  /**
   * The ID of the user
   */
  @Id
  private final String id;
  /**
   * The username of the user
   */
  @NotNull(message = "username is null")
  @Size(min = 3, max = 50, message = "must be between 3 and 50 characters")
  private String username;
  /**
   * The name of the user
   */
  @NotNull(message = "name is null")
  @Size(min = 3, max = 50, message = "must be between 3 and 50 characters")
  private String name;
  /**
   * The email of the user
   */
  @NotNull(message = "email is null")
  @Email(message = "email is not valid", regexp = EMAIL_REGEX)
  private String email;

  /**
   * Constructor for User
   *
   * @param id       the id of the user for persistence
   * @param username the username of the user
   * @param name     the name of the user
   * @param email    the email of the user
   * @throws IllegalArgumentException if any of the parameters is invalid
   */
  @PersistenceCreator
  public User(String id,
              String username,
              String name,
              String email) throws IllegalArgumentException {
    if (username == null) {
      throw new IllegalArgumentException("username is null");
    }
    if (username.length() < MIN_NAME_LENGTH || username.length() > MAX_NAME_LENGTH) {
      throw new IllegalArgumentException("username must be between 3 and 50 characters");
    }
    if (name == null) {
      throw new IllegalArgumentException("Name is null");
    }
    if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
      throw new IllegalArgumentException("name must be between 3 and 50 characters");
    }
    if (email == null) {
      throw new IllegalArgumentException("email is null");
    }
    final var matcher = EMAIL_PATTERN.matcher(email);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("email is not valid");
    }
    this.id = id;
    this.username = username;
    this.name = name;
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  /**
   * Sets the username of the user
   *
   * @param username the new username of the user
   * @throws IllegalArgumentException if the username is null or is not between 3 and 50 character long
   */
  public void setUsername(String username) throws IllegalArgumentException {
    if (username == null) {
      throw new IllegalArgumentException("username is NULL");
    }
    if (username.length() < MIN_NAME_LENGTH || username.length() > MAX_NAME_LENGTH) {
      throw new IllegalArgumentException("username must be of 3 - 50 characters");
    }
    this.username = username;
  }

  public String getName() {
    return name;
  }

  /**
   * Sets the name of the user
   *
   * @param name the name of the user
   * @throws IllegalArgumentException if the name is null or is not between 3 and 50 character long
   */
  public void setName(String name) throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("name is NULL");
    }
    if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
      throw new IllegalArgumentException("name must be of 3 - 50 characters");
    }
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  /**
   * Sets the email of the user
   *
   * @param email the email of the user
   * @throws IllegalArgumentException if the email is null or is invalid
   */
  public void setEmail(String email) throws IllegalArgumentException {
    if (email == null) {
      throw new IllegalArgumentException("email is NULL");
    }
    final var matcher = EMAIL_PATTERN.matcher(email);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("email is not valid");
    }
    this.email = email;
  }

}

package ch.usi.inf.bsc.sa4.lab02spring.repository;

import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for users.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
  // You can implement complex "predefined" logic with specific conventions by
  // specific method names
  // Documentation link:
  // https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongodb.repositories.queries

  /**
   * Find a user by a string contained in its name.
   *
   * @param name the string contained in the name
   * @return the user
   */
  List<User> findUserByNameContaining(String name);

  /**
   * Find a user by its name.
   *
   * @param name the name
   * @return the user
   */
  Optional<User> findUserByName(String name);

  /**
   * Find a user by its id.
   *
   * @param id the id
   * @return the user
   */
  Optional<User> findUserById(String id);


}

package ch.usi.inf.bsc.sa4.lab02spring.repository;

import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for simulations.
 */
@Repository
public interface SimulationRepository extends MongoRepository<Simulation, String> {

  /**
   * Find a simulation by its id.
   *
   * @param id the id
   * @return the simulation
   */
  Optional<Simulation> findSimulationById(String id);

  /**
   * Find a simulation by its name.
   *
   * @param name the name
   * @return the simulation
   */
  List<Simulation> findSimulationByNameContaining(String name);

  /**
   * Find a simulation by the user id.
   *
   * @param userId the user id
   * @return the simulation
   */
  List<Simulation> findSimulationByUserId(String userId);

  /**
   * Count the number of simulations by the user id.
   *
   * @param userId the user id
   * @return the number of simulations
   */
  Optional<Long> countSimulationByUserId(String userId);
}

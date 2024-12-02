package ch.usi.inf.bsc.sa4.lab02spring.service;

import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.CreateSimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
import ch.usi.inf.bsc.sa4.lab02spring.repository.SimulationRepository;
import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.InvalidSimulationException;
import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.SimulationNotFoundException;
import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.UnauthorizedUserException;
import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Service class for simulations.
 */
@Service
@Validated
public class SimulationService {
  /**
   * The error message for a simulation not found.
   */
  private static final String SIMULATION_NOT_FOUND = "Simulation not found";

  /**
   * The simulation repository
   */
  private final SimulationRepository simulationRepository;
  /**
   * The user service
   */
  private final UserService userService;

  /**
   * Constructor for the SimulationService
   */
  @Autowired
  public SimulationService(SimulationRepository simulationRepository, UserService userService) {
    this.simulationRepository = simulationRepository;
    this.userService = userService;
  }

  /**
   * Retrieves a simulation by its ID.
   *
   * @param simulationId The ID of the simulation to retrieve.
   * @return The simulation with the specified ID.
   * @throws SimulationNotFoundException If the simulation is not found
   * @throws UserNotFoundException       If the user is not logged in.
   * @throws UnauthorizedUserException   If the user is not authorized to perform this operation.
   */
  public Simulation getSimulation(String simulationId)
          throws
          SimulationNotFoundException,
          UserNotFoundException,
          UnauthorizedUserException {
    final var simulation = simulationRepository.findSimulationById(simulationId)
            .orElseThrow(() -> new SimulationNotFoundException(SIMULATION_NOT_FOUND));
    validateUserOwnership(simulation.getUserId());
    return simulation;
  }

  /**
   * Retrieves a list of all simulations available in the system.
   *
   * @return A list containing all simulations currently stored in the system.
   */
  public List<Simulation> getSimulations() {
    return simulationRepository.findAll();
  }

  /**
   * Retrieves simulations associated with the provided user ID.
   *
   * @param userId The ID of the user for whom simulations are to be retrieved.
   * @return A list containing simulations associated with the specified user.
   * @throws UserNotFoundException     If the logged-in user is not found, or if no simulations are found for the user.
   * @throws UnauthorizedUserException If the logged-in user is not authorized to perform this operation.
   */
  public List<Simulation> getUserSimulations(String userId) throws UserNotFoundException, UnauthorizedUserException {
    validateUserOwnership(userId);
    return simulationRepository.findSimulationByUserId(userId);
  }

  /**
   * Creates and saves a new simulation to the database if authorized.
   *
   * @param createSimDTO The simulation data to be created.
   * @param userId       The ID of the user initiating the creation.
   * @return The newly created simulation.
   * @throws UserNotFoundException      If the user is not logged in.
   * @throws UnauthorizedUserException  If the user is not authorized to perform this operation.
   * @throws InvalidSimulationException If the provided simulation data or user ID is invalid.
   */
  public Simulation createSimulation(CreateSimulationDTO createSimDTO, String userId)
          throws
          InvalidSimulationException,
          UserNotFoundException,
          UnauthorizedUserException {

    validateUserOwnership(userId);
    try {
      final var createdSimulation = new Simulation(
              null,
              createSimDTO.name(),
              createSimDTO.wage(),
              createSimDTO.transportCost(),
              createSimDTO.globalRentLimit(),
              createSimDTO.isPublic(),
              userId,
              createSimDTO.creationDate()
      );
      // print all the fields of the simulation


      final var savedSimulation = simulationRepository.save(createdSimulation);
      // Ensure that the city is the same since a new random one is instantiated during saving in DB
      savedSimulation.setCity(createdSimulation.getCity());
      return savedSimulation;
    } catch (IllegalArgumentException e) {
      throw new InvalidSimulationException(e);
    }
  }

  /**
   * Updates an existing simulation with the provided data.
   *
   * @param createSimDTO The new simulation data.
   * @param simulationId The ID of the simulation to update.
   * @return The updated simulation.
   * @throws SimulationNotFoundException If the simulation is not found
   * @throws UserNotFoundException       If the user is not logged in.
   * @throws UnauthorizedUserException   If the logged-in user is not the owner of the simulation.
   * @throws InvalidSimulationException  If the provided simulation data is invalid.
   */
  public Simulation updateSimulation(CreateSimulationDTO createSimDTO, String simulationId)
          throws
          SimulationNotFoundException,
          UserNotFoundException,
          UnauthorizedUserException {
    final var simulation = simulationRepository.findById(simulationId)
            .orElseThrow(() -> new SimulationNotFoundException(SIMULATION_NOT_FOUND));
    validateUserOwnership(simulation.getUserId());
    try {
      final var updatedSimulation = new Simulation(
              simulation.getId(),
              createSimDTO.name(),
              createSimDTO.wage(),
              createSimDTO.transportCost(),
              createSimDTO.globalRentLimit(),
              createSimDTO.isPublic(),
              simulation.getUserId(),
              simulation.getCreationDate()
      );
      return simulationRepository.save(updatedSimulation);
    } catch (IllegalArgumentException e) {
      throw new InvalidSimulationException(e);
    }
  }


  /**
   * Deletes a simulation with the provided ID.
   *
   * @param simulationId The ID of the simulation to delete.
   * @return The deleted simulation.
   * @throws SimulationNotFoundException If the simulation is not found
   * @throws UserNotFoundException       If the user is not logged in.
   * @throws UnauthorizedUserException   If the logged-in user is not the owner of the simulation.
   */
  public Simulation deleteSimulation(String simulationId)
          throws
          SimulationNotFoundException,
          UserNotFoundException,
          UnauthorizedUserException {
    final var simulation = simulationRepository.findById(simulationId)
            .orElseThrow(() -> new SimulationNotFoundException(SIMULATION_NOT_FOUND));
    validateUserOwnership(simulation.getUserId());
    simulationRepository.deleteById(simulationId);
    return simulation;
  }

  /**
   * Deletes all simulations from the database.
   *
   * @throws UserNotFoundException     If the user is not logged in.
   * @throws UnauthorizedUserException If the logged-in user is not the owner of his simulations.
   */
  public void deleteUserSimulations(String userId) throws UserNotFoundException, UnauthorizedUserException {
    validateUserOwnership(userId);
    List<Simulation> simulations = simulationRepository.findSimulationByUserId(userId);
    for (Simulation simulation : simulations) {
      simulationRepository.deleteById(simulation.getId());
    }
  }

  /**
   * Verifies if the logged-in user owns the simulation they are operating on.
   *
   * @param userId the logged-in user
   * @throws UserNotFoundException     If the user is not logged in.
   * @throws UnauthorizedUserException If the logged-in user is not the owner of his simulations.
   */
  public void validateUserOwnership(String userId) throws UserNotFoundException, UnauthorizedUserException {
    final var loggedInUser = userService.getLoggedInUser()
            .orElseThrow(() -> new UserNotFoundException("User not logged-in"));
    if (!userId.equals(loggedInUser.getId())) {
      throw new UnauthorizedUserException("User unauthorized to perform this operation");
    }
  }

}

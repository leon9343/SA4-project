package ch.usi.inf.bsc.sa4.lab02spring.controller;

import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.CreateSimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.SimulationDTO;
import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
import ch.usi.inf.bsc.sa4.lab02spring.service.SimulationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/simulations")
@Validated
public class SimulationController {
  private final SimulationService simulationService;

  @Autowired
  public SimulationController(SimulationService simService) {
    this.simulationService = simService;
  }

  private static final boolean isIdValid(String id) {
    return id != null && !id.isEmpty();
  }

  private static final boolean isNameValid(String name) {
    return name != null && !name.isEmpty();
  }

  private static final boolean isAverageIncomeValid(Double averageIncome) {
    return averageIncome != null && averageIncome >= 0;
  }

  private static final boolean isCommuteCostValid(Double commuteCost) {
    return commuteCost != null && commuteCost >= 0;
  }

  private static final boolean isPopulationValid(Integer population) {
    return population != null && population >= 0;
  }

  private static final boolean isCitySizeValid(Integer citySize) {
    return citySize != null && citySize >= 1;
  }

  // All double fields will have to be indentified
  // with Double (non-primitive) since otherwise it cannot be checked if != null
  private static final boolean isMinUtilityValid(Double minUtility) {
    return minUtility != null && minUtility >= 0;
  }

  // Alpha will not be equal to 0, since it is a multiplicator, CAN BE CHANGED
  private static final boolean isAlphaValid(Double alpha) {
    return alpha != null && alpha > 0;
  }

  // Delta will not be zero for the same reason as Alpha, CAN BE CHANGED
  private static final boolean isDeltaValid(Double delta) {
    return delta != null && delta > 0;
  }

  private static final boolean isCostZeroValid(Double costZero) {
    return costZero != null && costZero >= 0;
  }

  private static final boolean isHousingValid(Integer housing) {
    return housing != null && housing >= 0;
  }

  /**
   * Check if the simulation dto is valid
   *
   * @param dto the simulation dto
   * @return true if the dto is valid
   */
  public static boolean isValidSimulationDto(SimulationDTO dto) {
    return dtoParamValidatorFirst(dto)
            && dtoParamValidatorSecond(dto)
            && dtoParamValidatorThird(dto);
  }

  private static boolean dtoParamValidatorFirst(SimulationDTO dto) {
    return dto != null &&
            isIdValid(dto.id()) &&
            isMinUtilityValid(dto.minUtility()) &&
            isAlphaValid(dto.alpha());
  }

  private static boolean dtoParamValidatorSecond(SimulationDTO dto) {
    return dto != null &&
            isAverageIncomeValid(Double.valueOf(dto.averageIncome())) &&
            isCommuteCostValid(Double.valueOf(dto.transportCost()));
  }

  private static boolean dtoParamValidatorThird(SimulationDTO dto) {
    return dto != null &&
            isCostZeroValid(dto.costZero()) &&
            isNameValid(dto.name()) &&
            isDeltaValid(dto.delta());
  }

  /**
   * Check if the creation simulation dto is valid
   *
   * @param dto the creation simulation dto
   * @return true if the dto is valid
   */
  public static boolean isValidCreationSimulationDto(CreateSimulationDTO dto) {
    return createdDtoParamCheckerFirst(dto)
            && createdDtoParamCheckerSecond(dto);
  }

  private static boolean createdDtoParamCheckerFirst(CreateSimulationDTO dto) {
    return dto != null &&
            isDeltaValid(dto.delta()) &&
            isCostZeroValid(dto.costZero()) &&
            isAlphaValid(dto.alpha());
  }

  private static boolean createdDtoParamCheckerSecond(CreateSimulationDTO dto) {
    return
            isAverageIncomeValid(Double.valueOf(dto.wage())) &&
                    isCommuteCostValid(Double.valueOf(dto.transportCost())) &&
                    isMinUtilityValid(dto.minUtility()) &&
                    isNameValid(dto.name());

  }

  /**
   * Retrieves a simulation with the provided ID.
   *
   * @param simulationId The ID of the simulation to retrieve.
   * @return ResponseEntity containing the simulation DTO if found, otherwise returns an appropriate error response.
   */
  @GetMapping("/{simulationId}")
  public ResponseEntity<SimulationDTO> getSimulation(@PathVariable String simulationId) {
    Simulation simulation = simulationService.getSimulation(simulationId);
    var simulationDTO = new SimulationDTO(simulation);
    return ResponseEntity.ok(simulationDTO);
  }

  /**
   * Retrieves all simulations associated with a user specified by userId.
   *
   * @param userId The ID of the user whose simulations are to be retrieved.
   * @return ResponseEntity containing a list of simulation DTOs if simulations are found,
   * otherwise returns an appropriate error response.
   */
  @GetMapping("/users/{userId}")
  public ResponseEntity<List<SimulationDTO>> getUserSimulations(@PathVariable String userId) {
    List<SimulationDTO> listSimulationDTO = simulationService.getUserSimulations(userId)
            .stream()
            .map(SimulationDTO::new)
            .toList();
    return ResponseEntity.ok(listSimulationDTO);
  }

  /**
   * Retrieves all public simulations available in the system.
   *
   * @return ResponseEntity containing a list of public simulation DTOs,
   * otherwise returns an appropriate error response.
   */
  @GetMapping
  public ResponseEntity<List<SimulationDTO>> getPublicSimulations() {
    List<SimulationDTO> publicSimulations = simulationService
            .getSimulations()
            .stream()
            .filter(Simulation::isPublic)
            .map(SimulationDTO::new)
            .toList();
    return ResponseEntity.ok(publicSimulations);
  }

  /**
   * Creates a new simulation for the user identified by userId with the data specified by createSimDTO.
   *
   * @param createSimDTO The simulation DTO containing the fields to create the simulation.
   * @param userId       The ID of the user for whom the simulation is created.
   * @return ResponseEntity containing the DTO of the created simulation if successful,
   * otherwise returns an appropriate error response.
   */
  @PostMapping("/users/{userId}")
  public ResponseEntity<SimulationDTO> createSimulation(@RequestBody @Valid CreateSimulationDTO createSimDTO,
                                                        @PathVariable String userId) {

    System.out.println("ETYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
    Simulation createdSim = simulationService.createSimulation(createSimDTO, userId);
    var createdSimDTO = new SimulationDTO(createdSim);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdSimDTO);
  }

  /**
   * Updates the simulation for the specified simulationId with the data specified by updateSimDTO.
   *
   * @param updateSimDTO The simulation DTO containing the fields to change.
   * @param simulationId The ID of the simulation to be updated.
   * @return ResponseEntity containing the DTO of the updated simulation if successful,
   * otherwise returns an appropriate error response.
   */
  @PatchMapping("/{simulationId}")
  public ResponseEntity<SimulationDTO> updateSimulation(@RequestBody @Valid CreateSimulationDTO updateSimDTO,
                                                        @PathVariable String simulationId) {
    Simulation updatedSim = simulationService.updateSimulation(updateSimDTO, simulationId);
    var updatedSimDTO = new SimulationDTO(updatedSim);
    return ResponseEntity.ok(updatedSimDTO);
  }

  /**
   * Deletes the simulation with the specified simulationId.
   *
   * @param simulationId The ID of the simulation to delete.
   * @return ResponseEntity containing the DTO of the deleted simulation if successful,
   * otherwise returns an appropriate error response.
   */
  @DeleteMapping("/{simulationId}")
  public ResponseEntity<SimulationDTO> deleteSimulation(@PathVariable String simulationId) {
    Simulation deletedSim = simulationService.deleteSimulation(simulationId);
    var deletedSimDTO = new SimulationDTO(deletedSim);
    return ResponseEntity.ok(deletedSimDTO);
  }
}

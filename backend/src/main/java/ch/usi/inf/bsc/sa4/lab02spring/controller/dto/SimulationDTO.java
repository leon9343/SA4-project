package ch.usi.inf.bsc.sa4.lab02spring.controller.dto;

import ch.usi.inf.bsc.sa4.lab02spring.model.City;
import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO for the simulation
 */
public record SimulationDTO(
        @NotNull(message = "Invalid id: id is NULL")
        String id,

        @NotNull(message = "Invalid name: name is NULL")
        @Size(min = 3, max = 50, message = "Invalid name: must be of 3 - 50 characters")
        String name,

        @NotNull(message = "Invalid wage: wage is NULL")
        @PositiveOrZero(message = "Invalid wage: must be positive or zero")
        Integer averageIncome,

        @NotNull(message = "Invalid transportCost: transportCost is NULL")
        @PositiveOrZero(message = "Invalid transportCost: must be positive or zero")
        double transportCost,

        @NotNull(message = "Invalid minUtility: maxUtility is NULL")
        @PositiveOrZero(message = "Invalid minUtility: must be positive or zero")
        double minUtility,

        @NotNull(message = "Invalid alpha: alpha is NULL")
        double alpha,

        @NotNull(message = "Invalid delta: delta is NULL")
        double delta,

        @NotNull(message = "Invalid costZero: costZero is NULL")
        @PositiveOrZero(message = "Invalid costZero: must be positive or zero")
        double costZero,

//        @NotNull(message = "Invalid housing: housing is NULL")
//        @PositiveOrZero(message = "Invalid housing: must be positive or zero")
//        int housing,

        @NotNull(message = "Invalid rentEdge: rentEdge is NULL")
        @PositiveOrZero(message = "Invalid rentEdge: must be positive or zero")
        double rentEdge,

        @NotNull(message = "Invalid isPublic: isPublic is NULL")
        Boolean isPublic,

        @NotNull(message = "Invalid userId: User is NULL")
        String userId,

        @NotNull(message = "Invalid creationDate: Date is NULL")
        LocalDateTime creationDate,

        @NotNull(message = "Invalid city: city is NULL")
        City city,

        @PositiveOrZero(message = "Invalid rentLimit: must be positive or zero")
        Double globalRentLimit) {

  /**
   * Constructor for the SimulationDTO
   *
   * @param simulation the simulation
   */
  public SimulationDTO(Simulation simulation) {
    this(simulation.getId(),
            simulation.getName(),
            simulation.getWage(),
            simulation.getTransportCost(),

            simulation.getMinUtility(),
            simulation.getAlpha(),
            simulation.getDelta(),
            simulation.getCostZero(),
            simulation.getRentEdge(),

            simulation.isPublic(),
            simulation.getUserId(),
            simulation.getCreationDate(),
            simulation.getCity(),
            simulation.getGlobalRentLimit().orElse(null)
    );


  }
}

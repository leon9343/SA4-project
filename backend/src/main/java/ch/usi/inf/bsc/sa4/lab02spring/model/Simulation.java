package ch.usi.inf.bsc.sa4.lab02spring.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Represents a simulation
 */
@Document(collection = "simulations")
public class Simulation {

  /**
   * The error thrown when the average income is negative
   */
  private static final String BAD_INCOME = "wage must be positive or zero";

  /**
   * The error thrown when the commute cost is negative
   */
  private static final String BAD_TRANSPORT_COST = "transportCost must be positive or zero";

  /**
   * The minimum length of the name
   */
  private static final int MIN_NAME_LENGTH = 3;

  /**
   * The maximum length of the name
   */
  private static final int MAX_NAME_LENGTH = 50;

// ---------------------------- Parameters -------------------------------------
  /**
   * The ID of the simulation
   */
  @Id
  private final String id;

  /**
   * The ID of the user associated with the simulation
   */
  @NotNull(message = "user is NULL")
  private final String userId;
  /**
   * The constants of the simulation
   */
  private final SimulationConstants constants = new SimulationConstants(
          12500, //MIN_UTILITY
          2000, //ALPHA
          20, //COST_ZERO
          1.5, //DELTA
          0 //RENT_EDGE
  );
  /**
   * The average income of the simulation
   */
  private final int wage;
  /**
   * The name of the simulation
   */
  @NotNull(message = "name is NULL")
  @Size(min = 3, max = 50, message = "must be of 3 - 50 characters")
  private String name;
  /**
   * Indicates whether the simulation is public
   */
  @NotNull(message = "isPublic is NULL")
  private boolean isPublic;
  /**
   * The creation date of the simulation
   */
  @NotNull(message = "date is NULL")
  private LocalDateTime creationDate;
  /**
   * The parameters of the simulation
   */
  @NotNull
  private SimulationParameters parameters;
  /**
   * The city of the simulation
   */
  @NotNull(message = "city is NULL")
  private City city;


  /**
   * Constructor for Simulation
   *
   * @param id            The ID of the simulation in the database
   * @param name          The name of the simulation
   * @param wage          The average income of the simulation
   * @param transportCost The commute cost of the simulation
   * @param isPublic      Indicates whether the simulation is public
   * @param userId        The ID of the user associated with the simulation
   * @param creationDate  The creation date of the simulation
   * @throws IllegalArgumentException If any of the parameters are invalid
   */
  @PersistenceCreator
  public Simulation(String id,
                    String name,
                    int wage,
                    double transportCost,
                    Double globalRentLimit,
                    boolean isPublic,
                    String userId,
                    LocalDateTime creationDate) throws IllegalArgumentException {
    //TODO: consider remove this check due to the jakarta validation.
    if (name == null) {
      throw new IllegalArgumentException("name is NULL");
    }
    if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
      throw new IllegalArgumentException("name must be of 3 - 50 characters");
    }
    if (wage < 0) {
      throw new IllegalArgumentException(BAD_INCOME);
    }
    if (transportCost < 0) {
      throw new IllegalArgumentException(BAD_TRANSPORT_COST);
    }
    if (userId == null) {
      throw new IllegalArgumentException("userId is NULL");
    }
    if (creationDate == null) {
      throw new IllegalArgumentException("creationDate is NULL");
    }

    this.id = id;
    this.userId = userId;
    this.name = name;
    this.parameters = new SimulationParameters(wage, transportCost, globalRentLimit);
    this.wage = wage;
    this.creationDate = creationDate;
    this.isPublic = isPublic;
    this.city = new City(constants, parameters);

    System.out.println("SIMULATION CREATED");
  }

  /**
   * Empty constructor for Simulation used to fix errors with Spring Boot
   */
  public Simulation() {
    this.id = "";
    this.name = "";
    this.userId = "";
    this.parameters = new SimulationParameters(0, 0, null);
    this.creationDate = LocalDateTime.now();
    this.wage = 0;
    this.isPublic = false;
    this.city = new City();
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  /**
   * Sets the name of the simulation
   *
   * @param name the new name of the simulation
   * @throws IllegalArgumentException if the name is null or is not between 3 and 50 character long
   */
  public void setName(String name) throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("id is NULL");
    }
    if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
      throw new IllegalArgumentException("id must be of 3 - 50 characters");
    }
    this.name = name;
  }

  public String getUserId() {
    return userId;
  }

  //TODO consider remove and keep only the getParameters

  /**
   * Returns the average income of the simulation
   *
   * @return the average income of the simulation
   */
  public int getWage() {
    return parameters.wage();
  }

  //TODO consider remove and keep only the getParameters

  /**
   * Returns the transport cost of the simulation
   *
   * @return the transport cost of the simulation
   */
  public double getTransportCost() {
    return parameters.transportCost();
  }

  /**
   * Returns the global rent limit of the simulation
   *
   * @return the global rent limit of the simulation
   */
  public Optional<Double> getGlobalRentLimit() {
    return Optional.ofNullable(parameters.globalRentLimit());
  }

  /**
   * Returns the parameters of the simulation
   *
   * @return the parameters of the simulation
   */
  public SimulationParameters getParameters() {
    return parameters;
  }

  /**
   * Sets the new parameters and recomputes the city accordigly
   *
   * @param params the parameters record
   * @throws IllegalArgumentException if the parameters id null
   */
  public void setParameters(SimulationParameters params) {
    if (parameters == null) {
      throw new IllegalArgumentException("parameters are NULL");
    }
    this.parameters = params;
    city = new City(constants, parameters);
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  /**
   * Sets the creation date of the simulation
   *
   * @param creationDate the date of creation of the simulation
   * @throws IllegalArgumentException if the city is null
   */
  public void setCreationDate(LocalDateTime creationDate) {
    if (creationDate == null) {
      throw new IllegalArgumentException("creationDate is NULL");
    }
    this.creationDate = creationDate;
  }

  public City getCity() {
    return this.city;
  }

  /**
   * Sets the city of the simulation
   *
   * @param city the city of the simulation
   * @throws IllegalArgumentException if the city is null
   */
  public void setCity(City city) throws IllegalArgumentException {
    if (city == null) {
      throw new IllegalArgumentException("city is NULL");
    }
    this.city = city;
  }

  /**
   * Returns the constant minUtility
   *
   * @return the constant minUtility
   */
  public int getMinUtility() {
    return constants.minUtility();
  }

  /**
   * Returns the constant costZero
   *
   * @return the constant costZero
   */
  public double getCostZero() {
    return constants.costZero();
  }

  /**
   * Returns the constant delta
   *
   * @return the constant delta
   */
  public double getDelta() {
    return constants.delta();
  }

  /**
   * Returns the constant rentEdge
   *
   * @return the constant rentEdge
   */
  public int getRentEdge() {
    return constants.rentEdge();
  }

  /**
   * Returns the constant alpha
   *
   * @return the constant alpha
   */
  public double getAlpha() {
    return constants.alpha();
  }

  /**
   * Returns whether the simulation is public
   *
   * @return true if the simulation is public
   */
  public boolean isPublic() {
    return this.isPublic;
  }

  /**
   * Sets the public status of the simulation
   *
   * @param isPublic the new public status
   */
  public void setPublic(boolean isPublic) {
    this.isPublic = isPublic;
  }

}

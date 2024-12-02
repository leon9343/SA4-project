package ch.usi.inf.bsc.sa4.lab02spring.model.blocks;

import ch.usi.inf.bsc.sa4.lab02spring.utils.Coordinate;

import java.math.BigDecimal;
import java.util.Objects;

import static java.lang.Math.pow;

/**
 * A block in the city.
 */
public class Block {
  /**
   * The error message for a null coordinate.
   */
  private static final String BAD_COORDINATE = "coordinate is NULL";
  /**
   * The error message for a null block type.
   */
  private static final String BAD_BLOCK_TYPE = "blockType is NULL";
  /**
   * The error message for a negative height.
   */
  private static final String BAD_HEIGHT = "height must be positive or zero";
  /**
   * The error message for a negative population.
   */
  private static final String BAD_POPULATION = "population must be positive or zero";
  /**
   * The error message for a negative cost.
   */
  private static final String BAD_COST = "cost must be positive or zero";
  /**
   * The maximum population of a floor.
   */
  private static final int MAX_FLOOR_POPULATION = 5;
  /**
   * The maximum number of floors.
   */
  private static final int MAX_FLOOR_COUNT = 10;
  /**
   * The maximum cost of renting.
   */
  private static final int MAX_COST = 800;
  /**
   * The length of the block.
   */
  private static final double LENGTH = 1;
  /**
   * The coordinate of the block.
   */
  private Coordinate coordinate;
  /**
   * The distance from the center of the city.
   */
  private int distanceFromCenter;
  /**
   * The type of the block.
   */
  private BlockType blockType;
  /**
   * The number of floors.
   */
  private int floorCount;
  /**
   * The cost of renting.
   */
  private BigDecimal housingCost;
  /**
   * The population.
   */
  private int population;
  /**
   * The height of the block.
   */
  private int height;
  /**
   * The cost for each floor.
   */
  private double heightCost;
  /**
   * The cost of land.
   */
  private double landCost;
  /**
   * The cost to travel to CBD.
   */
  private double commuteCost;

  /**
   * Constructor for Block when it's a WATER or PARK
   *
   * @param coordinate the coordinate in form of (x, y)
   * @param blockType  the type of the block
   * @throws IllegalArgumentException if any of the parameters are invalid
   */
  public Block(Coordinate coordinate,
               int distanceFromCenter,
               BlockType blockType) throws IllegalArgumentException {
    if (coordinate == null) {
      throw new IllegalArgumentException(BAD_COORDINATE);
    }
    if (blockType == null) {
      throw new IllegalArgumentException(BAD_BLOCK_TYPE);
    }
    this.coordinate = coordinate;
    this.blockType = blockType;
    this.distanceFromCenter = distanceFromCenter;
  }

  /**
   * Constructor for Block when it's RESIDENTIAL
   *
   * @param coordinate  the coordinate in form of (x, y)
   * @param floorCount  the number of floors
   * @param housingCost the cost of the renting
   * @param landCost    the cost of land
   * @param popDensity  the population in the block
   * @throws IllegalArgumentException if any of the parameters are invalid
   */
  public Block(Coordinate coordinate,
               int distanceFromCenter,
               int floorCount,
               double housingCost,
               double popDensity,
               double landCost,
               double transportCost) throws IllegalArgumentException {
    if (floorCount < 0) {
      throw new IllegalArgumentException(BAD_HEIGHT);
    }
    if (housingCost < 0) {
      throw new IllegalArgumentException("housingCost must be positive or zero");
    }
    if (popDensity < 0) {
      throw new IllegalArgumentException(BAD_POPULATION);
    }
    if (coordinate == null) {
      throw new IllegalArgumentException(BAD_COORDINATE);
    }
    this.coordinate = coordinate;
    this.distanceFromCenter = distanceFromCenter;
    this.floorCount = floorCount;
    this.housingCost = BigDecimal.valueOf(housingCost);
    this.population = (int) (popDensity * pow(LENGTH, 2));
    this.commuteCost = transportCost * distanceFromCenter;
    this.landCost = landCost;
    this.blockType = BlockType.RESIDENTIAL;
  }

  /**
   * Empty constructor for Block used to fix errors with Spring Boot
   */
  public Block() {
    this.coordinate = new Coordinate();
    this.floorCount = 0;
    this.housingCost = BigDecimal.valueOf(0);
    this.population = 0;
    this.blockType = BlockType.WATER;
    this.distanceFromCenter = 0;
    this.commuteCost = 0;
  }

  /**
   * Calculates the taxicab distance from the center of the city with respect to a given coordinate
   *
   * @return the distance from the center of the city
   */
  public final int calculateDistanceFromCenter() {
    return Math.abs(coordinate.getX()) + Math.abs(coordinate.getY());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Block that = (Block) o;
    return floorCount == that.floorCount
            && Objects.equals(housingCost, that.housingCost)
            && population == that.population
            && distanceFromCenter == that.distanceFromCenter
            && coordinate.equals(that.coordinate)
            && blockType == that.blockType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinate, floorCount, housingCost, population, blockType, distanceFromCenter);
  }

  /**
   * Returns the distance from the center of the city
   *
   * @return the distance from the center
   */
  public int getDistanceFromCenter() {
    return distanceFromCenter;
  }

  /**
   * Sets the distance from the center of the city
   *
   * @param distanceFromCenter the distance from the center
   * @throws IllegalArgumentException if the distance is less than 0
   */
  public void setDistanceFromCenter(int distanceFromCenter) throws IllegalArgumentException {
    if (distanceFromCenter < 0) {
      throw new IllegalArgumentException("distanceFromCenter must be positive or zero");
    }
    this.distanceFromCenter = distanceFromCenter;
  }

  public double getLength() {
    return LENGTH;
  }

  public int getHeight() {
    return height;
  }

  /**
   * Sets the height of the block.
   *
   * @param height the height of the block
   */
  public void setHeight(int height) {
    this.height = height;
  }

  public double getLandCost() {
    return landCost;
  }

  /**
   * Sets the cost of land.
   *
   * @param landCost the cost of land
   */
  public void setLandCost(double landCost) {
    this.landCost = landCost;
  }

  public BigDecimal getHousingCost() {
    return housingCost;
  }

  /**
   * Sets the cost of housing.
   *
   * @param housingCost the cost of housing
   */
  public void setHousingCost(double housingCost) {
    this.housingCost = BigDecimal.valueOf(housingCost);
  }

  public double getHeightCost() {
    return heightCost;
  }

  /**
   * Sets the cost of each floor.
   *
   * @param heightCost the cost of each floor
   */
  public void setHeightCost(double heightCost) {
    this.heightCost = heightCost;
  }

  /**
   * Returns the travelCost of the block
   *
   * @return the travelCost
   */
  public double getCommuteCost() {
    return this.commuteCost;
  }

  /**
   * Sets the travelCost of the block
   *
   * @param commuteCost the cost
   * @throws IllegalArgumentException if the population is less than 0
   */
  public void setCommuteCost(double commuteCost) {
    if (commuteCost < 0) {
      throw new IllegalArgumentException(BAD_COST);
    }
    this.commuteCost = commuteCost * distanceFromCenter;
  }

  /**
   * Returns the type of the block among RESIDENTIAL, PARK and WATER
   *
   * @return the type of the block
   */
  public BlockType getBlockType() {
    return blockType;
  }

  /**
   * Sets the block type of the block among RESIDENTIAL, PARK and WATER
   *
   * @param blockType the type of the block
   * @throws IllegalArgumentException if the blockType is null
   */
  public void setBlockType(BlockType blockType) throws IllegalArgumentException {
    if (blockType == null) {
      throw new IllegalArgumentException(BAD_BLOCK_TYPE);
    }
    this.blockType = blockType;
  }

  /**
   * Returns the population of the block
   *
   * @return the population
   */
  public int getPopulation() {
    return population;
  }

  /**
   * Sets the population of the block
   *
   * @param population the population
   * @throws IllegalArgumentException if the population is less than 0
   */
  public void setPopulation(int population) {
    if (population < 0) {
      throw new IllegalArgumentException(BAD_POPULATION);
    }
    this.population = population;
  }

  public int getFloorCount() {
    return floorCount;
  }

  /**
   * Sets the number of floors of the block
   *
   * @param floorCount the number of floors
   * @throws IllegalArgumentException if the floorCount is less than 0
   */
  public void setFloorCount(int floorCount) throws IllegalArgumentException {
    if (floorCount < 0) {
      throw new IllegalArgumentException("floorCount must be positive or zero");
    }
    this.floorCount = floorCount;
  }

  /**
   * Returns the coordinate of the block in form (x, y)
   *
   * @return the coordinate
   */
  public Coordinate getCoordinate() {
    return coordinate;
  }

  /**
   * Sets the coordinate of the block in form (x, y)
   *
   * @param coordinate the coordinate
   * @throws IllegalArgumentException if coordinate is null
   */


  public void setCoordinate(Coordinate coordinate) throws IllegalArgumentException {
    if (coordinate == null) {
      throw new IllegalArgumentException(BAD_COORDINATE);
    }
    this.coordinate = coordinate;
  }

  /**
   * Sets the cost of renting of the block
   *
   * @param rentingCost the cost of renting
   * @throws IllegalArgumentException if the rentingCost is less than 0
   */
  public void setRentingCost(int rentingCost) {
    if (rentingCost < 0) {
      throw new IllegalArgumentException("rentingCost must be positive or zero");
    }
    this.housingCost = BigDecimal.valueOf(rentingCost);
  }
}


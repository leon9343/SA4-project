package ch.usi.inf.bsc.sa4.lab02spring.model;

import ch.usi.inf.bsc.sa4.lab02spring.model.blocks.Block;
import ch.usi.inf.bsc.sa4.lab02spring.utils.Coordinate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.*;

public class City {
  @NotNull(message = "blocks is NULL")
  private Map<Coordinate, Block> blocks;
  @Positive(message = "must be positive")
  private int size;
  private int housing;
  private int population;

  /**
   * Constructor for City
   *
   * @throws IllegalArgumentException If any of the parameters are invalid
   */
  public City(SimulationConstants constants,
              SimulationParameters parameters) throws IllegalArgumentException {
    this.blocks = build(constants, parameters);
  }

  /**
   * Empty constructor for City used to fix errors with Spring Boot
   */
  public City() {
    this.blocks = new HashMap<>();
    this.size = 0;
  }

  private static double landCost(int d, SimulationConstants c, SimulationParameters p) {
    return (c.delta() - 1)
            * pow(c.delta(), (c.delta() / (1 - c.delta())))
            * pow(c.costZero(), (1 / (1 - c.delta())))
            * pow(c.alpha(), c.delta() / (1 - c.delta()))
            * exp(c.delta() * (p.wage() - c.minUtility() - c.alpha() - p.transportCost() * d)
            / (c.alpha() * (c.delta() - 1)));
  }

  private static double popDensity(int d, SimulationConstants c, SimulationParameters p) {
    return pow(c.delta(), 1 / (1 - c.delta()))
            * pow(c.costZero(), 1 / (1 - c.delta()))
            * pow(c.alpha(), 1 / (c.delta() - 1))
            * exp(c.delta() * (p.wage() - c.minUtility() - c.alpha() - p.transportCost() * d)
            / (c.alpha() * (c.delta() - 1)));
  }

  /**
   * Builds the city
   *
   * @param c the simulation constants
   * @param p the simulation parameters
   * @return the city
   */
  private Map<Coordinate, Block> build(SimulationConstants c, SimulationParameters p) {
    Map<Coordinate, Block> city = new HashMap<>();
    var aux = 1.0;
    int size = 10;

    System.out.println("WE ARE IN BUILD CITY");
    for (int row = -size; row < size; row++) {
      for (int column = -size; column < size; column++) {
        var coordinate = new Coordinate(row, column);
        var distance = abs(coordinate.getX()) + abs(coordinate.getY());
        var block = new Block(
                coordinate,
                distance,
                floorCount(distance, c, p),
                housingCost(distance, c, p),
                popDensity(distance, c, p),
                landCost(distance, c, p),
                p.transportCost()
        );
        housing++;
        population += block.getPopulation();
        aux = landCost(distance, c, p);
        city.put(coordinate, block);
      }
    }
    this.size = size;

    System.out.println("we are out of the loop");
    return city;
  }

  private double housingCost(int d, SimulationConstants c, SimulationParameters p) {
    double calculatedCost = c.alpha() * exp((p.wage() - c.minUtility() - c.alpha() - p.transportCost() * d) / c.alpha());

    if (p.globalRentLimit() != null) {
      calculatedCost = Math.min(calculatedCost, p.globalRentLimit());
    }
    return calculatedCost;
  }

  private int houseCount(int d, SimulationConstants c, SimulationParameters p) {
    return (int) (c.alpha() / housingCost(d, c, p));
  }

  private int floorCount(int d, SimulationConstants c, SimulationParameters p) {
    return (int) (pow(c.delta(), (1 / (1 - c.delta())))
            * pow(c.costZero(), (1 / (1 - c.delta())))
            * pow(housingCost(d, c, p), 1 / (c.delta() - 1)));
  }

  /**
   * Checks if the chosen indexes to are within the matrix bounds
   *
   * @param row    the row of the matrix
   * @param column the column of the matrix
   * @throws IllegalArgumentException if the row or the column index is invalid
   */
  private void checkBlockIndexes(int row, int column) throws IllegalArgumentException {
    if (row < -this.size || row >= this.size) {
      throw new IllegalArgumentException("row must be between 0 and " + (this.size - 1));
    }
    if (column < -this.size || column >= this.size) {
      throw new IllegalArgumentException("column must be between 0 and " + (this.size - 1));
    }
  }

  public Map<Coordinate, Block> getBlocks() {
    return new HashMap<>(blocks);
  }

  /**
   * Sets the name of the city
   *
   * @param blocks the name of the city
   * @throws IllegalArgumentException if the name is null or is not between 3 and 50 character long
   */
  public void setBlocks(Map<Coordinate, Block> blocks) throws IllegalArgumentException {
    if (blocks == null) {
      throw new IllegalArgumentException("blocks is NULL");
    }
    this.blocks = new HashMap<>(blocks);
  }

  /**
   * Returns the block at the given row and column indexes
   *
   * @param row    the row of the block
   * @param column the column of the block
   * @return the block in position (row, column)
   * @throws IllegalArgumentException if the row or column index is invalid
   */
  public Block getBlock(int row, int column) throws IllegalArgumentException {
    checkBlockIndexes(row, column);
    var coordinate = new Coordinate(row, column);
    return blocks.get(coordinate);
  }

  public int getSize() {
    return this.size;
  }

  /**
   * Sets the radius of the city
   *
   * @param size the new radius
   * @throws IllegalArgumentException if radius is less than or equal to 0
   */
  public void setSize(int size) throws IllegalArgumentException {
    if (size < 1) {
      throw new IllegalArgumentException("size must be positive");
    }
    this.size = size;
  }

  /**
   * Sets the block at the given row and column indexes
   *
   * @param row    the row of the block
   * @param column the column of the block
   * @param block  the block in position (row, column)
   * @throws IllegalArgumentException if the row or column index is invalid
   */
  public void setBlock(int row, int column, Block block) throws IllegalArgumentException {
    checkBlockIndexes(row, column);
    var coordinate = new Coordinate(row, column);
    blocks.put(coordinate, block);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    City that = (City) o;
    return size == that.size
            && blocksEquals(that.blocks);
  }

  /**
   * Compares the blocks of this city with the blocks of another city
   *
   * @param otherBlocks the blocks of the other city
   * @return true if the blocks are equal, false otherwise
   */
  private boolean blocksEquals(Map<Coordinate, Block> otherBlocks) {
    if (blocks.size() != otherBlocks.size()) {
      return false;
    }
    for (Map.Entry<Coordinate, Block> entry : blocks.entrySet()) {
      Coordinate coordinate = entry.getKey();
      Block block = entry.getValue();
      if (!otherBlocks.containsKey(coordinate)) {
        return false;
      }
      if (!block.equals(otherBlocks.get(coordinate))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(blocks, size);
  }
}

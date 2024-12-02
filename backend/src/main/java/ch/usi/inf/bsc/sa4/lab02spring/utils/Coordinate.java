package ch.usi.inf.bsc.sa4.lab02spring.utils;

import java.util.Objects;

/**
 * Class for the Coordinate
 */
public class Coordinate {
  /**
   * The x coordinate
   */
  private int x;
  /**
   * The y coordinate
   */
  private int y;

  /**
   * Empty constructor for Coordinate
   */
  public Coordinate() {
    this.x = 0;
    this.y = 0;
  }

  /**
   * Constructor for Coordinate
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Checks if the current coordinate is equal to the other coordinate
   *
   * @param other the other coordinate
   * @return a boolean whether the 2 coordinates are equal
   */
  public boolean areCoordinatesEqual(Coordinate other) {
    return this.x == other.getX() && this.y == other.getY();
  }

  public int getX() {
    return x;
  }

  /**
   * Sets the coordinate x
   *
   * @param x the x coordinate
   */
  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  /**
   * Sets the coordinate y
   *
   * @param y the y coordinate
   */
  public void setY(int y) {
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Coordinate that = (Coordinate) o;
    return x == that.x
            && y == that.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

}

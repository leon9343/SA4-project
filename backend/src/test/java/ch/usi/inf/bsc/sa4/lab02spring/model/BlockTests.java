//package ch.usi.inf.bsc.sa4.lab02spring.model;
//
//
//import ch.usi.inf.bsc.sa4.lab02spring.model.blocks.Block;
//import ch.usi.inf.bsc.sa4.lab02spring.model.blocks.BlockType;
//import ch.usi.inf.bsc.sa4.lab02spring.utils.Coordinate;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//class BlockTests {
//  private Block testBlock;
//
//  @BeforeEach
//  void setUp() {
//    Coordinate coordinate = new Coordinate(5, 5);
//    testBlock = new Block(coordinate, 10, 1000, 4, BlockType.RESIDENTIAL);
//  }
//
//  @DisplayName("Verify floor count is calculated within expected range")
//  @Test
//  void testFloorCountWithinRange() {
//    int floorCount = testBlock.getFloorCount();
//    assertTrue(floorCount >= 1 && floorCount <= 10,
//            () -> "Expected floor count to be between 1 and 10, but was " + floorCount);
//  }
//
//  @DisplayName("Verifying the distance from the center")
//  @Test
//  void testDistanceFromCenter() {
//    int distance = testBlock.calculateDistanceFromCenter();
//    assertEquals(10, distance, () -> "Expected distance to be 10, but it is " + distance);
//  }
//
//  @DisplayName("Verify cost of renting is calculated within expected range")
//  @Test
//  void testCostOfRentingWithinRange() {
//    int distanceFromCenter = testBlock.getDistanceFromCenter();
//    int expectedMinimumCost = (int) (800 / (distanceFromCenter + 1));
//    int costOfRenting = testBlock.getCostOfRenting();
//    assertTrue(costOfRenting >= expectedMinimumCost,
//            () -> "Expected cost of renting to be at least " + expectedMinimumCost + ", but was " + costOfRenting);
//  }
//
//  @DisplayName("Verify population per floor is calculated within expected range")
//  @Test
//  void testPopulationWithinRange() {
//    int population = testBlock.getPopulation();
//    assertTrue(population >= 0 && population <= 5,
//            () -> "Expected population per floor to be between 0 and 5, but was " + population);
//  }
//
//  @Test
//  @DisplayName("Constructor throws IllegalArgumentException for null coordinate")
//  void testConstructorThrowsForNullCoordinate() {
//    assertThrows(IllegalArgumentException.class, () -> new Block(null, BlockType.RESIDENTIAL), "Expected IllegalArgumentException for null coordinate");
//  }
//
//  @Test
//  @DisplayName("Constructor throws IllegalArgumentException for null blockType")
//  void testConstructorThrowsForNullBlockType() {
//    Coordinate coordinate = new Coordinate(1, 1);
//    assertThrows(IllegalArgumentException.class, () -> new Block(coordinate, null), "Expected IllegalArgumentException for null blockType");
//  }
//}

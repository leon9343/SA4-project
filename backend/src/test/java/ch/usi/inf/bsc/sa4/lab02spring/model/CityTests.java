//package ch.usi.inf.bsc.sa4.lab02spring.model;
//
//import ch.usi.inf.bsc.sa4.lab02spring.model.blocks.Block;
//import ch.usi.inf.bsc.sa4.lab02spring.model.blocks.BlockType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.Collections;
//import java.util.EnumSet;
//import java.util.List;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CityTests {
//  private static final Set<BlockType> POPULATIONS = Collections.unmodifiableSet(EnumSet.of(BlockType.RESIDENTIAL, BlockType.PARK, BlockType.WATER));
//  private City city;
//  private City completeCity;
//
//  @BeforeEach
//  void setUp() {
//    String name = "XXX";
//    int averageIncome = 50;
//    int commuteCost = 10;
//
//    completeCity = new City(name, averageIncome, commuteCost);
//    city = new City(name);
//  }
//
//  @Test
//  @DisplayName("City object is correctly instantiated and updated")
//  void testBasicCase() {
//    int averageIncome = 60;
//    int commuteCost = 20;
//    int size = 14;
//    city.update(averageIncome, commuteCost, population, size);
//    assertEquals(city.getName(), "XXX", () -> "Name should be XXX");
//  }
//
//  @DisplayName("Should not accept city with null name")
//  @Test
//  void testNullCityName() {
//    String name = "";
//    int averageIncome = 50;
//    int commuteCost = 10;
//    assertThrows(IllegalArgumentException.class, () -> new City(name, averageIncome, commuteCost));
//  }
//
//  @DisplayName("Will throw exceptions when fed illegal value")
//  @Test
//  public void testInitializeBlock() {
//    int population = 100;
//    int averageIncome = 50;
//    int commuteCost = 10;
//
//    city.update(averageIncome, commuteCost);
//
//    assertThrows(IllegalArgumentException.class, () -> city.update(averageIncome, commuteCost));
//    assertThrows(IllegalArgumentException.class, () -> city.update(averageIncome, commuteCost));
//    assertThrows(IllegalArgumentException.class, () -> city.update(averageIncome, commuteCost));
//    assertThrows(IllegalArgumentException.class, () -> city.update(averageIncome, commuteCost));
//    assertThrows(IllegalArgumentException.class, () -> city.update(averageIncome, commuteCost));
//
//  }
//
//  @Test
//  @DisplayName("City object is correctly instantiated with full constructor")
//  public void testFullConstructorInitialization() {
//    assertNotNull(completeCity.getBlocks(), "Blocks should be initialized");
//    assertEquals("XXX", completeCity.getName(), "Name should be initialized correctly");
//    assertEquals(10, completeCity.getSize(), "Radius should be initialized correctly");
//  }
//
//  @Test
//  @DisplayName("Invalid parameters in full constructor cause exceptions")
//  void testInvalidParameters() {
//    assertThrows(IllegalArgumentException.class, () -> new City(null, 50, 10));
//    assertThrows(IllegalArgumentException.class, () -> new City("X", 0, 10));
//    assertThrows(IllegalArgumentException.class, () -> new City("XX", 50, 0));
//    assertThrows(IllegalArgumentException.class, () -> new City("XXX", 50, 10));
//    assertThrows(IllegalArgumentException.class, () -> new City("XXX", -50, 10));
//  }
//
//  @Test
//  @DisplayName("Check block initialization correctness")
//  void testBlockInitialization() {
//    Block block = completeCity.getBlocks().getFirst().getFirst();
//    assertNotNull(block, "Block should not be null");
//    assertTrue(POPULATIONS.contains(block.getBlockType()), "Block type should be set correctly");
//  }
//
//  @Test
//  @DisplayName("the matrix has correct dimensions")
//  void testMatrixDimensions() {
//    List<List<Block>> blocks = completeCity.getBlocks();
//    assertEquals(10, blocks.size(), () -> "The city should have " + 10 + " rows");
//    blocks.forEach(row -> assertEquals(10, row.size(), () -> "Each row should contain " + 10 + " columns"));
//  }
//}
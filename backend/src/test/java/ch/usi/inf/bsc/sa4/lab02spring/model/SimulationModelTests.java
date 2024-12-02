//package ch.usi.inf.bsc.sa4.lab02spring.model;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SimulationModelTests {
//  private final LocalDateTime testDate = LocalDateTime.now();
//  private Simulation simulation;
//
//  @BeforeEach
//  void setUp() {
//    // Initialize Simulation object with consistent test data
//    simulation = new Simulation("1", "TestCity", 50000, 300, true, "JovoBiden", testDate);
//  }
//
//  public void simulate(Integer averageIncome, Integer commuteCost, Integer population, Integer size) {
//    // Directly use setters or similar methods if available in the Simulation class to update the state
//    simulation.setWage(averageIncome);
//    simulation.setCommuteCost(commuteCost);
//  }
//
//  @Test
//  @DisplayName("Test Simulation Constructor for Correct Initialization")
//  void testSimulationConstructor() {
//    assertEquals("1", simulation.getId());
//    assertEquals("TestCity", simulation.getName());
//    assertEquals(50000, simulation.getWage());
//    assertEquals(300, simulation.getCommuteCost());
//    assertTrue(simulation.isPublic());
//    assertEquals("JovoBiden", simulation.getUserId());
//    assertEquals(testDate, simulation.getCreationDate());
//  }
//
//  @Test
//  @DisplayName("simulate method updates simulation state correctly")
//  void testSimulate() {
//    System.out.println("Before simulate - Income: " + simulation.getWage());
//    simulate(55000, 350, 105000, 10);  // Use the local simulate method
//    System.out.println("After simulate - Income: " + simulation.getWage());
//    assertEquals(55000, simulation.getWage(), "Average income should be correctly updated to 55000 after simulation.");
//    assertEquals(350, simulation.getCommuteCost(), "Commute cost should be correctly updated to 350 after simulation.");
//  }
//
//
//  @Test
//  @DisplayName("setAverageIncome updates value with valid input")
//  void testSetAverageIncomeValid() {
//    simulation.setWage(60000);
//    assertEquals(60000, simulation.getWage(), "Average income should be updated correctly with valid input.");
//  }
//
//  @Test
//  @DisplayName("setAverageIncome throws IllegalArgumentException for invalid input")
//  void testSetAverageIncomeInvalid() {
//    Exception exception = assertThrows(IllegalArgumentException.class, () -> simulation.setWage(-100));
//    assertEquals("wage must be positive or zero", exception.getMessage(), "setAverageIncome should throw IllegalArgumentException for non-positive values.");
//  }
//
//  @Test
//  @DisplayName("setCommuteCost updates value with valid input")
//  void testSetCommuteCostValid() {
//    simulation.setCommuteCost(200);
//    assertEquals(Integer.valueOf(200), simulation.getCommuteCost(), "Commute cost should be updated correctly with valid input.");
//  }
//
//  @Test
//  @DisplayName("setCommuteCost throws IllegalArgumentException for invalid input")
//  void testSetCommuteCostInvalid() {
//    Exception exception = assertThrows(IllegalArgumentException.class, () -> simulation.setCommuteCost(-50));
//    assertEquals("transportCost must be positive or zero", exception.getMessage(),
//            "setCommuteCost should throw IllegalArgumentException for non-positive values.");
//  }
//
//}
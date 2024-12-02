//package ch.usi.inf.bsc.sa4.lab02spring.service;
//
//import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.CreateSimulationDTO;
//import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.SimulationDTO;
//import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
//import ch.usi.inf.bsc.sa4.lab02spring.model.User;
//import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.InvalidSimulationException;
//import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.SimulationNotFoundException;
//import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.UnauthorizedUserException;
//import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.UserNotFoundException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
///**
// * Test suite for SimulationService class.
// */
//@SpringBootTest
//@AutoConfigureMockMvc
//@ExtendWith(SpringExtension.class)
//@DisplayName("SimulationService Tests")
//class SimulationServiceTests {
//
//  private static final LocalDateTime testDate = LocalDateTime.now();
//  private static Simulation simulation;
//  private static CreateSimulationDTO createSimDTO;
//  private static SimulationDTO simulationDTO;
//  private static User user;
//  @Autowired
//  private MockMvc mockMvc;
//  @MockBean
//  private UserService userService;
//  @Autowired
//  private SimulationService simulationService;
//  @Autowired
//  private ObjectMapper objectMapper;
//
//  @BeforeAll
//  public static void setupAll() {
//    user = new User("User123", "username", "name", "email@usi.ch");
//    simulation = new Simulation("1234", "SimulationNr1", 6000, 111, 555, true, "MarlonBrando", testDate, 10);
//    createSimDTO = new CreateSimulationDTO("SimulationName", 6000, 111, 555, 10, true, testDate);
//    simulationDTO = new SimulationDTO(simulation);
//  }
//
//  @BeforeEach
//  public void setup() {
//    when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
//  }
//
//  @AfterEach
//  void tearDown() {
//    simulationService.deleteUserSimulations(user.getId());
//  }
//
//  @Test
//  @DisplayName("Test deleting a simulation successfully")
//  void testDeleteSimulationSuccess() {
//    // Create a new simulation
//    Simulation createdSim = simulationService.createSimulation(
//            createSimDTO,
//            user.getId()
//    );
//    try {
//      Simulation deletedSim = simulationService.deleteSimulation(createdSim.getId());
//      assertEquals(createdSim, deletedSim, "The deleted simulation should match the created one");
//    } catch (UserNotFoundException | UnauthorizedUserException e) {
//      fail("Unexpected exception thrown: " + e.getMessage());
//    }
//
//  }
//
//
//  @Test
//  @DisplayName("Test creating a simulation with null name")
//  void testCreateSimulationNullName() throws Exception {
//    CreateSimulationDTO createSimulationDTO = new CreateSimulationDTO(
//            null,
//            6000,
//            111,
//            555,
//            10,
//            true,
//            LocalDateTime.now()
//    );
//    String body = objectMapper.writeValueAsString(createSimulationDTO);
//    mockMvc.perform(
//                    post("/simulations/users/{userId}", "MarlonBrando")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body)
//            )
//            .andExpect(status().isBadRequest());
//  }
//
//  @Test
//  @DisplayName("Test creating a simulation with empty name")
//  void testCreateSimulationEmptyName() {
//    CreateSimulationDTO createSimulationDTO = new CreateSimulationDTO(
//            "",
//            6000,
//            111,
//            555,
//            10,
//            true,
//            testDate
//    );
//    assertThrows(
//            InvalidSimulationException.class,
//            () -> simulationService.createSimulation(createSimulationDTO, "User123"),
//            "Should throw IllegalArgumentException for empty name"
//    );
//  }
//
//  @Test
//  @DisplayName("Test creating a simulation with long name")
//  void testCreateSimulationLongName() throws Exception {
//    String longName = "ThisNameIsDefinitelyWayTooLongForTheSimulationServiceToAcceptItShouldFail";
//    CreateSimulationDTO dto = new CreateSimulationDTO(
//            longName,
//            6000,
//            111,
//            555,
//            10,
//            true,
//            LocalDateTime.now()
//    );
//    String body = objectMapper.writeValueAsString(dto);
//    mockMvc.perform(
//                    post("/simulations/users/{userId}", "someUserId")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(body)
//            )
//            .andExpect(status().isBadRequest());
//  }
//
//  @Test
//  @DisplayName("Test creating a simulation with short name")
//  void testCreateSimulationShortName() throws Exception {
//    String shortName = "Yo";
//    CreateSimulationDTO dto = new CreateSimulationDTO(
//            shortName,
//            6000,
//            111,
//            555,
//            10,
//            true,
//            LocalDateTime.now()
//    );
//    String content = objectMapper.writeValueAsString(dto);
//    mockMvc.perform(
//                    post("/simulations/users/{userId}", "someUserId")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(content)
//            )
//            .andExpect(status().isBadRequest());
//  }
//
//  @Test
//  @DisplayName("Test creating a simulation with special characters in name")
//  void testCreateSimulationSpecialCharsName() {
//    CreateSimulationDTO createSimulationDTO = new CreateSimulationDTO(
//            "Name@#$$%^&*",
//            6000,
//            111,
//            555,
//            10,
//            true,
//            testDate
//    );
//    assertDoesNotThrow(
//            () -> simulationService.createSimulation(createSimulationDTO, "User123"),
//            "Should handle names with special characters"
//    );
//
//  }
//
//  @Test
//  @DisplayName("Test creating a simulation with min boundary name length")
//  void testCreateSimulationMinNameLength() {
//    String maxName = "a".repeat(3);
//    CreateSimulationDTO dto = new CreateSimulationDTO(
//            maxName,
//            6000,
//            111,
//            555,
//            10,
//            true,
//            testDate
//    );
//    assertDoesNotThrow(
//            () -> simulationService.createSimulation(dto, "User123"),
//            "Should not throw any exception for max valid name length"
//    );
//  }
//
//
//  @Test
//  @DisplayName("Test creating a simulation with max boundary name length")
//  void testCreateSimulationMaxNameLength() {
//    String maxName = "a".repeat(50);
//    CreateSimulationDTO dto = new CreateSimulationDTO(
//            maxName,
//            6000,
//            111,
//            555,
//            10,
//            true,
//            testDate
//    );
//    assertDoesNotThrow(
//            () -> simulationService.createSimulation(dto, "User123"),
//            "Should not throw any exception for max valid name length"
//    );
//  }
//
//  @Test
//  @DisplayName("Test creating a simulation with max integer values for income and cost")
//  void testCreateSimulationMaxIntValues() {
//    CreateSimulationDTO dto = new CreateSimulationDTO(
//            "ValidName",
//            Integer.MAX_VALUE,
//            Integer.MAX_VALUE,
//            555,
//            10,
//            true,
//            testDate
//    );
//    assertDoesNotThrow(
//            () -> simulationService.createSimulation(dto, "User123"),
//            "Should handle maximum integer values for income and cost without throwing exceptions"
//    );
//  }
//
//  @Test
//  @DisplayName("Test creating a valid simulation")
//  void testCreateSimulationValid() {
//    try {
//      Simulation sim = simulationService.createSimulation(createSimDTO, "User123");
//      assertAll(
//              () -> assertEquals(createSimDTO.name(), sim.getName(), "The name doesn't match"),
//              () -> assertEquals(createSimDTO.averageIncome(), sim.getAverageIncome(), "The average income doesn't match"),
//              () -> assertEquals(createSimDTO.commuteCost(), sim.getCommuteCost(), "The commute cost doesn't match"),
//              () -> assertEquals(createSimDTO.population(), sim.getPopulation(), "The population doesn't match"),
//              () -> assertEquals(createSimDTO.isPublic(), sim.isPublic(), "The public status doesn't match")
////        () -> assertEquals(createSimDTO.creationDate(), sim.getCreationDate(), "The creation date doesn't match")
//      );
//    } catch (UserNotFoundException | UnauthorizedUserException e) {
//      fail("Unexpected exception thrown: " + e.getMessage());
//    }
//  }
//
//  @Test
//  @DisplayName("Test creating a simulation with null DTO")
//  void testCreateSimulationNullDTO() {
//    assertThrows(
//            InvalidSimulationException.class,
//            () -> simulationService.createSimulation(null, user.getId())
//    );
//  }
//
//  @Test
//  @DisplayName("Test creating a simulation with valid inputs")
//  void testCreateSimulationWithValidInputs() {
//    CreateSimulationDTO createSimulationDTO = new CreateSimulationDTO(
//            "ValidName",
//            1000,
//            100,
//            500,
//            10,
//            true,
//            testDate
//    );
//    assertDoesNotThrow(
//            () -> simulationService.createSimulation(createSimulationDTO, "User123"),
//            "Should not throw any exception for valid parameters"
//    );
//  }
//
//  @Test
//  @DisplayName("Test retrieving a simulation by valid ID")
//  void testGetSimulationByIdValid() {
//    Simulation createdSim = simulationService.createSimulation(
//            createSimDTO,
//            user.getId()
//    );
//    Simulation retrievedSim = simulationService.getSimulation(createdSim.getId());
//    assertEquals(createdSim, retrievedSim);
//  }
//
//
//  @Test
//  @DisplayName("Test retrieving a simulation by invalid ID")
//  void testGetSimulationByIdInvalid() {
//    Simulation createdSim = simulationService.createSimulation(
//            createSimDTO,
//            user.getId()
//    );
//    assertThrows(
//            SimulationNotFoundException.class,
//            () -> simulationService.getSimulation("invalidId")
//    );
//  }
//
//  @Test
//  @DisplayName("Test updating a simulation successfully")
//  void testUpdateSimulationSuccess() {
//    final var createdSim = simulationService.createSimulation(
//            createSimDTO,
//            user.getId()
//    );
//    final var updateDTO = new CreateSimulationDTO(
//            "SIM 2",
//            300,
//            222,
//            1000,
//            12,
//            false,
//            testDate
//    );
//    final var updatedSim = simulationService.updateSimulation(updateDTO, createdSim.getId());
//    assertAll("Check updated simulation parameters",
//            () -> assertEquals(updatedSim.getName(), updateDTO.name()),
//            () -> assertEquals(updatedSim.getAverageIncome(), updateDTO.averageIncome()),
//            () -> assertEquals(updatedSim.getCommuteCost(), updateDTO.commuteCost()),
//            () -> assertEquals(updatedSim.getPopulation(), updateDTO.population()),
//            () -> assertEquals(updatedSim.isPublic(), updateDTO.isPublic()),
//            () -> assertEquals(updatedSim.getCitySize(), updateDTO.citySize())
//    );
//  }
//
//  @Test
//  @DisplayName("Test updating non-existing simulation")
//  void testUpdateNonExistingSimulation() {
//    final var createdSim = simulationService.createSimulation(
//            createSimDTO,
//            user.getId()
//    );
//    final var updateDTO = new CreateSimulationDTO(
//            "SIM 2",
//            300,
//            222,
//            1000,
//            12,
//            false,
//            testDate
//    );
//    assertThrows(
//            SimulationNotFoundException.class,
//            () -> simulationService.updateSimulation(updateDTO, "invalidId")
//    );
//  }
//
//  @Test
//  @DisplayName("Test deleting a non-existing simulation")
//  void testDeleteNonExistingSimulation() {
//    final var createdSim = simulationService.createSimulation(
//            createSimDTO,
//            user.getId()
//    );
//    assertThrows(
//            SimulationNotFoundException.class,
//            () -> simulationService.deleteSimulation("invalidId")
//    );
//  }
//
//  @Test
//  @DisplayName("Test retrieving all simulations with just one simulation in the DB")
//  void testGetAllSimulationsWithOneSimulation() {
//    final var createdSim = simulationService.createSimulation(
//            createSimDTO,
//            user.getId()
//    );
//    List<Simulation> simulations = simulationService.getSimulations();
//
//    assertEquals(1, simulations.size());
//    assertEquals(createdSim, simulations.getFirst());
//  }
//}
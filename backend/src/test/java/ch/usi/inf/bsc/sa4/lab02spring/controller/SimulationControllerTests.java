//package ch.usi.inf.bsc.sa4.lab02spring.controller;
//
//import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.CreateSimulationDTO;
//import ch.usi.inf.bsc.sa4.lab02spring.controller.dto.SimulationDTO;
//import ch.usi.inf.bsc.sa4.lab02spring.model.Simulation;
//import ch.usi.inf.bsc.sa4.lab02spring.service.SimulationService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@DisplayName("The simulation controller")
//public class SimulationControllerTests {
//
//  private final LocalDateTime testDate = LocalDateTime.now();
//  @Autowired
//  private MockMvc mockMvc;
//  @MockBean
//  private SimulationService simulationService;
//  @Autowired
//  private ObjectMapper objectMapper;
//  private Simulation simulation;
//
//  @BeforeAll
//  public static void setupAll() {
//  }
//
//  @BeforeEach
//  public void setup() {
//    simulation = new Simulation("1234", "SimulationNr1", 777, 111, true, "MarlonBrando", testDate);
//    new SimulationDTO(simulation);
//  }
//
//  @DisplayName("the simulation controller should return a list of simulations")
//  @Test
//  void testGetUserSimulationsSuccess() throws Exception {
//    // Assuming 'MarlonBrando' is the userId you want to test
//    when(simulationService.getUserSimulations("MarlonBrando")).thenReturn(List.of(simulation));
//
//    // Corrected URL to match the controller
//    mockMvc.perform(get("/simulations/users/MarlonBrando"))
//            .andExpect(status().isOk());
//  }
//
//  @DisplayName("the simulation controller should return an error when the simulation is not found")
//  @Test
//  void testGetSimulationNotFound() throws Exception {
//    when(simulationService.getUserSimulations(anyString())).thenReturn(Collections.emptyList());
//
//    mockMvc.perform(get("/MarlonBrando/simulations/FakeID"))
//            .andExpect(status().isNotFound());
//  }
//
//  @DisplayName("SimulationDTO should correctly map fields from Simulation model")
//  @Test
//  void testSimulationDtoConstructorTest() {
//    Simulation simulation;
//    simulation = new Simulation("1234", "SimulationNr1", 777, 111, true, "MarlonBrando", testDate);
//    SimulationDTO dto = new SimulationDTO(simulation);
//
//    assertAll("Ensure mapping correctness",
//            () -> assertEquals("1234", dto.id()),
//            () -> assertEquals("SimulationNr1", dto.name()),
//            () -> assertEquals(777, dto.averageIncome()),
//            () -> assertEquals(111, dto.transportCost()),
//            () -> assertTrue(dto.isPublic()),
//            () -> assertEquals("MarlonBrando", dto.userId()),
//            () -> assertNotNull(dto.name())
//    );
//  }
//
//  @DisplayName("should successfully create a simulation and return it")
//  @Test
//  void testCreateSimulationSuccess() throws Exception {
//    CreateSimulationDTO createDto = new CreateSimulationDTO("NewSimulation", 1000, 50, true, LocalDateTime.now());
//    Simulation newSimulation = new Simulation("12345", "NewSimulation", 1000, 50, false, "MarlonBrando", LocalDateTime.now());
//
//    when(simulationService.createSimulation(any(CreateSimulationDTO.class), eq("MarlonBrando"))).thenReturn(newSimulation);
//
//    mockMvc.perform(MockMvcRequestBuilders.post("/simulations/users/MarlonBrando")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(createDto)))
//            .andExpect(MockMvcResultMatchers.status().isCreated())
//            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("12345"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("NewSimulation"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.isPublic").value(false));
//  }
//
//  @DisplayName("should return bad request when numerical inputs are zero or negative")
//  @Test
//  void testCreateSimulationNegativeInputs() throws Exception {
//    when(simulationService.createSimulation(any(CreateSimulationDTO.class), anyString()))
//            .thenReturn(new Simulation("12345", "NewSimulation", 1000, 50, 300, false, "MarlonBrando", LocalDateTime.now(), 5));
//
//    mockMvc.perform(post("/simulations/users/MarlonBrando")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(testDate)))
//            .andExpect(status().isBadRequest());
//  }
//
//
//  @DisplayName("Failed Update Due to Invalid Name Should Throw Exceptions")
//  @Test
//  void testUpdateSimulationWithInvalidName() {
//    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Simulation("1234", "Ab", 1, 100, true, "MarlonBrando", LocalDateTime.now()));
//    assertTrue(exception.getMessage().contains("name must be of 3 - 50 characters"),
//            "Expected exception message to indicate invalid name");
//  }
//
//  @DisplayName("Failed Update Due to Invalid Income Should Throw Exceptions")
//  @Test
//  void testUpdateSimulationWithInvalidAverageIncome() {
//    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Simulation("1234", "Abc", -1, 100, true, "MarlonBrando", LocalDateTime.now()));
//    assertTrue(exception.getMessage().contains("wage must be positive or zero"),
//            "Expected exception message to indicate invalid average income");
//  }
//
//
//  @DisplayName("Failed Update Due to Invalid Commute Cost Should Throw Exceptions")
//  @Test
//  void testUpdateSimulationWithInvalidCommuteCost() {
//    Exception exception = assertThrows(IllegalArgumentException.class, () -> new Simulation("1234", "Abc", 100, -100, true, "MarlonBrando", LocalDateTime.now()));
//    assertTrue(exception.getMessage().contains("transportCost must be positive or zero"),
//            "Expected exception message to indicate invalid commute cost");
//  }
//
//  @DisplayName("Failed Update Due to Invalid Population Should Throw Exceptions")
//  @Test
//  void testUpdateSimulationWithInvalidPopulation() {
//    Exception exception = assertThrows(IllegalArgumentException.class, () -> new Simulation("1234", "Abc", 100, 100, -50, true, "MarlonBrando", LocalDateTime.now(), 10));
//    assertTrue(exception.getMessage().contains("population must be positive or zero"),
//            "Expected exception message to indicate invalid population");
//  }
//
//  @DisplayName("Failed Update Due to Invalid City Size Should Throw Exceptions")
//  @Test
//  void testUpdateSimulationWithInvalidCitySize() {
//    Exception exception = assertThrows(IllegalArgumentException.class, () -> new Simulation("1234", "Abc", 100, 100, 50, true, "MarlonBrando", LocalDateTime.now(), -1));
//    assertTrue(exception.getMessage().contains("citySize must be positive"),
//            "Expected exception message to indicate invalid city size");
//  }
//
//  @Test
//  void testIsValidSimulationDto() {
//    // Create a valid SimulationDTO
//    SimulationDTO validDto = new SimulationDTO(
//            "1",
//            "Test Simulation",
//            50000,
//            1000,
//            1000,
//            100,
//            true,
//            "user1",
//            LocalDateTime.now(),
//            new City()
//    );
//
//    assertTrue(SimulationController.isValidSimulationDto(validDto));
//
//    // Create an invalid SimulationDTO with null values
//    SimulationDTO nullDto = new SimulationDTO(
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null
//    );
//    assertFalse(SimulationController.isValidSimulationDto(nullDto));
//
//    // Create an invalid SimulationDTO with empty name
//    SimulationDTO emptyNameDto = new SimulationDTO(
//            "2",
//            "",
//            50000,
//            1000,
//            1000,
//            100,
//            true,
//            "user1",
//            LocalDateTime.now(),
//            new City()
//    );
//    assertFalse(SimulationController.isValidSimulationDto(emptyNameDto));
//  }
//
//  @Test
//  void testIsValidCreationSimulationDto() {
//    // Create a valid CreateSimulationDTO
//    CreateSimulationDTO validDto = new CreateSimulationDTO(
//            "Test Simulation",
//            50000,
//            1000,
//            1000,
//            100,
//            true,
//            LocalDateTime.now()
//    );
//
//    assertTrue(SimulationController.isValidCreationSimulationDto(validDto));
//
//    // Create an invalid CreateSimulationDTO with null values
//    CreateSimulationDTO nullDto = new CreateSimulationDTO(
//            null,
//            null,
//            null,
//            null,
//            null,
//            null,
//            null
//    );
//    assertFalse(SimulationController.isValidCreationSimulationDto(nullDto));
//
//    // Create an invalid CreateSimulationDTO with empty name
//    CreateSimulationDTO emptyNameDto = new CreateSimulationDTO(
//            "",
//            50000,
//            1000,
//            1000,
//            100,
//            true,
//            LocalDateTime.now()
//    );
//    assertFalse(SimulationController.isValidCreationSimulationDto(emptyNameDto));
//  }
//}
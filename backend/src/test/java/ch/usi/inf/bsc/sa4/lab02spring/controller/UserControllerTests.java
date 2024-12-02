package ch.usi.inf.bsc.sa4.lab02spring.controller;

import ch.usi.inf.bsc.sa4.lab02spring.model.User;
import ch.usi.inf.bsc.sa4.lab02spring.service.UserService;
import ch.usi.inf.bsc.sa4.lab02spring.utils.exceptions.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("The simulation controller")
class UserControllerTests {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  private OAuth2AuthenticationToken createOAuth2Token() {
    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
    OAuth2User principal = new DefaultOAuth2User(authorities, Map.of("sub", "1", "name", "Test User"), "name");
    return new OAuth2AuthenticationToken(principal, authorities, "client");
  }


  @Test
  void testGetAllUsersSuccess() throws Exception {
    List<User> users = List.of(new User("1", "testUser", "Test User", "test@example.com"));
    when(userService.getAllUsers()).thenReturn(users);

    mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].username", is("testUser")));
  }

  @Test
  void testGetAllUsersEmpty() throws Exception {
    // Return an empty list when userService.getAll() is called
    List<User> users = List.of();  // An empty list
    when(userService.getAllUsers()).thenReturn(users);

    // Perform GET request
    mockMvc.perform(get("/users"))
            .andExpect(status().isOk())  // Expecting the HTTP status to be 200 OK
            .andExpect(jsonPath("$", hasSize(0)))  // Expecting the JSON array to be empty
            .andExpect(jsonPath("$").isEmpty());  // Confirming that the result is indeed an empty array
  }

  @Test
  void testGetUserByIdSuccess() throws Exception {
    OAuth2AuthenticationToken token = this.createOAuth2Token();
    when(userService.getUser("1")).thenReturn(new User("1", "testUser", "Test User", "test@example.com"));

    mockMvc.perform(get("/users/1").principal(token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is("testUser")));
  }

  @Test
  void testGetUserByIdNotFound() throws Exception {
    when(userService.getUser("2")).thenThrow(UserNotFoundException.class);

    mockMvc.perform(get("/users/2"))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()));
  }

  @Test
  void testGetUserByUsernameSuccess() throws Exception {
    OAuth2AuthenticationToken token = this.createOAuth2Token();
    when(userService.getUser("1")).thenReturn((new User("1", "testUser", "Test User", "test@example.com")));
    mockMvc.perform(get("/users/1").principal(token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is("testUser")));
  }

  @Test
  void testGetUserByUsernameNotFound() throws Exception {
    when(userService.getUser("2")).thenThrow(UserNotFoundException.class);
    mockMvc.perform(get("/users/2"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testGetUserByEmailSuccess() throws Exception {
    OAuth2AuthenticationToken token = this.createOAuth2Token();
    when(userService.getUser("test@example.com")).thenReturn(new User("1", "testUser", "Test User", "test@example.com"));
    mockMvc.perform(get("/users/test@example.com").principal(token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is("testUser")));
  }

  @Test
  void testGetUserByEmailNotFound() throws Exception {
    when(userService.getUser("test@example.com")).thenThrow(UserNotFoundException.class);
    mockMvc.perform(get("/users/test@example.com"))
            .andExpect(status().isNotFound());
  }

  @Test
  void testCreateNewUserFromOAuthNoAuthenticationFailure() throws Exception {
    mockMvc.perform(get("/users/login"))
            .andExpect(status().isForbidden());
  }
}

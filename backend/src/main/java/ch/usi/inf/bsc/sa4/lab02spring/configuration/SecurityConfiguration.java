package ch.usi.inf.bsc.sa4.lab02spring.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.requestMatchers("/users/login").permitAll()
                    .requestMatchers("users/**").authenticated()
                    .requestMatchers("**").permitAll())
            .oauth2Login(oauth2 -> oauth2
                    .defaultSuccessUrl("http://localhost:3000/", true)
            )
            .build();
  }

}

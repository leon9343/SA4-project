package ch.usi.inf.bsc.sa4.lab02spring.configuration;

import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configuration class for validation.
 */
@Configuration
@ComponentScan({"ch.usi.inf.bsc.sa4.lab02spring.model"})
public class ValidationConfig {

  /**
   * Create a validator.
   *
   * @return the validator
   */
  @Bean
  public Validator validator() {
    return new LocalValidatorFactoryBean();
  }

  /**
   * Create a validating mongo event listener.
   *
   * @return the validating mongo event listener
   */
  @Bean
  public ValidatingMongoEventListener validatingMongoEventListener() {
    return new ValidatingMongoEventListener(validator());
  }

}

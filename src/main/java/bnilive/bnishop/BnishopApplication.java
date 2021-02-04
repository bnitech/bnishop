package bnilive.bnishop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BnishopApplication {

  public static void main(String[] args) {
    SpringApplication.run(BnishopApplication.class, args);
  }

  @Bean
  Hibernate5Module hibernate5Module() {

    Hibernate5Module hibernate5Module = new Hibernate5Module();
//    hibernate5Module.configure(Feature.FORCE_LAZY_LOADING, true);
    return hibernate5Module;
  }
}

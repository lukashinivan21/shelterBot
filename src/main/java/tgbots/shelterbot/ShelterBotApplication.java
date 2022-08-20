package tgbots.shelterbot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class ShelterBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShelterBotApplication.class, args);
    }

}

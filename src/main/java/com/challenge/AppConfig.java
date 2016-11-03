package com.challenge;

import com.challenge.elevator.CallSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by david on 10/30/16.
 */
@Configuration
public class AppConfig {
    @Bean
    CallSystem callSystem(@Value("${elevator.services.stories}") int stories,
                          @Value("${elevator.services.elevators}") int elevators) {
        return new CallSystem(elevators, stories);
    }
}

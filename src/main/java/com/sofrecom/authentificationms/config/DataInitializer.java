package com.sofrecom.authentificationms.config;

import com.sofrecom.authentificationms.service.VehicleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class DataInitializer {

    /*@Bean
    CommandLineRunner initDatabase(VehicleService vehicleService) {
        return args -> {
            try {
                vehicleService.importVehiclesFromCsv("D:\\project\\authentificationMS\\src\\main\\resources\\static\\voiture.csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }*/

}

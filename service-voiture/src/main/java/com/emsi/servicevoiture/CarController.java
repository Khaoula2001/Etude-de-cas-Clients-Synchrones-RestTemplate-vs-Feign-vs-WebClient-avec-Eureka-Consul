package com.emsi.servicevoiture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @GetMapping("/byClient/{clientId}")
    public Car getCarByClient(@PathVariable Long clientId) {
        // Simulation of database access or complex processing
        try {
            Thread.sleep(20); // 20ms delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Mock response
        Car car = new Car();
        car.setId(10L);
        car.setMarque("Toyota");
        car.setModele("Yaris");
        car.setClientId(clientId);

        return car;
    }
}

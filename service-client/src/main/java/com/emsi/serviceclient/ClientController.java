package com.emsi.serviceclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private CarClient carClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/{id}/car/rest")
    public Car getCarRest(@PathVariable Long id) {
        // Use LoadBalanced RestTemplate
        String url = "http://SERVICE-VOITURE/api/cars/byClient/" + id;
        return restTemplate.getForObject(url, Car.class);
    }

    @GetMapping("/{id}/car/feign")
    public Car getCarFeign(@PathVariable Long id) {
        return carClient.getCarByClient(id);
    }

    @GetMapping("/{id}/car/webclient")
    public Car getCarWebClient(@PathVariable Long id) {
        // Use LoadBalanced WebClient
        return webClientBuilder.build()
                .get()
                .uri("http://SERVICE-VOITURE/api/cars/byClient/" + id)
                .retrieve()
                .bodyToMono(Car.class)
                .block();
    }
}

package org.example.dataingestionstreets.controller;

import lombok.AllArgsConstructor;
import org.example.dataingestionstreets.model.Street;
import org.example.dataingestionstreets.service.StreetsProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@AllArgsConstructor
@RestController
public class StreetController {

    private final StreetsProducerService streetsService;

    @GetMapping("/")
    public String home() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("/streets")
    public List<Street> getStreetsInCity(@RequestParam String cityName) {
        // קריאה לפונקציה בשירות והחזרת התוצאה
        return streetsService.getStreetsInCity(cityName);
    }
}

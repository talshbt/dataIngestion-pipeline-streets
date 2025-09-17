package org.example.dataingestionstreets.controller;

import lombok.AllArgsConstructor;
import org.example.dataingestionstreets.service.StreetsProducerService;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class StreetController {

    private final StreetsProducerService streetsService;

    @PostMapping("/streets")
    public void getStreetsInCity(@RequestBody String cityName) {
        streetsService.getStreetsInCity(cityName);
    }
}

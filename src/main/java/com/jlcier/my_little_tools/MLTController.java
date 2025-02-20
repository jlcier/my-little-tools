package com.jlcier.my_little_tools;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MLTController {

    @PostMapping("/fuel")
    public ResponseEntity<?> fuelCalculator(@RequestBody @Valid FuelRequest request) {
        return ResponseEntity.ok(MLTService.getHowMuchToPay(request));
    }
}

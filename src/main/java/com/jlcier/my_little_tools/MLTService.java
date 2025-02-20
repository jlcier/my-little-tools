package com.jlcier.my_little_tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MLTService {
    public static String getHowMuchToPay(FuelRequest fuel) {
        double fuelRemaining = (fuel.getKmRemaining() * fuel.getLitersFor100Km()) / 100;
        double fuelRequired = fuel.getCapacity() - fuelRemaining;
        double value = Math.floor(fuelRequired * fuel.getLiterCost()/10)*10;
        return "$" + String.valueOf(value).replace(".0", "");
    }
}

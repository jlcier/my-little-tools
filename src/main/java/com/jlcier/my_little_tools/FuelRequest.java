package com.jlcier.my_little_tools;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelRequest {
    @NotNull(message = "l/100km is required")
    private Double litersFor100Km;
    @NotNull(message = "liter cost is required")
    private Double literCost;
    @NotNull(message = "km remaining is required")
    private Double kmRemaining;
    @NotNull(message = "tank capacity is required")
    private Double capacity;
}

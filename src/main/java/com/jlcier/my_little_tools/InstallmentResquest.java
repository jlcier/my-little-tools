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
public class InstallmentResquest {
    @NotNull(message = "annual inflation rate is required")
    private Double annualInflationRate;
    @NotNull(message = "annual return rate is required")
    private Double annualReturnRate;
    @NotNull(message = "cash value is required")
    private Double cashValue;
    @NotNull(message = "installment value is required")
    private Double totalInstallmentValue;
    @NotNull(message = "number of installments is required")
    private Integer numberOfInstallments;
}

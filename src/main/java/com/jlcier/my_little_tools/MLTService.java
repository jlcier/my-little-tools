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

    public static String compare(InstallmentResquest installment) {
        double annualIncome = (1 + installment.getAnnualReturnRate()) / (1 + installment.getAnnualInflationRate()) - 1;
        double monthlyIncome = Math.pow(1 + annualIncome, 1.0 / 12) - 1;
        double installmentValue = installment.getTotalInstallmentValue() / installment.getNumberOfInstallments();
        double presentValue = 0.0;
        for (int i = 1; i <= installment.getNumberOfInstallments(); i++) {
            presentValue += installmentValue / Math.pow(1 + monthlyIncome, i);
        }
        String bestOption;
        if (presentValue < installment.getCashValue()) {
            bestOption = "Buy in installments";
        } else {
            bestOption = "Buy in cash";
        }
        return String.format("Cash value: $ %.2f%nPresent value: $ %.2f%nBest option: %s",
                installment.getCashValue(), presentValue, bestOption);
    }
}

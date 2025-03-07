package com.jlcier.my_little_tools;

public class NumeroPorExtenso {

    private static final String[] UNIDADES = { "", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove" };
    private static final String[] DEZ_A_DEZENOVE = { "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove" };
    private static final String[] DEZENAS = { "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa" };
    private static final String[] CENTENAS = { "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos" };


    public static String converterValorPorExtenso(String valorMonetario) {
        try {
            String valorLimpo = valorMonetario.replaceAll("[^\\d,]", "").replace(",", ".");
            double valor = Double.parseDouble(valorLimpo);

            int valorInteiro = (int) valor;
            return converterNumeroParaExtenso(valorInteiro) + " reais";
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de valor inválido.");
        }
    }

    private static String converterNumeroParaExtenso(int numero) {
        if (numero == 0) {
            return "zero";
        }
        if (numero == 100) {
            return "cem";
        }

        StringBuilder extenso = new StringBuilder();
        if (numero >= 1000) {
            extenso.append(converterMilhar(numero / 1000)).append(" ");
            numero %= 1000;
        }
        if (numero >= 100) {
            if (!extenso.isEmpty() && numero % 100 == 0) {
                extenso.append("e ");
            }
            if (numero / 100 == 1 && numero % 100 == 0) {
                extenso.append("cem").append(" ");
            } else {
                extenso.append(CENTENAS[numero / 100]).append(" ");
            }
            numero %= 100;
        }
        if (!extenso.isEmpty() && numero > 0) {
            extenso.append("e ");
        }
        if (numero >= 20) {
            extenso.append(DEZENAS[numero / 10]).append(" ");
            numero %= 10;
        } else if (numero >= 10) {
            extenso.append(DEZ_A_DEZENOVE[numero - 10]).append(" ");
            numero = 0;
        }
        if (numero > 0) {
            extenso.append(UNIDADES[numero]).append(" ");
        }

        return extenso.toString().trim().replaceAll(" +", " ").replaceAll(" $", "").replace("  ", " e ");
    }

    private static String converterMilhar(int numero) {
        if (numero == 1) {
            return "mil";
        }
        return converterNumeroParaExtenso(numero) + " mil";
    }
}

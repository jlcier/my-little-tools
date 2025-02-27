package com.jlcier.my_little_tools;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static void markdownToPDF(String markdown, String outputPath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();
            String[] lines = markdown.split("\n");

            for (String line : lines) {
                line = line.trim();

                if (line.matches("^#{1,3} .*")) {
                    int level = line.lastIndexOf("#") + 1;
                    Font font = new Font(Font.FontFamily.HELVETICA, 16 - (level * 2), Font.BOLD);
                    document.add(new Paragraph(line.replaceAll("^#{1,3} ", ""), font));

                } else if (line.matches("^\\*\\*.*\\*\\*$")) {
                    document.add(new Paragraph(line.replaceAll("\\*\\*(.*?)\\*\\*", "$1"), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

                } else if (line.matches("^\\*.*\\*$")) {
                    document.add(new Paragraph(line.replaceAll("\\*(.*?)\\*", "$1"), new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC)));

                } else if (line.matches("^(-|\\*) .*")) {
                    document.add(new Paragraph("• " + line.substring(2), new Font(Font.FontFamily.HELVETICA, 12)));

                } else if (line.matches("^\\d+\\. .*")) {
                    document.add(new Paragraph(line, new Font(Font.FontFamily.HELVETICA, 12)));

                } else if (line.matches("^```.*")) {
                    document.add(new Paragraph(line.replaceAll("```", ""), new Font(Font.FontFamily.COURIER, 12)));

                } else if (line.matches("\\[.*?\\]\\(.*?\\)")) {
                    Matcher matcher = Pattern.compile("\\[(.*?)\\]\\((.*?)\\)").matcher(line);
                    if (matcher.find()) {
                        Anchor anchor = new Anchor(matcher.group(1), new Font(Font.FontFamily.HELVETICA, 12, Font.UNDERLINE, BaseColor.BLUE));
                        anchor.setReference(matcher.group(2));
                        document.add(anchor);
                    }

                } else if (line.startsWith("|")) {
                    String[] columns = line.split("\\|");
                    PdfPTable table = new PdfPTable(columns.length - 2);
                    for (int i = 1; i < columns.length - 1; i++) {
                        table.addCell(columns[i].trim());
                    }
                    document.add(table);

                } else {
                    document.add(new Paragraph(line, new Font(Font.FontFamily.HELVETICA, 12)));
                }
            }

            document.close();
            System.out.println("Success: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

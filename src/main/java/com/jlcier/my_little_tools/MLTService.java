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
            int fontSize = 12;
            boolean lineCode = false;

            for (String rawLine : lines) {
                if (rawLine.trim().isEmpty()) {
                    document.add(new Paragraph(" "));
                    continue;
                }

                int indentLevel = 0;
                while (rawLine.startsWith("\t")) {
                    indentLevel++;
                    rawLine = rawLine.substring(1);
                }
                while (rawLine.startsWith("    ")) {
                    indentLevel++;
                    rawLine = rawLine.substring(4);
                }

                String line = rawLine.trim();

                if (line.matches("^```.*")) {
                    lineCode = !lineCode;
                    Paragraph p = new Paragraph(line.replaceAll("```", ""), new Font(Font.FontFamily.COURIER, fontSize));
                    document.add(p);

                } else if (lineCode) {
                    Paragraph p = new Paragraph(line, new Font(Font.FontFamily.COURIER, fontSize));
                    document.add(p);

                } else if (line.matches("^#{1,5} .*")) {
                    int level = line.lastIndexOf("#") + 1;
                    if (level > 5) {
                        level = 5;
                    }
                    Font font = new Font(Font.FontFamily.HELVETICA, (fontSize * 2) - (level * 2), Font.BOLD);
                    Paragraph p = new Paragraph(line.replaceAll("^#{1,5} ", ""), font);
                    document.add(p);

                } else if (line.matches("^\\*\\*.*\\*\\*$")) {
                    Paragraph p = new Paragraph(line.replaceAll("\\*\\*(.*?)\\*\\*", "$1"), new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD));
                    document.add(p);

                } else if (line.matches("^\\*.*\\*$")) {
                    Paragraph p = new Paragraph(line.replaceAll("\\*(.*?)\\*", "$1"), new Font(Font.FontFamily.HELVETICA, fontSize, Font.ITALIC));
                    document.add(p);

                } else if (line.matches("^(-|\\*) .*")) {
                    Paragraph p = new Paragraph("• " + line.substring(2), new Font(Font.FontFamily.HELVETICA, fontSize));
                    p.setIndentationLeft(indentLevel * 20f);
                    document.add(p);

                } else if (line.matches("^\\d+\\. .*")) {
                    Paragraph p = new Paragraph(line, new Font(Font.FontFamily.HELVETICA, fontSize));
                    p.setIndentationLeft(indentLevel * 20f);
                    document.add(p);

                } else if (line.matches("\\[.*?\\]\\(.*?\\)")) {
                    Matcher matcher = Pattern.compile("\\[(.*?)\\]\\((.*?)\\)").matcher(line);
                    if (matcher.find()) {
                        Anchor anchor = new Anchor(matcher.group(1), new Font(Font.FontFamily.HELVETICA, fontSize, Font.UNDERLINE, BaseColor.BLUE));
                        anchor.setReference(matcher.group(2));
                        Paragraph p = new Paragraph();
                        p.add(anchor);
                        p.setIndentationLeft(indentLevel * 20f);
                        document.add(p);
                    }

                } else if (line.startsWith("|")) {
                    if (!line.contains("| :-") && !line.contains("-: |")) {
                        String[] columns = line.split("\\|");
                        PdfPTable table = new PdfPTable(columns.length - 1);
                        for (int i = 1; i < columns.length; i++) {
                            table.addCell(columns[i].trim());
                        }
                        document.add(table);
                    }

                } else {
                    Paragraph p = new Paragraph(line, new Font(Font.FontFamily.HELVETICA, fontSize));
                    p.setIndentationLeft(indentLevel * 20f);
                    document.add(p);
                }
            }

            document.close();
            System.out.println("Success: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String numbersInFull(String value) {
        return NumeroPorExtenso.converterValorPorExtenso(value);
    }
}

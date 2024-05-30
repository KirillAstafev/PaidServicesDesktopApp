package com.paidservices.paidservicesdesktopapp.financial.report.impl;

import com.paidservices.paidservicesdesktopapp.financial.analytics.model.FinancialAnalytics;
import com.paidservices.paidservicesdesktopapp.financial.report.ReportCreator;
import com.paidservices.paidservicesdesktopapp.model.MedicalService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

public class PdfReportCreator implements ReportCreator {
    private int xOffset = 100;
    private int yOffset = 700;

    @Override
    public void createReport(FinancialAnalytics reportData, String absoluteFilePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = createContentStream(document, page);

            printReportHeader(reportData, contentStream);

            contentStream.setFont(PDType0Font.load(document,
                    new File("C:\\Windows\\Fonts\\times.ttf")), 13);

            printServiceSumData(reportData, contentStream);
            printTotalSumData(reportData, contentStream);

            contentStream.close();

            document.save(absoluteFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PDPageContentStream createContentStream(PDDocument document, PDPage page) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType0Font.load(document,
                    new File("C:\\Windows\\Fonts\\times.ttf")), 18);

            return contentStream;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void printReportHeader(FinancialAnalytics reportData, PDPageContentStream contentStream) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(xOffset, yOffset);
        contentStream.showText("Отчёт за период с " + reportData
                .getStartDate()
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                + " по " + reportData
                .getEndDate()
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        contentStream.endText();

        yOffset -= 60;
    }

    private void printServiceSumData(FinancialAnalytics reportData, PDPageContentStream contentStream) throws IOException {
        Map<MedicalService, BigDecimal> medicalServiceSum = reportData.getMedicalServiceSum();

        medicalServiceSum.forEach((key, value) -> {
            try {
                contentStream.beginText();
                contentStream.newLineAtOffset(xOffset, yOffset);
                contentStream.showText(key.getName() + " - " +
                        value.toPlainString() + " р.");
                contentStream.endText();

                yOffset -= 40;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }

    private void printTotalSumData(FinancialAnalytics reportData, PDPageContentStream contentStream) {
        try {
            contentStream.beginText();
            contentStream.newLineAtOffset(xOffset, yOffset);
            contentStream.showText("Всего за отчётный период: " + reportData.getTotalSum().toPlainString()  + " р.");
            contentStream.endText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

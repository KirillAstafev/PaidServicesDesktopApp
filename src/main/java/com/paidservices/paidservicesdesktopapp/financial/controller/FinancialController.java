package com.paidservices.paidservicesdesktopapp.financial.controller;

import com.paidservices.paidservicesdesktopapp.financial.analytics.AnalyticsCreator;
import com.paidservices.paidservicesdesktopapp.financial.analytics.impl.AnalyticsCreatorImpl;
import com.paidservices.paidservicesdesktopapp.financial.analytics.model.FinancialAnalytics;
import com.paidservices.paidservicesdesktopapp.financial.report.ReportCreator;
import com.paidservices.paidservicesdesktopapp.financial.report.impl.PdfReportCreator;
import com.paidservices.paidservicesdesktopapp.model.MedicalService;
import com.paidservices.paidservicesdesktopapp.model.Person;
import com.paidservices.paidservicesdesktopapp.model.Staff;
import com.paidservices.paidservicesdesktopapp.model.Visitation;
import com.paidservices.paidservicesdesktopapp.webclient.client.WebClient;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Stream;

import static javafx.stage.FileChooser.ExtensionFilter;

public class FinancialController {
    private final WebClient client = WebClient.getInstance();
    private final ReportCreator reportCreator = new PdfReportCreator();
    private final AnalyticsCreator analyticsCreator = new AnalyticsCreatorImpl();

    public StackPane rootPane;
    public TableView<Visitation> financialInfoTable;
    public TableColumn<Visitation, String> patientColumn;
    public TableColumn<Visitation, String> staffColumn;
    public TableColumn<Visitation, String> serviceColumn;
    public TableColumn<Visitation, String> dateColumn;
    public TableColumn<Visitation, String> servicePriceColumn;
    public TableColumn<Visitation, String> serviceStatusColumn;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;

    @FXML
    public void initialize() {
        setCellValueFactories();

        client.getVisitations()
                .thenAccept(visitations -> {
                    Platform.runLater(() -> {
                        financialInfoTable.setItems(FXCollections.observableArrayList(visitations));
                    });
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> {
                        Notifications
                                .create()
                                .title("Ошибка!")
                                .text("Не удалось загрузить данные о доходах!")
                                .showError();
                    });

                    throw new RuntimeException(throwable);
                });
    }

    private void setCellValueFactories() {
        patientColumn.setCellValueFactory(param -> {
            Person patient = param.getValue().getPatient();
            return new SimpleStringProperty(String.format("%s %s %s",
                    patient.getLastName(),
                    patient.getFirstName(),
                    patient.getMiddleName() != null ? patient.getMiddleName() : ""));
        });

        staffColumn.setCellValueFactory(param -> {
            Staff staff = param.getValue().getStaff();
            return new SimpleStringProperty(String.format("%s %s.%s.",
                    staff.getPerson().getLastName(),
                    staff.getPerson().getFirstName().charAt(0),
                    staff.getPerson().getMiddleName() != null ? staff.getPerson().getMiddleName().charAt(0) : ""));
        });

        serviceColumn.setCellValueFactory(param -> {
            return new SimpleStringProperty(param.getValue().getMedicalService().getName());
        });

        dateColumn.setCellValueFactory(param -> {
            LocalDateTime dateTime = param.getValue().getDateTime();
            return new SimpleStringProperty(dateTime
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        });

        servicePriceColumn.setCellValueFactory(param -> {
            MedicalService service = param.getValue().getMedicalService();
            String priceString = String.format("%s р.", service.getPrice().toPlainString());

            return new SimpleStringProperty(priceString);
        });

        serviceStatusColumn.setCellValueFactory(param -> {
            if (param.getValue().getDateTime().isAfter(LocalDateTime.now()))
                return new SimpleStringProperty("В ожидании");
            else
                return new SimpleStringProperty("Оплачено");
        });
    }

    public void saveFinancialReportAction(ActionEvent actionEvent) {
        List<Visitation> reportData = applyDateFilters(financialInfoTable.getItems());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить отчёт");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("PDF", "*.pdf"),
                new ExtensionFilter("Все файлы", "*.*"));

        File selectedFile = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
        if (selectedFile != null) {
            FinancialAnalytics analysisData = analyticsCreator.createAnalytics(reportData);
            reportCreator.createReport(analysisData, selectedFile.getAbsolutePath());
        }
    }

    private List<Visitation> applyDateFilters(List<Visitation> reportData) {
        Stream<Visitation> reportDataStream = reportData.stream()
                .filter(visitation -> visitation.getDateTime().isBefore(LocalDateTime.now()));

        if (startDatePicker.getValue() != null) {
            reportDataStream = reportDataStream
                    .filter(visitation -> visitation
                            .getDateTime()
                            .toLocalDate()
                            .isAfter(startDatePicker.getValue()));
        }

        if (endDatePicker.getValue() != null) {
            reportDataStream = reportDataStream
                    .filter(visitation -> visitation
                            .getDateTime()
                            .toLocalDate()
                            .isBefore(endDatePicker.getValue()));
        }

        return reportDataStream.toList();
    }
}

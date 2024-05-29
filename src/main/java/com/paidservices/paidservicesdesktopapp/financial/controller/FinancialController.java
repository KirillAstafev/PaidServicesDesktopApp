package com.paidservices.paidservicesdesktopapp.financial.controller;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.controlsfx.control.Notifications;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class FinancialController {
    private final WebClient client = WebClient.getInstance();

    public TableView<Visitation> financialInfoTable;
    public TableColumn<Visitation, String> patientColumn;
    public TableColumn<Visitation, String> staffColumn;
    public TableColumn<Visitation, String> serviceColumn;
    public TableColumn<Visitation, String> dateColumn;
    public TableColumn<Visitation, String> servicePriceColumn;
    public TableColumn<Visitation, String> serviceStatusColumn;

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

    }
}

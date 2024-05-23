package com.paidservices.paidservicesdesktopapp.visitation.controller;

import com.paidservices.paidservicesdesktopapp.visitation.model.MedicalService;
import com.paidservices.paidservicesdesktopapp.visitation.model.Person;
import com.paidservices.paidservicesdesktopapp.visitation.model.Staff;
import com.paidservices.paidservicesdesktopapp.visitation.model.Visitation;
import com.paidservices.paidservicesdesktopapp.webclient.client.WebClient;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.controlsfx.control.Notifications;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class VisitationController {
    private final WebClient client = WebClient.getInstance();

    public TableView<Visitation> visitationTable;
    public TableColumn<Visitation, String> patientColumn;
    public TableColumn<Visitation, String> staffColumn;
    public TableColumn<Visitation, String> serviceColumn;
    public TableColumn<Visitation, String> dateTimeColumn;

    public Button addVisitationButton;
    public Button editVisitationButton;
    public Button deleteVisitationButton;

    @FXML
    public void initialize() {
        setCellValueFactories();

        client.getVisitations()
                .thenAccept(visitations -> {
                    Platform.runLater(() -> {
                        visitationTable.setItems(FXCollections.observableArrayList(visitations));
                    });
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> {
                        Notifications
                                .create()
                                .title("Ошибка!")
                                .text("Не удалось загрузить данные о посещениях!")
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
            return new SimpleStringProperty(String.format("%s %s.%s",
                    staff.getPerson().getLastName(),
                    staff.getPerson().getFirstName().charAt(0),
                    staff.getPerson().getMiddleName() != null ? staff.getPerson().getMiddleName().charAt(0) : ""));
        });

        serviceColumn.setCellValueFactory(param -> {
            MedicalService service = param.getValue().getMedicalService();
            return new SimpleStringProperty(service.getName());
        });

        dateTimeColumn.setCellValueFactory(param -> {
            LocalDateTime dateTime = param.getValue().getDateTime();
            return new SimpleStringProperty(dateTime
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        });
    }

    public void addVisitationAction(ActionEvent actionEvent) {

    }

    public void editVisitationAction(ActionEvent actionEvent) {

    }

    public void deleteVisitationAction(ActionEvent actionEvent) {

    }
}

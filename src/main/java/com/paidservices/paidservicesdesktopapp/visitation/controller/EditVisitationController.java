package com.paidservices.paidservicesdesktopapp.visitation.controller;

import com.paidservices.paidservicesdesktopapp.visitation.model.MedicalService;
import com.paidservices.paidservicesdesktopapp.visitation.model.Visitation;
import com.paidservices.paidservicesdesktopapp.visitation.view.cellfactory.MedicalServiceComboBoxCellFactory;
import com.paidservices.paidservicesdesktopapp.visitation.view.converter.MedicalServiceConverter;
import com.paidservices.paidservicesdesktopapp.webclient.client.WebClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class EditVisitationController {
    private final WebClient client = WebClient.getInstance();
    private Consumer<Visitation> controllerCallback;

    public TextField patientLastNameTextField;
    public TextField patientFirstNameTextField;
    public TextField patientMiddleNameTextField;
    public TextField patientSnilsTextField;
    public TextField patientPhoneNumberTextField;
    public ComboBox<MedicalService> medicalServiceComboBox;
    public DatePicker datePicker;
    public TextField timeTextField;
    public Button saveVisitationButton;

    private Visitation visitation;

    public void setControllerCallback(Consumer<Visitation> controllerCallback) {
        this.controllerCallback = controllerCallback;
    }

    public void setVisitation(Visitation visitation) {
        this.visitation = visitation;

        patientLastNameTextField.setText(visitation.getPatient().getLastName());
        patientFirstNameTextField.setText(visitation.getPatient().getFirstName());
        patientMiddleNameTextField.setText(visitation.getPatient().getMiddleName());
        patientSnilsTextField.setText(visitation.getPatient().getSnils());
        patientPhoneNumberTextField.setText(visitation.getPatient().getPhoneNumber());

        medicalServiceComboBox.setValue(visitation.getMedicalService());
        datePicker.setValue(visitation.getDateTime().toLocalDate());
        timeTextField.setText(visitation.getDateTime().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
    }

    @FXML
    public void initialize() {
        initializeMedicalServiceComboBox();

        client.getMedicalServiceList()
                .thenAccept(serviceList -> {
                    Platform.runLater(() -> {
                        medicalServiceComboBox.setItems(FXCollections.observableArrayList(serviceList));
                    });
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> {
                        Notifications
                                .create()
                                .title("Ошибка!")
                                .text("Не удалось загрузить данные о мед.услугах!")
                                .showError();
                    });

                    throw new RuntimeException(throwable);
                });
    }

    public void saveVisitationAction(ActionEvent actionEvent) {
        Visitation editedVisitation = buildVisitation();

        client.updateVisitation(editedVisitation)
                .thenAccept(v -> {
                    controllerCallback.accept(editedVisitation);

                    Platform.runLater(() -> {
                        Notifications
                                .create()
                                .text("Данные успешно сохранены.")
                                .showConfirm();
                    });
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> {
                        Notifications
                                .create()
                                .title("Ошибка!")
                                .text("Не удалось обновить данные о посещении!")
                                .showError();
                    });

                    throw new RuntimeException(throwable);
                });
    }

    private Visitation buildVisitation() {
        Visitation visitation = new Visitation();

        visitation.setId(this.visitation.getId());
        visitation.setPatient(this.visitation.getPatient());
        visitation.setStaff(this.visitation.getStaff());
        visitation.setMedicalService(medicalServiceComboBox.getSelectionModel().getSelectedItem());
        visitation.setDateTime(LocalDateTime.of(datePicker.getValue(), LocalTime.parse(timeTextField.getText())));

        return visitation;
    }

    private void initializeMedicalServiceComboBox() {
        medicalServiceComboBox.setCellFactory(new MedicalServiceComboBoxCellFactory());
        medicalServiceComboBox.setConverter(new MedicalServiceConverter());
    }
}

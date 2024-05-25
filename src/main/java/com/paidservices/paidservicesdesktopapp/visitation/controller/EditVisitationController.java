package com.paidservices.paidservicesdesktopapp.visitation.controller;

import com.paidservices.paidservicesdesktopapp.visitation.model.MedicalService;
import com.paidservices.paidservicesdesktopapp.visitation.model.Staff;
import com.paidservices.paidservicesdesktopapp.visitation.model.Visitation;
import com.paidservices.paidservicesdesktopapp.visitation.view.cellfactory.MedicalServiceComboBoxCellFactory;
import com.paidservices.paidservicesdesktopapp.visitation.view.cellfactory.StaffComboBoxCellFactory;
import com.paidservices.paidservicesdesktopapp.visitation.view.converter.MedicalServiceConverter;
import com.paidservices.paidservicesdesktopapp.visitation.view.converter.StaffConverter;
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

import java.util.function.Consumer;

public class EditVisitationController {
    private final WebClient client = WebClient.getInstance();
    private Consumer<Visitation> controllerCallback;

    public ComboBox<Staff> staffComboBox;
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
    }

    @FXML
    public void initialize() {
        initializeStaffComboBox();
        initializeMedicalServiceComboBox();

        client.getStaffList()
                .thenAccept(staffList -> {
                    Platform.runLater(() -> {
                        staffComboBox.setItems(FXCollections.observableArrayList(staffList));
                    });
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> {
                        Notifications
                                .create()
                                .title("Ошибка!")
                                .text("Не удалось загрузить данные о мед.персонале!")
                                .showError();
                    });

                    throw new RuntimeException(throwable);
                });

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

    }

    private void initializeStaffComboBox() {
        staffComboBox.setCellFactory(new StaffComboBoxCellFactory());
        staffComboBox.setConverter(new StaffConverter());
    }

    private void initializeMedicalServiceComboBox() {
        medicalServiceComboBox.setCellFactory(new MedicalServiceComboBoxCellFactory());
        medicalServiceComboBox.setConverter(new MedicalServiceConverter());
    }
}

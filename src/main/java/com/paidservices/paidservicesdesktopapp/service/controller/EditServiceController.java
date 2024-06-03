package com.paidservices.paidservicesdesktopapp.service.controller;

import com.paidservices.paidservicesdesktopapp.model.MedicalService;
import com.paidservices.paidservicesdesktopapp.webclient.client.WebClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

import java.math.BigDecimal;
import java.util.function.Consumer;

public class EditServiceController {
    private final WebClient client = WebClient.getInstance();
    private Consumer<MedicalService> controllerCallback;

    public TextField serviceNameTextField;
    public TextField servicePriceTextField;
    public Button saveServiceButton;

    private MedicalService medicalService;

    public void setControllerCallback(Consumer<MedicalService> controllerCallback) {
        this.controllerCallback = controllerCallback;
    }

    public void setMedicalService(MedicalService service) {
        this.medicalService = service;

        serviceNameTextField.setText(medicalService.getName());
        servicePriceTextField.setText(medicalService.getPrice().toPlainString());
    }

    public void saveServiceAction(ActionEvent actionEvent) {
        MedicalService editedService = buildMedicalService();

        client.updateMedicalService(editedService)
                .thenAccept((v) -> {
                    controllerCallback.accept(editedService);

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
                                .text("Не удалось сохранить данные о мед. услуге!")
                                .showError();
                    });

                    throw new RuntimeException(throwable);
                });
    }

    private MedicalService buildMedicalService() {
        MedicalService service = new MedicalService();

        service.setId(this.medicalService.getId());
        service.setName(serviceNameTextField.getText());
        service.setPrice(new BigDecimal(servicePriceTextField.getText()));

        return service;
    }
}

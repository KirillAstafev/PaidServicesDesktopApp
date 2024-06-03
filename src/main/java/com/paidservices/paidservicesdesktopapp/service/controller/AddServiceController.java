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

public class AddServiceController {
    private final WebClient client = WebClient.getInstance();
    private Consumer<MedicalService> controllerCallback;

    public TextField serviceNameTextField;
    public TextField servicePriceTextField;
    public Button saveServiceButton;

    public void setControllerCallback(Consumer<MedicalService> controllerCallback) {
        this.controllerCallback = controllerCallback;
    }

    public void saveServiceAction(ActionEvent actionEvent) {
        MedicalService service = buildMedicalService();

        client.saveMedicalService(service)
                .thenAccept(id -> {
                    service.setId(id);
                    controllerCallback.accept(service);

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
        MedicalService medicalService = new MedicalService();

        medicalService.setName(serviceNameTextField.getText());
        medicalService.setPrice(new BigDecimal(servicePriceTextField.getText()));

        return medicalService;
    }
}

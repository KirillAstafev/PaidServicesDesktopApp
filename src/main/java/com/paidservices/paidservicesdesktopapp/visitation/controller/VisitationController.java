package com.paidservices.paidservicesdesktopapp.visitation.controller;

import com.paidservices.paidservicesdesktopapp.visitation.model.Visitation;
import com.paidservices.paidservicesdesktopapp.webclient.client.WebClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.controlsfx.control.Notifications;

public class VisitationController {
    private final WebClient client = WebClient.getInstance();

    public TableView<Visitation> visitationTable;
    public Button addVisitationButton;
    public Button editVisitationButton;
    public Button deleteVisitationButton;

    @FXML
    public void initialize() {
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

    public void addVisitationAction(ActionEvent actionEvent) {

    }

    public void editVisitationAction(ActionEvent actionEvent) {

    }

    public void deleteVisitationAction(ActionEvent actionEvent) {

    }
}

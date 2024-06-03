package com.paidservices.paidservicesdesktopapp.service.controller;

import com.paidservices.paidservicesdesktopapp.PaidServiceApplication;
import com.paidservices.paidservicesdesktopapp.model.MedicalService;
import com.paidservices.paidservicesdesktopapp.webclient.client.WebClient;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class ServiceController {
    private final WebClient client = WebClient.getInstance();

    public Button addServiceButton;
    public Button editServiceButton;
    public Button deleteServiceButton;
    public TableView<MedicalService> serviceTable;
    public TableColumn<MedicalService, String> nameColumn;
    public TableColumn<MedicalService, String> priceColumn;

    @FXML
    public void initialize() {
        setCellValueFactories();

        client.getMedicalServiceList()
                .thenAccept(medicalServices -> {
                    Platform.runLater(() -> {
                        serviceTable.setItems(FXCollections.observableArrayList(medicalServices));
                    });
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> {
                        Notifications
                                .create()
                                .title("Ошибка!")
                                .text("Не удалось загрузить данные о мед. услугах!")
                                .showError();
                    });

                    throw new RuntimeException(throwable);
                });
    }

    private void setCellValueFactories() {
        nameColumn.setCellValueFactory(param -> {
            return new SimpleStringProperty(param.getValue().getName());
        });

        priceColumn.setCellValueFactory(param -> {
            return new SimpleStringProperty(String.format("%s р.",
                    param.getValue().getPrice().toPlainString()));
        });
    }

    public void addServiceAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PaidServiceApplication.class.getResource("service/add-service-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        AddServiceController controller = fxmlLoader.getController();
        controller.setControllerCallback(service -> {
            Platform.runLater(() -> {
                serviceTable.getItems().add(service);
                stage.close();
            });
        });

        stage.setTitle("Сохранение данных о мед. услуге");
        stage.setScene(scene);
        stage.show();
    }

    public void editServiceAction(ActionEvent actionEvent) throws IOException {
        if (serviceTable.getSelectionModel().getSelectedItem() == null) {
            Notifications
                    .create()
                    .text("Выберите данные для редактирования.")
                    .showWarning();

            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(PaidServiceApplication.class.getResource("service/edit-service-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        EditServiceController controller = fxmlLoader.getController();
        controller.setMedicalService(serviceTable.getSelectionModel().getSelectedItem());

        controller.setControllerCallback(service -> {
            Platform.runLater(() -> {
                serviceTable.getItems().removeIf(s -> s.getId().equals(service.getId()));
                serviceTable.getItems().add(service);
                stage.close();
            });
        });

        stage.setTitle("Редактирование данных о мед. услуге");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteServiceAction(ActionEvent actionEvent) {
        if (serviceTable.getSelectionModel().getSelectedItem() == null) {
            Notifications
                    .create()
                    .text("Выберите данные для удаления.")
                    .showWarning();

            return;
        }

        MedicalService service = serviceTable.getSelectionModel().getSelectedItem();

        client.deleteMedicalService(service.getId())
                .thenRun(() -> {
                    Platform.runLater(() -> {
                        serviceTable.getItems().removeIf(value -> value.getId().equals(service.getId()));
                        Notifications
                                .create()
                                .text("Данные успешно удалены.")
                                .showConfirm();
                    });
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> {
                        Notifications
                                .create()
                                .title("Ошибка!")
                                .text("Не удалось удалить данные!")
                                .showError();
                    });

                    throw new RuntimeException(throwable);
                });
    }
}

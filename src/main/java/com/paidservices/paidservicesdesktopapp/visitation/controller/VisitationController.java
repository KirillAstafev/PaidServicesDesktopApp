package com.paidservices.paidservicesdesktopapp.visitation.controller;

import com.paidservices.paidservicesdesktopapp.PaidServiceApplication;
import com.paidservices.paidservicesdesktopapp.visitation.model.MedicalService;
import com.paidservices.paidservicesdesktopapp.visitation.model.Person;
import com.paidservices.paidservicesdesktopapp.visitation.model.Staff;
import com.paidservices.paidservicesdesktopapp.visitation.model.Visitation;
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

    public void addVisitationAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PaidServiceApplication.class.getResource("visitation/add-visitation-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        AddVisitationController controller = fxmlLoader.getController();
        controller.setControllerCallback(visitation -> {
            Platform.runLater(() -> {
                visitationTable.getItems().add(visitation);
                stage.close();
            });
        });

        stage.setTitle("Сохранение данных о посещении");
        stage.setScene(scene);
        stage.show();
    }

    public void editVisitationAction(ActionEvent actionEvent) throws IOException {
        if (visitationTable.getSelectionModel().getSelectedItem() == null) {
            Notifications
                    .create()
                    .text("Выберите данные для редактирования.")
                    .showWarning();

            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(PaidServiceApplication.class.getResource("visitation/edit-visitation-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        EditVisitationController controller = fxmlLoader.getController();
        controller.setVisitation(visitationTable.getSelectionModel().getSelectedItem());

        controller.setControllerCallback(visitation -> {
            Platform.runLater(() -> {
                visitationTable.getItems().removeIf(v -> v.getId().equals(visitation.getId()));
                visitationTable.getItems().add(visitation);
                stage.close();
            });
        });

        stage.setTitle("Редактирование данных о посещении");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteVisitationAction(ActionEvent actionEvent) {
        if (visitationTable.getSelectionModel().getSelectedItem() == null) {
            Notifications
                    .create()
                    .text("Выберите данные для удаления.")
                    .showWarning();

            return;
        }

        Visitation visitation = visitationTable.getSelectionModel().getSelectedItem();

        client.deleteVisitation(visitation.getId())
                .thenRun(() -> {
                    Platform.runLater(() -> {
                        visitationTable.getItems().removeIf(value -> value.getId().equals(visitation.getId()));
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

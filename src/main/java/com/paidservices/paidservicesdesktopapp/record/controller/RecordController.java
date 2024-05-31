package com.paidservices.paidservicesdesktopapp.record.controller;

import com.paidservices.paidservicesdesktopapp.PaidServiceApplication;
import com.paidservices.paidservicesdesktopapp.model.MedicalRecord;
import com.paidservices.paidservicesdesktopapp.model.Person;
import com.paidservices.paidservicesdesktopapp.model.Staff;
import com.paidservices.paidservicesdesktopapp.model.Visitation;
import com.paidservices.paidservicesdesktopapp.visitation.controller.AddVisitationController;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class RecordController {
    private final WebClient client = WebClient.getInstance();

    public TextField patientSnilsTextField;
    public Button searchPatientButton;
    public Button addRecordButton;
    public Button editRecordButton;
    public Button deleteRecordButton;
    public TableView<MedicalRecord> recordTable;
    public TableColumn<MedicalRecord, String> patientColumn;
    public TableColumn<MedicalRecord, String> staffColumn;
    public TableColumn<MedicalRecord, String> diagnosisColumn;
    public TableColumn<MedicalRecord, String> dateColumn;

    @FXML
    public void initialize() {
        setCellValueFactories();
    }

    private void setCellValueFactories() {
        patientColumn.setCellValueFactory(param -> {
            Person patient = param.getValue().getPatient();
            return new SimpleStringProperty(String.format("%s %s %s ",
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

        diagnosisColumn.setCellValueFactory(param -> {
            return new SimpleStringProperty(param.getValue().getDiagnosis().getName());
        });

        dateColumn.setCellValueFactory(param -> {
            LocalDate date = param.getValue().getDate();
            return new SimpleStringProperty(date
                    .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        });
    }

    public void searchPatientAction(ActionEvent actionEvent) {
        client.getPersonBySnils(patientSnilsTextField.getText())
                .thenAccept(person -> {
                    client.getRecordsByPatientId(person.getId())
                            .thenAccept(medicalRecords -> {
                                Platform.runLater(() -> {
                                    recordTable.setItems(FXCollections.observableArrayList(medicalRecords));
                                });
                            })
                            .exceptionally(throwable -> {
                                Platform.runLater(() -> {
                                    Notifications
                                            .create()
                                            .title("Ошибка!")
                                            .text("Не удалось загрузить данные о мед.карте пациента!")
                                            .showError();
                                });

                                throw new RuntimeException(throwable);
                            });
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> {
                        Notifications
                                .create()
                                .title("Ошибка!")
                                .text("Не удалось загрузить данные о пациенте!")
                                .showError();
                    });

                    throw new RuntimeException(throwable);
                });
    }

    public void addRecordAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PaidServiceApplication.class.getResource("record/add-record-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        AddRecordController controller = fxmlLoader.getController();
        controller.setControllerCallback(record -> {
            Platform.runLater(() -> {
                recordTable.getItems().add(record);
                stage.close();
            });
        });

        stage.setTitle("Сохранение мед.записи");
        stage.setScene(scene);
        stage.show();
    }

    public void editRecordAction(ActionEvent actionEvent) {

    }

    public void deleteRecordAction(ActionEvent actionEvent) {
        if (recordTable.getSelectionModel().getSelectedItem() == null) {
            Notifications
                    .create()
                    .text("Выберите данные для удаления.")
                    .showWarning();

            return;
        }

        MedicalRecord record = recordTable.getSelectionModel().getSelectedItem();

        client.deleteMedicalRecord(record.getId())
                .thenRun(() -> {
                    Platform.runLater(() -> {
                        recordTable.getItems().removeIf(value -> value.getId().equals(record.getId()));
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

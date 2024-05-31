package com.paidservices.paidservicesdesktopapp.record.controller;

import com.paidservices.paidservicesdesktopapp.model.Diagnosis;
import com.paidservices.paidservicesdesktopapp.model.MedicalRecord;
import com.paidservices.paidservicesdesktopapp.model.Person;
import com.paidservices.paidservicesdesktopapp.model.Staff;
import com.paidservices.paidservicesdesktopapp.record.view.cellfactory.DiagnosisComboBoxCellFactory;
import com.paidservices.paidservicesdesktopapp.record.view.converter.DiagnosisConverter;
import com.paidservices.paidservicesdesktopapp.visitation.view.cellfactory.StaffComboBoxCellFactory;
import com.paidservices.paidservicesdesktopapp.visitation.view.converter.StaffConverter;
import com.paidservices.paidservicesdesktopapp.webclient.client.WebClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.Notifications;

import java.util.function.Consumer;

public class AddRecordController {
    private final WebClient client = WebClient.getInstance();
    private Consumer<MedicalRecord> controllerCallback;

    public TextField patientSearchTextField;
    public Button searchPatientButton;
    public TextField patientLastNameTextField;
    public TextField patientFirstNameTextField;
    public TextField patientMiddleNameTextField;
    public TextField patientSnilsTextField;
    public TextField patientPhoneNumberTextField;
    public ComboBox<Staff> staffComboBox;
    public ComboBox<Diagnosis> diagnosisComboBox;
    public DatePicker datePicker;
    public TextArea noteTextArea;
    public Button saveRecordButton;

    private Person selectedPatient;

    public void setControllerCallback(Consumer<MedicalRecord> controllerCallback) {
        this.controllerCallback = controllerCallback;
    }

    @FXML
    public void initialize() {
        initializeStaffComboBox();
        initializeDiagnosisComboBox();

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

        client.getDiagnoses()
                .thenAccept(diagnoses -> {
                    Platform.runLater(() -> {
                        diagnosisComboBox.setItems(FXCollections.observableArrayList(diagnoses));
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

    public void searchPatientAction(ActionEvent actionEvent) {
        client.getPersonBySnils(patientSearchTextField.getText())
                .thenAccept(person -> {
                    Platform.runLater(() -> {
                        showPatientInfo(person);
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

    public void saveRecordAction(ActionEvent actionEvent) {
        MedicalRecord record = buildMedicalRecord();

        client.saveMedicalRecord(record)
                .thenAccept(id -> {
                    record.setId(id);
                    Platform.runLater(() -> {
                        controllerCallback.accept(record);

                        Platform.runLater(() -> {
                            Notifications
                                    .create()
                                    .text("Данные успешно сохранены.")
                                    .showConfirm();
                        });
                    });
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> {
                        Notifications
                                .create()
                                .title("Ошибка!")
                                .text("Не удалось сохранить данные о посещении!")
                                .showError();
                    });

                    throw new RuntimeException(throwable);
                });
    }

    private void showPatientInfo(Person patient) {
        selectedPatient = patient;

        patientFirstNameTextField.setText(selectedPatient.getFirstName());
        patientMiddleNameTextField.setText(selectedPatient.getMiddleName());
        patientLastNameTextField.setText(selectedPatient.getLastName());
        patientSnilsTextField.setText(selectedPatient.getSnils());
        patientPhoneNumberTextField.setText(selectedPatient.getPhoneNumber());
    }

    private MedicalRecord buildMedicalRecord() {
        MedicalRecord record = new MedicalRecord();

        record.setPatient(selectedPatient);
        record.setStaff(staffComboBox.getSelectionModel().getSelectedItem());
        record.setDiagnosis(diagnosisComboBox.getSelectionModel().getSelectedItem());
        record.setDate(datePicker.getValue());
        record.setNote(noteTextArea.getText());

        return record;
    }

    private void initializeStaffComboBox() {
        staffComboBox.setCellFactory(new StaffComboBoxCellFactory());
        staffComboBox.setConverter(new StaffConverter());
    }

    private void initializeDiagnosisComboBox() {
        diagnosisComboBox.setCellFactory(new DiagnosisComboBoxCellFactory());
        diagnosisComboBox.setConverter(new DiagnosisConverter());
    }
}

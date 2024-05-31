package com.paidservices.paidservicesdesktopapp.record.controller;

import com.paidservices.paidservicesdesktopapp.model.*;
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

public class EditRecordController {
    private final WebClient client = WebClient.getInstance();
    private Consumer<MedicalRecord> controllerCallback;

    public TextField patientSearchTextField;
    public Button searchPatientButton;
    public TextField patientLastNameTextField;
    public TextField patientFirstNameTextField;
    public TextField patientMiddleNameTextField;
    public TextField patientSnilsTextField;
    public TextField patientPhoneNumberTextField;
    public ComboBox<Diagnosis> diagnosisComboBox;
    public DatePicker datePicker;
    public TextArea noteTextArea;
    public Button saveRecordButton;

    private MedicalRecord selectedRecord;
    private Person selectedPatient;

    public void setControllerCallback(Consumer<MedicalRecord> controllerCallback) {
        this.controllerCallback = controllerCallback;
    }

    public void setSelectedRecord(MedicalRecord record) {
        this.selectedRecord = record;

        diagnosisComboBox.setValue(selectedRecord.getDiagnosis());
        datePicker.setValue(selectedRecord.getDate());
        noteTextArea.setText(selectedRecord.getNote());

        setSelectedPatient(record.getPatient());
    }

    private void setSelectedPatient(Person patient) {
        this.selectedPatient = patient;

        patientLastNameTextField.setText(patient.getLastName());
        patientFirstNameTextField.setText(patient.getFirstName());
        patientMiddleNameTextField.setText(patient.getMiddleName());
        patientSnilsTextField.setText(patient.getSnils());
        patientPhoneNumberTextField.setText(patient.getPhoneNumber());
    }

    @FXML
    public void initialize() {
        initializeDiagnosisComboBox();

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
                                .text("Не удалось загрузить данные о диагнозах!")
                                .showError();
                    });

                    throw new RuntimeException(throwable);
                });
    }

    public void searchPatientAction(ActionEvent actionEvent) {
        client.getPersonBySnils(patientSearchTextField.getText())
                .thenAccept(person -> {
                    Platform.runLater(() -> {
                        setSelectedPatient(person);
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
        MedicalRecord editedRecord = buildMedicalRecord();

        client.updateMedicalRecord(editedRecord)
                .thenAccept(v -> {
                    controllerCallback.accept(editedRecord);

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
                                .text("Не удалось обновить мед.запись!")
                                .showError();
                    });

                    throw new RuntimeException(throwable);
                });
    }

    private MedicalRecord buildMedicalRecord() {
        MedicalRecord record = new MedicalRecord();

        record.setId(selectedRecord.getId());
        record.setPatient(selectedPatient);
        record.setDiagnosis(diagnosisComboBox.getSelectionModel().getSelectedItem());
        record.setStaff(selectedRecord.getStaff());
        record.setDate(datePicker.getValue());
        record.setNote(noteTextArea.getText());

        return record;
    }

    private void initializeDiagnosisComboBox() {
        diagnosisComboBox.setCellFactory(new DiagnosisComboBoxCellFactory());
        diagnosisComboBox.setConverter(new DiagnosisConverter());
    }
}

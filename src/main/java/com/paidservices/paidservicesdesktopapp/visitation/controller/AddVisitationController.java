package com.paidservices.paidservicesdesktopapp.visitation.controller;

import com.paidservices.paidservicesdesktopapp.visitation.model.MedicalService;
import com.paidservices.paidservicesdesktopapp.visitation.model.Staff;
import com.paidservices.paidservicesdesktopapp.visitation.model.Visitation;
import com.paidservices.paidservicesdesktopapp.webclient.client.WebClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.controlsfx.control.Notifications;

import java.util.function.Consumer;

public class AddVisitationController {
    private final WebClient client = WebClient.getInstance();
    private Consumer<Visitation> controllerCallback;

    public TextField patientLastNameTextField;
    public TextField patientFirstNameTextField;
    public TextField patientMiddleNameTextField;
    public TextField patientSnilsTextField;
    public TextField patientPhoneNumberTextField;
    public ComboBox<Staff> staffComboBox;
    public ComboBox<MedicalService> medicalServiceComboBox;
    public DatePicker datePicker;
    public TextField timeTextField;
    public Button saveVisitationButton;

    public void setControllerCallback(Consumer<Visitation> controllerCallback) {
        this.controllerCallback = controllerCallback;
    }

    @FXML
    public void initialize() {
        initializeStaffComboBox();
        initializeMedicalServiceComboBox();

        client.getStaffList()
                .thenAccept(staffList -> {
                    Platform.runLater(() -> {
                        staffComboBox.setItems(FXCollections.observableArrayList(staffList));
                        staffComboBox.setValue(staffComboBox.getItems().get(0));
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
                        medicalServiceComboBox.setValue(medicalServiceComboBox.getItems().get(0));
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
        Visitation visitation = new Visitation();

        client.saveVisitation(visitation)
                .thenAccept(id -> {
                    visitation.setId(id);
                    controllerCallback.accept(visitation);
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

    private void initializeStaffComboBox() {
        staffComboBox.setCellFactory(comboBox -> new ListCell<>() {
            @Override
            protected void updateItem(Staff staff, boolean empty) {
                super.updateItem(staff, empty);

                if (staff == null || empty) {
                    setText(null);
                } else {
                    setText(String.format("%s %s.%s (%s)",
                            staff.getPerson().getLastName(),
                            staff.getPerson().getFirstName().charAt(0),
                            staff.getPerson().getMiddleName() != null ? staff.getPerson().getMiddleName().charAt(0) : null,
                            staff.getSpecialities().get(0).getName()));
                }
            }
        });

        staffComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Staff staff) {
                if (staff == null) return "";

                return String.format("%s %s.%s (%s)",
                        staff.getPerson().getLastName(),
                        staff.getPerson().getFirstName().charAt(0),
                        staff.getPerson().getMiddleName() != null ? staff.getPerson().getMiddleName().charAt(0) : null,
                        staff.getSpecialities().get(0).getName());
            }

            @Override
            public Staff fromString(String string) {
                return null;
            }
        });
    }

    private void initializeMedicalServiceComboBox() {
        medicalServiceComboBox.setCellFactory(comboBox -> new ListCell<>() {
            @Override
            protected void updateItem(MedicalService service, boolean empty) {
                super.updateItem(service, empty);

                if (service == null || empty) {
                    setText(null);
                } else {
                    setText(String.format("%s (%s р.)",
                            service.getName(),
                            service.getPrice().toPlainString()));
                }
            }
        });

        medicalServiceComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MedicalService service) {
                if (service == null) return "";

                return String.format("%s (%s р.)",
                        service.getName(),
                        service.getPrice().toPlainString());
            }

            @Override
            public MedicalService fromString(String string) {
                return null;
            }
        });
    }
}

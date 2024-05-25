package com.paidservices.paidservicesdesktopapp.visitation.view.cellfactory;

import com.paidservices.paidservicesdesktopapp.visitation.model.MedicalService;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class MedicalServiceComboBoxCellFactory implements Callback<ListView<MedicalService>, ListCell<MedicalService>> {

    @Override
    public ListCell<MedicalService> call(ListView<MedicalService> comboBox) {
        return new ListCell<>() {
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
        };
    }
}

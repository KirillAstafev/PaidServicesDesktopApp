package com.paidservices.paidservicesdesktopapp.record.view.cellfactory;

import com.paidservices.paidservicesdesktopapp.model.Diagnosis;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class DiagnosisComboBoxCellFactory implements Callback<ListView<Diagnosis>, ListCell<Diagnosis>> {
    @Override
    public ListCell<Diagnosis> call(ListView<Diagnosis> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Diagnosis diagnosis, boolean empty) {
                super.updateItem(diagnosis, empty);

                if (diagnosis == null || empty) {
                    setText(null);
                } else {
                    setText(diagnosis.getName());
                }
            }
        };
    }
}

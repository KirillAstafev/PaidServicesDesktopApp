package com.paidservices.paidservicesdesktopapp.visitation.view.cellfactory;

import com.paidservices.paidservicesdesktopapp.visitation.model.Staff;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class StaffComboBoxCellFactory implements Callback<ListView<Staff>, ListCell<Staff>> {
    @Override
    public ListCell<Staff> call(ListView<Staff> param) {
        return new ListCell<>() {
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
        };
    }
}

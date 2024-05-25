package com.paidservices.paidservicesdesktopapp.visitation.view.converter;

import com.paidservices.paidservicesdesktopapp.model.Staff;
import javafx.util.StringConverter;

public class StaffConverter extends StringConverter<Staff> {
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
}

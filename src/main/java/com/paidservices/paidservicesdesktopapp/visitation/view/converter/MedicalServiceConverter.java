package com.paidservices.paidservicesdesktopapp.visitation.view.converter;

import com.paidservices.paidservicesdesktopapp.visitation.model.MedicalService;
import javafx.util.StringConverter;

public class MedicalServiceConverter extends StringConverter<MedicalService> {
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
}

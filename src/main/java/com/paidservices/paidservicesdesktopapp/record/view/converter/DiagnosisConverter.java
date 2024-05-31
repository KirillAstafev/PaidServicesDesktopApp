package com.paidservices.paidservicesdesktopapp.record.view.converter;

import com.paidservices.paidservicesdesktopapp.model.Diagnosis;
import javafx.util.StringConverter;

public class DiagnosisConverter extends StringConverter<Diagnosis> {
    @Override
    public String toString(Diagnosis diagnosis) {
        if (diagnosis == null) return "";

        return diagnosis.getName();
    }

    @Override
    public Diagnosis fromString(String string) {
        return null;
    }
}

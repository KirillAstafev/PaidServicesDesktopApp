module com.paidservices.paidservicesdesktopapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    requires retrofit2;
    requires retrofit2.converter.jackson;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.apache.pdfbox;
    requires org.apache.pdfbox.io;
    requires org.apache.commons.logging;

    opens com.paidservices.paidservicesdesktopapp to javafx.fxml;
    opens com.paidservices.paidservicesdesktopapp.visitation.controller to javafx.fxml;
    opens com.paidservices.paidservicesdesktopapp.financial.controller to javafx.fxml;
    opens com.paidservices.paidservicesdesktopapp.record.controller to javafx.fxml;
    opens com.paidservices.paidservicesdesktopapp.model to com.fasterxml.jackson.databind,
            com.fasterxml.jackson.datatype.jsr310;
    exports com.paidservices.paidservicesdesktopapp;
}
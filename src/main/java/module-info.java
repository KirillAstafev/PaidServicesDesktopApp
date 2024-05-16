module com.paidservices.paidservicesdesktopapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.paidservices.paidservicesdesktopapp to javafx.fxml;
    opens com.paidservices.paidservicesdesktopapp.visitation.controller to javafx.fxml;
    exports com.paidservices.paidservicesdesktopapp;
}
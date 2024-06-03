package com.paidservices.paidservicesdesktopapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class PaidServiceController {
    public TabPane modulePane;

    public void visitationModuleOnClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() < 2)
            return;

        FXMLLoader fxmlLoader = new FXMLLoader(PaidServiceApplication.class.getResource("visitation/visitation-view.fxml"));
        Node rootNode = fxmlLoader.load();

        Tab visitationTab = new Tab("Модуль посещений");
        visitationTab.setStyle("-fx-font-size: 18; -fx-padding: 10");
        visitationTab.setContent(rootNode);

        modulePane.getTabs().add(visitationTab);
        modulePane.getSelectionModel().select(visitationTab);
    }

    public void financialModuleOnClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() < 2)
            return;

        FXMLLoader fxmlLoader = new FXMLLoader(PaidServiceApplication.class.getResource("financial/financial-view.fxml"));
        Node rootNode = fxmlLoader.load();

        Tab financialTab = new Tab("Модуль фин.отчётности");
        financialTab.setStyle("-fx-font-size: 18; -fx-padding: 10");
        financialTab.setContent(rootNode);

        modulePane.getTabs().add(financialTab);
        modulePane.getSelectionModel().select(financialTab);
    }

    public void recordsModuleOnClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() < 2)
            return;

        FXMLLoader fxmlLoader = new FXMLLoader(PaidServiceApplication.class.getResource("record/record-view.fxml"));
        Node rootNode = fxmlLoader.load();

        Tab recordsTab = new Tab("Модуль мед.записей");
        recordsTab.setStyle("-fx-font-size: 18; -fx-padding: 10");
        recordsTab.setContent(rootNode);

        modulePane.getTabs().add(recordsTab);
        modulePane.getSelectionModel().select(recordsTab);
    }

    public void servicesModuleOnClick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() < 2)
            return;

        FXMLLoader fxmlLoader = new FXMLLoader(PaidServiceApplication.class.getResource("service/service-view.fxml"));
        Node rootNode = fxmlLoader.load();

        Tab recordsTab = new Tab("Модуль мед. услуг");
        recordsTab.setStyle("-fx-font-size: 18; -fx-padding: 10");
        recordsTab.setContent(rootNode);

        modulePane.getTabs().add(recordsTab);
        modulePane.getSelectionModel().select(recordsTab);
    }
}
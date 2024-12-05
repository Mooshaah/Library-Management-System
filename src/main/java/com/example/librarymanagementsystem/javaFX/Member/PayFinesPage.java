package com.example.librarymanagementsystem.javaFX.Member;

import com.example.librarymanagementsystem.Backend.DAOs.FineDAO;
import com.example.librarymanagementsystem.Backend.Models.Fine;
import com.example.librarymanagementsystem.Backend.Models.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class PayFinesPage {
    private final Stage stage;
    private final FineDAO fineDAO;
    private final User user;

    public PayFinesPage(Stage stage, User user) {
        this.stage = stage;
        this.fineDAO = new FineDAO();
        this.user = user;
    }

    public void show() {
        stage.setTitle("Pay Fines");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // Table to display fines
        TableView<Fine> fineTable = new TableView<>();
        fineTable.setPrefHeight(400);

        TableColumn<Fine, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(data -> new SimpleStringProperty("$" + data.getValue().getAmount()));

        TableColumn<Fine, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isPaid() ? "Paid" : "Unpaid"));

        fineTable.getColumns().addAll(amountColumn, statusColumn);

        // Fetch and display fines
        List<Fine> fines = fineDAO.getFinesByMemberId(user.getId());
        ObservableList<Fine> fineList = FXCollections.observableArrayList(fines);
        fineTable.setItems(fineList);

        // Pay button
        Button payButton = new Button("Pay Selected Fine");
        payButton.setOnAction(event -> {
            Fine selectedFine = fineTable.getSelectionModel().getSelectedItem();
            if (selectedFine == null) {
                showAlert(Alert.AlertType.ERROR, "No Fine Selected", "Please select a fine to pay.");
                return;
            }

            if (selectedFine.isPaid()) {
                showAlert(Alert.AlertType.INFORMATION, "Fine Already Paid", "The selected fine has already been paid.");
                return;
            }

            // Process fine payment
            fineDAO.payFine(selectedFine.getId());
            selectedFine.setPaid(true); // Update the status locally
            fineTable.refresh(); // Refresh the table view
            showAlert(Alert.AlertType.INFORMATION, "Payment Successful", "The fine has been paid successfully.");
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> new MemberDashboardPage(stage, user).show());

        layout.getChildren().addAll(fineTable, payButton, backButton);

        Scene scene = new Scene(layout, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

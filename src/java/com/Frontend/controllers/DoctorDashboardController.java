package com.Frontend.controllers;

import com.Frontend.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.collections.FXCollections; // For creating list for editDoctor
import javafx.collections.ObservableList; // For creating list for editDoctor


import java.io.IOException;

public class DoctorDashboardController {

    @FXML private Button doctorProfileButton;
    @FXML private Button patientProfileButton;
    @FXML private Button apptScheduleButton;
    @FXML private Button writeDiagnosisButton;
    @FXML private Button logoutButton;

    @FXML
    public void initialize() {
        // Apply styles if needed
    }

    @FXML
    void handleDoctorProfileButton(ActionEvent event) {
        // Original logic: Show edit dialog for the currently logged-in doctor
        Doctor currentDoctor = SessionManager.getCurrentDoctor();
        if (currentDoctor != null) {
            ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
            doctorList.add(currentDoctor);
            // Assume editDoctor is a utility method or handled by loading a dialog FXML
            DialogLoader.showEditDoctorDialog(doctorList, 1); // Mode 1 for editing
        } else {
            UIUtils.showError("Error", "No doctor session found.");
        }
    }

    @FXML
    void handlePatientProfileButton(ActionEvent event) {
        loadScene("fxml/doctor_patients_table.fxml");
    }

    @FXML
    void handleApptScheduleButton(ActionEvent event) {
        loadScene("fxml/appt_schedule.fxml");
    }

    @FXML
    void handleWriteDiagnosisButton(ActionEvent event) {
        // Original logic: Open the diagnosis tree window
        // This should load a new FXML dialog
        DialogLoader.showDiagnoseWindow();
    }

    @FXML
    void handleLogoutButton(ActionEvent event) {
        SessionManager.logout();
        loadScene("fxml/menu.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            Main.loadScene(fxmlPath);
        } catch (IOException e) {
            System.err.println("Failed to load scene: " + fxmlPath + " - " + e.getMessage());
            UIUtils.showError("Navigation Error", "Could not load the requested screen.");
        }
    }
}
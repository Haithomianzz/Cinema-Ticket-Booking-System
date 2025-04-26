package com.Frontend.controllers;

import com.Frontend.Main;
import com.Frontend.SessionManager;
import com.Frontend.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class AdminDashboardController {

    @FXML private Button doctorsButton;
    @FXML private Button patientsButton;
    @FXML private Button appointmentsButton;
    @FXML private Button roomsButton;
    @FXML private Button logoutButton;

    @FXML
    public void initialize() {
        // Apply styles if needed (e.g., icon button styles)
        // doctorsButton.setStyle(Style.DashboardButtonStyle);
    }

    @FXML
    void handleDoctorsButton(ActionEvent event) {
        loadScene("fxml/doctors_table.fxml");
    }

    @FXML
    void handlePatientsButton(ActionEvent event) {
        // Logic from original code: clear and load all patients for the admin view
        // This data loading should ideally happen in the PatientsTableController's initialize method
        loadScene("fxml/patients_table.fxml");
    }

    @FXML
    void handleAppointmentsButton(ActionEvent event) {
        loadScene("fxml/appointments_table.fxml");
    }

    @FXML
    void handleRoomsButton(ActionEvent event) {
        // Logic from original code: load occupied rooms view
        // This data loading should ideally happen in the RoomsTableController's initialize method
        loadScene("fxml/rooms_table.fxml");
    }

    @FXML
    void handleLogoutButton(ActionEvent event) {
        SessionManager.logout(); // Clear session data
        loadScene("fxml/menu.fxml"); // Go back to main menu
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
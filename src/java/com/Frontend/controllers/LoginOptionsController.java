package com.Frontend.controllers;

import com.Frontend.Main;
import com.Frontend.SessionManager;
import com.Frontend.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class LoginOptionsController {

    @FXML private Button adminLoginButton;
    @FXML private Button physicianLoginButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        // adminLoginButton.setStyle(Style.ButtonStyle); // Apply styles if needed
        // physicianLoginButton.setStyle(Style.ButtonStyle);
        // backButton.setStyle(Style.ButtonStyle);
    }

    @FXML
    void handleAdminLogin(ActionEvent event) {
        navigateToLoginForm("Admin");
    }

    @FXML
    void handlePhysicianLogin(ActionEvent event) {
        navigateToLoginForm("Doctor");
    }

    private void navigateToLoginForm(String userType) {
        try {
            // Pass user type info perhaps via a SessionManager or similar
            SessionManager.setUserType(userType);
            Main.loadScene("fxml/login_form.fxml");
        } catch (IOException e) {
            System.err.println("Failed to load login form scene: " + e.getMessage());
            UIUtils.showError("Navigation Error", "Could not load the login form.");
        }
    }


    @FXML
    void handleBackButtonAction(ActionEvent event) {
        try {
            Main.loadScene("fxml/menu.fxml");
        } catch (IOException e) {
            System.err.println("Failed to load menu scene: " + e.getMessage());
            UIUtils.showError("Navigation Error", "Could not load the main menu.");
        }
    }
}
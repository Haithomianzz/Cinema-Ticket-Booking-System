package com.Frontend.controllers;

import com.Frontend.Main; // Assuming Main has loadScene
import com.Frontend.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {

    @FXML private Button loginButton;
    @FXML private Button exitButton;

    @FXML
    public void initialize() {
        // Initialization logic if needed
        // Apply styles programmatically if not fully handled by FXML/CSS
        // loginButton.setStyle(Style.ButtonStyle); // Example if needed
        // exitButton.setStyle(Style.ButtonStyle);
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        try {
            // Navigate to the login options screen (or login form directly)
            // Assuming login_options.fxml exists and is the next step
            Main.loadScene("fxml/login_options.fxml");
        } catch (IOException e) {
            System.err.println("Failed to load login options scene: " + e.getMessage());
            // Consider showing an AlertBox here
            UIUtils.showError("Navigation Error", "Could not load the login screen.");
        }
    }

    @FXML
    protected void handleExitButtonAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
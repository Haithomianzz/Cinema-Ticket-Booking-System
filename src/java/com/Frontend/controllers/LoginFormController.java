package com.Frontend.controllers;

import com.Frontend.Main;
import com.Frontend.SessionManager;
import com.Frontend.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


import java.io.IOException;

public class LoginFormController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label resultLabel;
    @FXML private Button loginButton;
    @FXML private Button backButton;

    // Temporary: Define constants here or in Style interface/class
    private static final String H2 = "-fx-font-size: 18px; -fx-font-weight: bold;";
    private static final String H3 = "-fx-font-size: 16px;";
    private static final String Success = "-fx-text-fill: green;";
    private static final String Warning = "-fx-text-fill: red;";


    // This needs to be managed, perhaps in a SessionManager class
    // public static ObservableList<Patient> SessionPatients = FXCollections.observableArrayList();
    // public static Doctor SessionDoctor;


    @FXML
    public void initialize() {
        resultLabel.setText("");
        // Apply styles if needed
        // usernameField.setStyle(H3);
        // passwordField.setStyle(H3);
        // loginButton.setStyle(Style.ButtonStyle);
        // backButton.setStyle(Style.ButtonStyle);
    }

    @FXML
    void handleLoginButtonAction(ActionEvent event) {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();
        String userType = SessionManager.getUserType(); // Get user type selected previously

        // Replace verifyCredentials with actual authentication logic
        if (AuthService.verifyCredentials(enteredUsername, enteredPassword, userType)) {
            resultLabel.setText("Successful Login!");
            resultLabel.setStyle(Success + H2);
            usernameField.clear();
            passwordField.clear();

            try {
                if ("Admin".equals(userType)) {
                    Main.loadScene("fxml/admin_dashboard.fxml");
                } else if ("Doctor".equals(userType)) {
                    // Load doctor specific data upon successful login
                    SessionManager.setCurrentDoctor(AuthService.getLoggedInDoctor()); // Get logged-in doctor object
                    SessionManager.loadDoctorPatients(); // Method to load patients for the logged-in doctor
                    // Navigation to dashboard happens after successful data load maybe
                    Main.loadScene("fxml/doctor_dashboard.fxml");
                }
            } catch (IOException e) {
                System.err.println("Failed to load dashboard scene: " + e.getMessage());
                resultLabel.setText("Login Succeeded, but failed to load dashboard.");
                resultLabel.setStyle(Warning + H2);
                UIUtils.showError("Navigation Error", "Could not load the dashboard.");
            }
            // Reset label style after navigation attempt (or on scene change)
            // resultLabel.setText("");
            // resultLabel.setStyle("");

        } else {
            resultLabel.setText("Incorrect Credentials!");
            resultLabel.setStyle(Warning + H2);
        }
    }

    @FXML
    void handleBackButtonAction(ActionEvent event) {
        try {
            // Go back to login options
            Main.loadScene("fxml/login_options.fxml");
        } catch (IOException e) {
            System.err.println("Failed to load login options scene: " + e.getMessage());
            UIUtils.showError("Navigation Error", "Could not load the previous screen.");
        }
    }

    // Placeholder for authentication logic - replace with your actual implementation
    // This might involve checking against stored credentials (database, file, etc.)
    // Needs access to Doctor/Admin credentials.
    private static class AuthService {
        static boolean verifyCredentials(String username, String password, String userType) {
            System.out.println("Verifying: " + username + "/" + password + " as " + userType);
            // Dummy logic: Replace with real check!
            if ("Admin".equals(userType) && "admin".equals(username) && "pass".equals(password)) {
                return true;
            } else if ("Doctor".equals(userType) && "doctor".equals(username) && "pass".equals(password)) {
                // Simulate fetching the logged-in doctor
                // In real app, query database based on username/password
                SessionManager.setCurrentDoctor(new Doctor("Dr. John Doe", "123 Main St", "555-1234", "Cardiology")); // Example Doctor
                SessionManager.getCurrentDoctor().setID(1); // Example ID
                return true;
            }
            return false;
        }

        static Doctor getLoggedInDoctor() {
            // Return the doctor object fetched during verification
            return SessionManager.getCurrentDoctor();
        }
    }
}
package com.Frontend.controllers;

import com.Backend.Entities.Customer;
import com.Frontend.AlertBox;
import com.Frontend.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.regex.Pattern;

public class ForgotPassword_pageController {

    @FXML
    private TextField FP_CPassword;
    @FXML
    private TextField FP_Password;
    @FXML
    private TextField FP_Email;

    public void ChangePassword(ActionEvent event) throws IOException {
        String email = FP_Email.getText().trim();
        String password = FP_CPassword.getText().trim();
        String CPassword = FP_Password.getText().trim();

        if (validateInput()) {
            Customer customer = new Customer();
            customer.setPassword(password);

            AlertBox.alert("Success", "Password Changed successfully!", "Close");
            goToLoginPage(event);
        }
    }

    private boolean validateInput() {
        String email = FP_Email.getText().trim();
        String password = FP_CPassword.getText().trim();
        String CPassword = FP_Password.getText().trim();

        if ( password.isEmpty() || CPassword.isEmpty() || email.isEmpty()) {
            AlertBox.alert("Validation Error", "All fields must be filled.", "Close");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.com$", email)) {
            AlertBox.alert("Validation Error", "Invalid email format.", "Close");
            return false;
        }
        if (!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", password)) {
            AlertBox.alert("Validation Error", "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and be at least 8 characters long.", "Close");
            return false;
        }
        if (!password.equals(CPassword)) {
            AlertBox.alert("Validation Error", "Passwords do not match.", "Close");
            return false;
        }
        return true;
    }

    public void goToLoginPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToLogin(event);
    }

}

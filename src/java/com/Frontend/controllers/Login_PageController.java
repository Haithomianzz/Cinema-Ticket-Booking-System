package com.Frontend.controllers;

import com.Backend.Client;
import com.Backend.Entities.Customer;
import com.Frontend.AlertBox;
import com.Frontend.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import com.Frontend.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login_PageController {

    @FXML
    private TextField LP_Email;

    @FXML
    private TextField LP_Password;

    public void initialize() {
        Main.setCurrentUser(null);
    }

    @FXML
    void handleLogin(ActionEvent event) throws IOException {
        String Email = LP_Email.getText().trim();
        String password = LP_Password.getText().trim();

        // Validate Input
        if (Email.isEmpty()) {
            AlertBox.alert("Error", "Email cannot be empty!", "Close");
            return;
        }
//        if (!Email.matches("^[a-zA-Z0-9](?!.*[_.]{2})[a-zA-Z0-9._]{1,18}[a-zA-Z0-9]$")) {
//            AlertBox.alert("Invalid Email", "Please enter a valid Email.", "Close");
//            return;
//        }
        if (password.isEmpty()) {
            AlertBox.alert( "Error", "Password cannot be empty!","Close");
            return;
        }

        if (!Client.verifyCredentials(Email, password)) {
            AlertBox.alert("Login Failed", "Invalid Email or password.", "Close");
            return;
        }
        // Set the current user
        if (Main.getCurrentUserType() == Main.UserType.ADMIN) {
            System.out.println("Admin Login successful");
            goToAdminPage(event);
        } else if (Main.getCurrentUserType() == Main.UserType.CUSTOMER) {
            System.out.println("Customer Login successful");
            goToHomePage(event);
        } else {
            AlertBox.alert("Login Failed","Invalid Email or password.","Close");
        }
    }

    @FXML
    void goToForgotPassword(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToForgotPassword(event);
    }
    @FXML
    void goToSignUp(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToSignup(event);
    }
    @FXML
    void guest(ActionEvent event) throws IOException {
        Main.setCurrentUserType(Main.UserType.GUEST);
        goToHomePage(event);
    }
    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToHome(event);
    }
    @FXML
    void goToAdminPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToAdminMovesGenres(event);
    }
}

package com.Frontend.controllers;

import com.Backend.Entities.Customer;
import com.Frontend.AlertBox;
import com.Frontend.SceneController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
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
    private TextField LP_Username;

    @FXML
    private TextField LP_Password;

    @FXML
    void handleLogin(ActionEvent event) throws IOException {
        String username = LP_Username.getText().trim();
        String password = LP_Password.getText().trim();

        // Validate Input
        if (username.isEmpty()) {
            AlertBox.alert("Error", "Username cannot be empty!", "Close");
            return;
        }
        if (!username.matches("^[a-zA-Z0-9](?!.*[_.]{2})[a-zA-Z0-9._]{1,18}[a-zA-Z0-9]$")) {
            AlertBox.alert("Invalid Username", "Please enter a valid username.", "Close");
            return;
        }
        if (password.isEmpty()) {
            AlertBox.alert( "Error", "Password cannot be empty!","Close");
            return;
        }
        if (password.length() < 8) {
            AlertBox.alert("Invalid Password", "Password must be at least 8 characters long.", "Close");
            return;
        }

        Customer customer = userService.loginGUICustomer(username, password);
        Customer admin = userService.loginGUIAdmin(username, password);

        if (customer != null) {
            System.out.println("Customer Login successful");
            goToHomePage(event);
        } else if (admin != null) {
            System.out.println("Admin Login successful");
            goToAdminPage(event);
        } else {
            AlertBox.alert("Login Failed","Invalid username or password.","Close");
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
        //customer = null;
        goToHomePage(event);
    }
    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToLogin(event);
    }
    @FXML
    void goToAdminPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToHome(event);
    }
}

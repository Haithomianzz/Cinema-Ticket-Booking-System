package com.Frontend.controllers.Customer.Pages;

import com.Backend.Entities.Booking;
import com.Frontend.AlertBox;
import com.Frontend.Main;
import com.Frontend.SceneController;
import com.Frontend.controllers.Customer.Forms.Booking_FormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.awt.print.Book;
import java.io.IOException;
import java.util.List;

public class Booking_PagesController {
    @FXML
    private Label C_Username;
    @FXML
    private VBox BP_Clist;
    @FXML
    private AnchorPane BP_MList;

    public void initialize() throws IOException {
        if (Main.getCurrentUser() != null) {
            C_Username.setText(Main.getCurrentUser().getName());
        } else {
            C_Username.setText("Guest");
        }

        List<Booking> bookingList =Main.getCurrentUser().getBookings();

        if (bookingList != null) {
            BP_Clist.getChildren().clear();
            int i = 1;
            for (Booking booking : bookingList) {
                if (booking.getTickets().isEmpty()){
                    continue;
                }
                Button bookingButton = new Button("Booking " + i);
                bookingButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;");
                bookingButton.setOnMouseEntered(event -> bookingButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-size: 14px;"));
                bookingButton.setOnMouseExited(event -> bookingButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;"));
                bookingButton.setOnAction(event -> {
                    BP_MList.getChildren().clear();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/Frontend/fxml/Customer/Forms/Booking_Form.fxml"));
                    AnchorPane BForm = null;
                    try {
                        BForm = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Booking_FormController controller = loader.getController();
                    controller.setData(booking);

                    BP_MList.getChildren().add(BForm);
                });
                bookingButton.setPrefWidth(250);
                bookingButton.setPrefHeight(50);

                BP_Clist.getChildren().add(bookingButton);
                i++;
            }
        }
    }

    public void goToHomePage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToHome(event);
    }
    public void goToMoviesPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToMovies(event);
    }
    public void goToBookingPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToBookingPages(event);
    }
    public void goToTicketPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToTicketPage(event);
    }
    public void goToProfilePage(MouseEvent event) throws IOException {
        if (Main.getCurrentUserType() != Main.UserType.GUEST){
            SceneController sceneController = new SceneController();
            sceneController.SwitchToProfileForm(event);
        }
        else
            AlertBox.alert("Error", "You must be logged in to access this page!", "Close");
    }
    public void goToLoginPage(ActionEvent event) throws IOException {
        Main.setCurrentUser(null);
        Main.setCurrentUserType(Main.UserType.GUEST);
        SceneController sceneController = new SceneController();
        sceneController.SwitchToLogin(event);
    }

}

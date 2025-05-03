package com.Frontend.controllers.Customer.Pages;

import com.Backend.Entities.Booking;
import com.Backend.Entities.Ticket;
import com.Frontend.AlertBox;
import com.Frontend.Main;
import com.Frontend.SceneController;
import com.Frontend.controllers.Customer.Cards.Ticket_CardController;
import com.Frontend.controllers.Customer.Forms.Booking_FormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;

public class Ticket_PageController {

    @FXML
    private Label C_Username;
    @FXML
    private ScrollPane TP_MList;
    @FXML
    private VBox TP_Slist;

    public void initialize() {
        if (Main.getCurrentUser() != null) {
            C_Username.setText(Main.getCurrentUser().getName());
        } else {
            C_Username.setText("Guest");
        }

        List<Booking> bookingList =Main.getCurrentUser().getBookings();

        if (bookingList != null) {
            TP_Slist.getChildren().clear();
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

                    VBox ticketCardsContainer = new VBox();
                    ticketCardsContainer.setSpacing(10); // Optional: Add spacing between ticket cards

                    List<Ticket> TicketList = booking.getTickets();
                    for (Ticket ticket : TicketList) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/Frontend/fxml/Customer/Cards/Ticket_Card.fxml"));
                        HBox TCard = null;
                        try {
                            TCard = loader.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Ticket_CardController controller = loader.getController();
                        controller.setData(ticket);

                        ticketCardsContainer.getChildren().add(TCard);
                    }

                    TP_MList.setContent(ticketCardsContainer);
                });
                bookingButton.setPrefWidth(250);
                bookingButton.setPrefHeight(50);

                TP_Slist.getChildren().add(bookingButton);
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
package com.Frontend.controllers.Customer.Forms;

import com.Backend.Client;
import com.Backend.Entities.Booking;
import com.Backend.Entities.Movie;
import com.Backend.Entities.Showtime;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Booking_FormController {

    @FXML
    private HBox BF_BList;
    @FXML
    private Label BF_Date;
    @FXML
    private Label BF_Hall;
    @FXML
    private Label BF_SDate;
    @FXML
    private Label BF_SPrice;
    @FXML
    private Label BF_STIME;
    @FXML
    private Label BF_Seats;
    @FXML
    private Label BF_Status;
    @FXML
    private Label BF_TPrice;
    @FXML
    private Label BF_Title;

    private Booking booking;

    public void setData(Booking booking) throws IllegalArgumentException {
        this.booking = booking;

        Showtime showtime = booking.getTickets().getFirst().getShowtime();
        Movie movie = showtime.getMovie();
        Booking.BookingStatus Status = booking.getBookingStatus();
        String seats = "";

        String time = showtime.getShowTime();
        int hour = Integer.parseInt(time.substring(0, 2));
        String period = hour >= 12 ? "PM" : "AM";
        hour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
        String formattedTime = String.format("%d:%s %s", hour, time.substring(3, 5), period);

        BF_Date.setText(booking.getBookingDate().toString());
        BF_Title.setText(movie.getTitle());
        BF_Hall.setText(String.valueOf(showtime.getHall().getHallNumber()));
        BF_SDate.setText(showtime.getShowDate().toString());
        BF_STIME.setText(formattedTime);
        BF_SPrice.setText(String.valueOf(showtime.getPricePerSeat()));

        for (int i = 0; i < booking.getTickets().size(); i++) {
            seats += booking.getTickets().get(i).getSeat().getSeatNumber();
            if (i != booking.getTickets().size() - 1) {
                seats += ", ";
            }
        }

        BF_Seats.setText(seats);
        BF_TPrice.setText(String.valueOf(booking.getTotalPrice()));
        BF_Status.setText(booking.getBookingStatus().toString());
        if(Status== Booking.BookingStatus.CANCELLED) {
            BF_Status.setStyle("-fx-text-fill: red");
        } else if(Status == Booking.BookingStatus.CONFIRMED) {
            BF_Status.setStyle("-fx-text-fill: green");
        } else if(Status== Booking.BookingStatus.PENDING) {

            BF_Status.setStyle("-fx-text-fill: orange");

            BF_BList.getChildren().clear();

            Button confirmButton = new Button("Confirm");
            confirmButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;");
            confirmButton.setOnMouseEntered(event -> confirmButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-size: 14px;"));
            confirmButton.setOnMouseExited(event -> confirmButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;"));
            confirmButton.setOnAction(event -> {
                Client.confirmBooking(booking);
                BF_Status.setText(booking.getBookingStatus().toString());
                BF_Status.setStyle("-fx-text-fill: green");
                BF_BList.getChildren().clear();

            });
            confirmButton.setPrefWidth(250);
            confirmButton.setPrefHeight(50);
            BF_BList.getChildren().add(confirmButton);

            Button cancelButton = new Button("Cancel");
            cancelButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;");
            cancelButton.setOnMouseEntered(event -> cancelButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-size: 14px;"));
            cancelButton.setOnMouseExited(event -> cancelButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;"));
            cancelButton.setOnAction(event -> {
                Client.cancelBooking(booking);
                BF_Status.setText(booking.getBookingStatus().toString());
                BF_Status.setStyle("-fx-text-fill: red");
                BF_BList.getChildren().clear();

            });
            cancelButton.setPrefWidth(250);
            cancelButton.setPrefHeight(50);
            BF_BList.getChildren().add(cancelButton);
        }

    }

}

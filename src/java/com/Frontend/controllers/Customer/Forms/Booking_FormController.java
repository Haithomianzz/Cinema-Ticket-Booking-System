package com.Frontend.controllers.Customer.Forms;

import com.Backend.Entities.Booking;
import com.Backend.Entities.Movie;
import com.Backend.Entities.Showtime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Booking_FormController {

    @FXML
    private Label BF_Date;
    @FXML
    private Label BF_Status;
    @FXML
    private Label BF_Title;
    @FXML
    private Label BF_Hall;
    @FXML
    private Label BF_SDate;
    @FXML
    private Label BF_STIME;
    @FXML
    private Label BF_SPrice;
    @FXML
    private Label BF_Seats;
    @FXML
    private Label BF_TPrice;

    private Booking booking;

    public void setData(Booking booking) throws IllegalArgumentException {
        this.booking = booking;
        Showtime showtime = booking.getTickets().getFirst().getShowtime();
        Movie movie = showtime.getMovie();
        BF_Date.setText(booking.getBookingDate().toString());
        BF_Status.setText(booking.getBookingStatus().toString());
        BF_Title.setText(movie.getTitle());
        BF_Hall.setText(String.valueOf(showtime.getHall().getHallNumber()));
        BF_SDate.setText(showtime.getShowDate().toString());
        BF_STIME.setText(showtime.getShowTime());
        BF_SPrice.setText(String.valueOf(showtime.getPricePerSeat()));
        String seats = "";
//        Integer totalPrice = 0;
        for (int i = 0; i < booking.getTickets().size(); i++) {
            seats += booking.getTickets().get(i).getSeat().getSeatNumber();
//            totalPrice += booking.getTickets().get(i).getShowtime().getPricePerSeat();
            if (i != booking.getTickets().size() - 1) {
                seats += ", ";
            }
        }
        BF_Seats.setText(seats);
        BF_TPrice.setText(String.valueOf(booking.getTotalPrice()));
    }
    public void cancelBooking(ActionEvent event) {
    }
    public void confirmBooking(ActionEvent event) {
    }

}

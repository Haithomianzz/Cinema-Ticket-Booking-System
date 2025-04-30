package com.Frontend.controllers.Customer.Cards;

import com.Backend.Entities.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ticket_CardController {

    @FXML
    private Label TC_Title;
    @FXML
    private Label TC_Date;
    @FXML
    private Label TC_Time;
    @FXML
    private Label TC_Hall;
    @FXML
    private Label TC_Seat;
    @FXML
    private Label TC_Price;
    @FXML
    private ImageView TC_QrCode;

    public void setData(Ticket ticket) throws IllegalArgumentException {
        TC_Title.setText(ticket.getShowtime().getMovie().getTitle());
        TC_Date.setText(ticket.getShowtime().getShowDate().toString());
        TC_Time.setText(ticket.getShowtime().getShowTime());
        TC_Hall.setText(Integer.toString(ticket.getShowtime().getHall().getHallNumber()));
        TC_Seat.setText(Integer.toString(ticket.getSeat().getSeatNumber()));
        TC_Price.setText(Integer.toString(ticket.getShowtime().getPricePerSeat()));
        TC_QrCode.setImage( new Image(getClass().getResourceAsStream( ticket.getQrCode() )));;
    }

}
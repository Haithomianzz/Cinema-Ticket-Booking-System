package com.Frontend.controllers.Customer.Cards;

import com.Backend.Entities.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

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
    private Label TC_SeatN;
    @FXML
    private Label TC_SeatR;
    @FXML
    private Label TC_Price;
    @FXML
    private ImageView TC_QrCode;

    public void setData(Ticket ticket) throws IllegalArgumentException {

        String time = ticket.getShowtime().getShowTime();
        int hour = Integer.parseInt(time.substring(0, 2));
        String period = hour >= 12 ? "PM" : "AM";
        hour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
        String formattedTime = String.format("%d:%s %s", hour, time.substring(3, 5), period);

        TC_Title.setText(ticket.getShowtime().getMovie().getTitle());
        TC_Date.setText(ticket.getShowtime().getShowDate().toString());
        TC_Time.setText(formattedTime);
        TC_Hall.setText(Integer.toString(ticket.getShowtime().getHall().getHallNumber()));
        TC_SeatN.setText(Integer.toString(ticket.getSeat().getSeatNumber()));
        TC_SeatR.setText(Integer.toString(ticket.getSeat().getRowNumber()));
        TC_Price.setText(Integer.toString(ticket.getShowtime().getPricePerSeat()));
        TC_QrCode.setImage(new Image(new ByteArrayInputStream(ticket.getQrCode())));
    }

}
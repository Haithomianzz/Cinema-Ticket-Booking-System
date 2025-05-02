package com.Frontend.controllers.Admin.Forms;

import com.Backend.Client;
import com.Backend.Entities.Hall;
import com.Backend.Entities.Seat;
import com.Frontend.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminHall_FormController {

    @FXML
    private Label AHF_Hall;
    @FXML
    private TextField AHF_NSeats;

    Hall hall;

    public void initialize() {
        AHF_Hall.setText(String.valueOf(Hall.getCounterID()+1));
    }

    public void setData(Hall hall) {
        this.hall = hall;
        if(hall != null) {
            AHF_Hall.setText(String.valueOf(hall.getHallNumber()));
            AHF_NSeats.setText(String.valueOf(hall.getSeats()));
        }
    }

    public void save(ActionEvent event) {
        int Nseats = Integer.parseInt(AHF_NSeats.getText());
        if( Nseats>0 && Nseats <19) {
            Hall newHall = new Hall();
            if (!Client.addHall(newHall)) {
                ;
                AlertBox.alert("Error", "Hall already exists!", "Close");
                return;
            }
            for (int r = 1; r <= 3; r++) {
                for (int c = 1; c <= 6; c++) {
                    if (Nseats == 0) {
                        AlertBox.alert("Success", "Successfully added all seats!", "Close");
                        return;
                    }
                    Seat seat = new Seat(newHall, r, c);
                    if (!Client.addSeat(seat)) {
                        AlertBox.alert("Error", "Seat already exists!", "Close");
                        return;
                    }
                    Nseats--;
                }
            }
        }
    }

    public void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}

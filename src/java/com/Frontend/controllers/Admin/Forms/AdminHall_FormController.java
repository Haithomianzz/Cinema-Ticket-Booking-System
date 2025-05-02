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
                AlertBox.alert("Error", "Hall already exists!", "Close");
            }
            AlertBox.alert("Success", "Hall added successfully!", "Close");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}

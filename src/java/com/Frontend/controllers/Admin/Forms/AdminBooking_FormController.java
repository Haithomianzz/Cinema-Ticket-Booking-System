package com.Frontend.controllers.Admin.Forms;

import com.Backend.Entities.Showtime;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class AdminBooking_FormController {

    @FXML
    private MaterialIconView A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6, C1, C2, C3, C4, C5, C6;
    @FXML
    private ComboBox<?> ABF_Customer;
    @FXML
    private Label ABF_Discount;
    @FXML
    private ComboBox<?> ABF_Movie;
    @FXML
    private Label ABF_SeatPrice;
    @FXML
    private Label ABF_Seats;
    @FXML
    private ComboBox<?> ABF_Showime;
    @FXML
    private Label ABF_TPrice;
    @FXML
    private Label availableSeatsLabel;
    @FXML
    private Label bookedSeatsLabel;
    @FXML
    private GridPane gridSeats;
    @FXML
    private Label totalSeatsLabel;


    public void setData(Showtime show){

    }


}

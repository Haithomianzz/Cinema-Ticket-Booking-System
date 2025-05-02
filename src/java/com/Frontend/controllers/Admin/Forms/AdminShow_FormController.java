package com.Frontend.controllers.Admin.Forms;

import com.Backend.Client;
import com.Backend.Entities.Hall;
import com.Backend.Entities.Movie;
import com.Backend.Entities.Showtime;
import com.Frontend.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class AdminShow_FormController {

    @FXML
    private DatePicker SF_Date;
    @FXML
    private ChoiceBox<String> SF_Hall;
    @FXML
    private ChoiceBox<String> SF_MOVIE;
    @FXML
    private Label SF_Movie;
    @FXML
    private TextField SF_SPrice;
    @FXML
    private TextField SF_Time;

    private Showtime showtime;

    public void initialize() {
        Map<Integer, Movie> movieMap = Client.getMovieMap();
        Map<Integer, Hall> hallMap = Client.getHallMap();

        ObservableList<String> movieTitles = FXCollections.observableArrayList();
        for (Movie movie : movieMap.values()) {
            movieTitles.add(movie.getTitle());
        }
        SF_MOVIE.setItems(movieTitles);

        ObservableList<String> hallNumbers = FXCollections.observableArrayList();
        for (Hall hall : hallMap.values()) {
            hallNumbers.add(String.valueOf(hall.getHallNumber()));
        }
        SF_Hall.setItems(hallNumbers);
    }

    public void setData(Showtime showtime) {
        this.showtime = showtime;
        if (showtime != null) {
            SF_MOVIE.setValue(showtime.getMovie().getTitle());
            SF_Hall.setValue(String.valueOf(showtime.getHall().getHallNumber()));
            SF_Date.setValue(showtime.getShowDate().getDate());
            SF_Time.setText(showtime.getShowTime());
            SF_SPrice.setText(String.valueOf(showtime.getPricePerSeat()));
        }
    }

    public void save(ActionEvent event) {
        String movieTitle = SF_MOVIE.getValue();
        String hallNumberStr = SF_Hall.getValue();
        LocalDate showDate = SF_Date.getValue();
        String showTimeStr = SF_Time.getText();
        String priceStr = SF_SPrice.getText();

        if (movieTitle == null || hallNumberStr == null || showDate == null || showTimeStr.isEmpty() || priceStr.isEmpty()) {
            AlertBox.alert("Error", "Please fill in all fields.", "Close");
            return;
        }

        try {
            int hallNumber = Integer.parseInt(hallNumberStr);
        } catch (NumberFormatException e) {
            AlertBox.alert("Error", "Invalid Hall number selected.", "Close");
            return;
        }
        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            AlertBox.alert("Error", "Invalid Price entered. Please enter a positive number.","Close");
            return;
        }
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            timeFormatter.parse(showTimeStr);
        } catch (DateTimeParseException e) {
            AlertBox.alert("Error", "Invalid Time format. Please use HH:mm (e.g., 14:30).", "Close");
            return;
        }
        cancel(event);
    }

    public void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
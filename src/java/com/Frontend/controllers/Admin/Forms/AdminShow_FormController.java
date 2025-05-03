package com.Frontend.controllers.Admin.Forms;

import com.Backend.Client;
import com.Backend.Entities.Hall;
import com.Backend.Entities.Movie;
import com.Backend.Entities.Showtime;
import com.Frontend.AlertBox;
import com.Frontend.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.StringConverter;

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
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Showtime showtime;
    private Movie movie;
    private Hall hall;

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

        // --- Set the StringConverter for the DatePicker ---
        SF_Date.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, dateFormatter);
                    } catch (DateTimeParseException e) {
                        // Handle parse error if needed, maybe return null or show an error
                        System.err.println("Error parsing date: " + string + " - " + e.getMessage());
                        return null; // Or handle appropriately
                    }
                } else {
                    return null;
                }
            }
        });
        // Set a default value or prompt text if desired
        SF_Date.setPromptText("yyyy-MM-dd");
        // Optionally set the default displayed date
        // SF_Date.setValue(LocalDate.now());
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
        for (Movie movie : Client.getMovieMap().values()) {
            if (movie.getTitle().equals(movieTitle)) {
                this.movie = movie;
                break;
            }
        }
        try {
            int hallNumber = Integer.parseInt(hallNumberStr);
        } catch (NumberFormatException e) {
            AlertBox.alert("Error", "Invalid Hall number selected.", "Close");
            return;
        }
        for (Hall hall : Client.getHallMap().values()) {
            if (hall.getHallNumber() == Integer.parseInt(hallNumberStr)) {
                this.hall = hall;
                break;
            }
        }

        if (movieTitle == null || hallNumberStr == null || showDate == null || showTimeStr.isEmpty() || priceStr.isEmpty() || this.movie == null || this.hall == null) {
            AlertBox.alert("Error", "Please fill in all fields.", "Close");
            return;
        }


        int price;
        try {
            price = Integer.parseInt(priceStr);
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
        if(showtime != null) {
            Showtime newShowtime = new Showtime(showtime.getShowtimeId(), this.movie, this.hall, showDate.toString(), showTimeStr, price);
            if (!Client.updateShowtime(showtime)) {
                AlertBox.alert("Error", "Showtime already exists!", "Close");
                return;
            }
            showtime.editShowtime(this.movie, this.hall, new Date(showDate), showTimeStr, price);
        } else {
            showtime = new Showtime(this.movie, this.hall, new Date(showDate), showTimeStr, price);
            if (!Client.addShowtime(showtime)) {
                AlertBox.alert("Error", "Showtime already exists!", "Close");
                return;
            }
        }
        cancel(event);
    }

    public void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
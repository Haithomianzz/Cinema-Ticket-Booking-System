package com.Frontend.controllers.Admin.Pages;
import com.Backend.Entities.*;
import com.Frontend.Date;
import com.Backend.Client;
import com.Frontend.AlertBox;
import com.Frontend.Main;
import com.Frontend.SceneController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Optional;

public class AdminShowsSeats_PageController {

    @FXML
    private Label C_Username;
    @FXML
    private TableView<Ticket> SST; // Show Seats Table (Available seats for selected showtime)
    @FXML
    private TableColumn<Ticket, Integer> SST_ST_ID; // Showtime ID (Contextual)
    @FXML
    private TableColumn<Ticket, Integer> SST_S_ID; // Seat ID
    @FXML
    private TableView<Showtime> ST; // Showtime Table
    @FXML
    private TableColumn<Showtime, Integer> ST_H_N; // Hall Number
    @FXML
    private TableColumn<Showtime, Integer> ST_M_ID; // Movie ID
    @FXML
    private TableColumn<Showtime, Integer> ST_ST_ID; // Showtime ID
    @FXML
    private TableColumn<Showtime, Date> ST_S_D; // Show Date
    @FXML
    private TableColumn<Showtime, Integer> ST_S_P; // Price Per Seat
    @FXML
    private TableColumn<Showtime, String> ST_S_T; // Show Time
    @FXML
    private TextField Search;

    private final ObservableList<Showtime> showtimeList = FXCollections.observableArrayList();
    private FilteredList<Showtime> filteredShowtimes;
    private final ObservableList<Ticket> availableSeatsList = FXCollections.observableArrayList(); // For the selected showtime

    public void initialize() {
        if (Main.getCurrentUser() != null && Main.getCurrentUser().getName() != null) {
            C_Username.setText(Main.getCurrentUser().getName());
        } else {
            C_Username.setText("Admin");
        }

        setupTables();
        setupSearchFilter();
        refreshTables();
    }

    private void setupTables() {

        ST_ST_ID.setCellValueFactory(new PropertyValueFactory<>("showtimeId"));
        ST_S_D.setCellValueFactory(new PropertyValueFactory<>("showDate"));
        ST_S_T.setCellValueFactory(new PropertyValueFactory<>("showTime"));
        ST_M_ID.setCellValueFactory(cellData -> {
            Movie movie = cellData.getValue().getMovie();
            return new SimpleIntegerProperty(movie != null ? movie.getMovieId() : -1).asObject();
        });
        ST_H_N.setCellValueFactory(cellData -> {
            Hall hall = cellData.getValue().getHall();
            return new SimpleIntegerProperty(hall != null ? hall.getHallNumber() : -1).asObject();
        });
        ST_S_P.setCellValueFactory(new PropertyValueFactory<>("pricePerSeat"));

        filteredShowtimes = new FilteredList<>(showtimeList, p -> true);

        SortedList<Showtime> sortedShowtimes = new SortedList<>(filteredShowtimes);

        sortedShowtimes.comparatorProperty().bind(ST.comparatorProperty());

        ST.setItems(sortedShowtimes);
        ST.setPlaceholder(new Label("No showtimes found."));

        SST_ST_ID.setCellValueFactory(cellData -> {
            Showtime selectedShowtime = ST.getSelectionModel().getSelectedItem();
            return new SimpleIntegerProperty(selectedShowtime != null ? selectedShowtime.getShowtimeId() : -1).asObject();
        });
        SST_S_ID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSeat().getSeatId()).asObject());

        SST.setItems(availableSeatsList);
        SST.setPlaceholder(new Label("Select a showtime to view available seats."));

        ST.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                availableSeatsList.setAll(newSelection.getTickets());
            } else {
                availableSeatsList.clear();
            }
            SST.refresh();
        });
    }
    private void refreshTables() {

        int selectedShowtimeIndex = ST.getSelectionModel().getSelectedIndex();
        Showtime selectedShowtime = ST.getSelectionModel().getSelectedItem();

        showtimeList.setAll(Client.getShowtimeMap().values());

        filteredShowtimes.setPredicate(filteredShowtimes.getPredicate());

        if (selectedShowtime != null && ST.getItems().contains(selectedShowtime)) {
            ST.getSelectionModel().select(selectedShowtime);
        } else if (selectedShowtimeIndex >= 0 && selectedShowtimeIndex < ST.getItems().size()) {
            ST.getSelectionModel().select(selectedShowtimeIndex);
        } else {
            ST.getSelectionModel().clearSelection();
            availableSeatsList.clear(); // Clear seats if selection is lost
        }

        ST.refresh();
        SST.refresh();
    }

    private void setupSearchFilter() {
        Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredShowtimes.setPredicate(showtime -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(showtime.getShowtimeId()).contains(lowerCaseFilter)) return true;
                if (showtime.getShowDate().toString().toLowerCase().contains(lowerCaseFilter)) return true;
                if (showtime.getShowTime().toLowerCase().contains(lowerCaseFilter)) return true;
                if (showtime.getMovie() != null && String.valueOf(showtime.getMovie().getMovieId()).contains(lowerCaseFilter)) return true;
                if (showtime.getMovie() != null && showtime.getMovie().getTitle().toLowerCase().contains(lowerCaseFilter)) return true;
                if (showtime.getHall() != null && String.valueOf(showtime.getHall().getHallNumber()).contains(lowerCaseFilter)) return true;
                if (String.valueOf(showtime.getPricePerSeat()).contains(lowerCaseFilter)) return true;

                return false;
            });

            ST.getSelectionModel().clearSelection();
            availableSeatsList.clear();
            SST.refresh();
            ST.refresh();
        });
    }

    public void add(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminShowForm(event,null);
        refreshTables();
    }

    public void edit(ActionEvent event) throws IOException {
        Showtime selectedShowtime = ST.getSelectionModel().getSelectedItem();
        if (selectedShowtime != null) {
            SceneController.SwitchToAdminShowForm(event, selectedShowtime);
            refreshTables();
        } else {
            AlertBox.alert("Information", "Please select a showtime to edit.", "Close");
        }
    }

    public void delete(ActionEvent event) {
        Showtime selectedShowtime = ST.getSelectionModel().getSelectedItem();
        if (selectedShowtime == null) {
            AlertBox.alert("Information", "Please select a showtime to delete.", "Close");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Showtime ID: " + selectedShowtime.getShowtimeId() + "?");
        confirmation.setContentText("Are you sure you want to delete this showtime and potentially associated tickets? This action cannot be undone.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = false;
            String errorMessage = "Failed to delete showtime.";
            try {
                success = Client.removeShowtime(selectedShowtime);
                if (success) {
                    AlertBox.alert("Success", "Showtime deleted successfully.", "Close");
                    refreshTables();
                } else {
                    AlertBox.alert("Error", errorMessage, "Close");
                }
            } catch (Exception e) {
                e.printStackTrace();
                AlertBox.alert("Error", "An error occurred during deletion: " + e.getMessage(), "Close");
            }
        }
    }
    public void goToAdminBookingsTicketsPage(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminBookingsTickets(event);
    }
    public void goToAdminCustomersPage(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminCustomers(event);
    }
    public void goToAdminHallsSeatsPage(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminHallsSeats(event);
    }
    public void goToAdminMovesGenresPage(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminMovesGenres(event);
    }
    public void goToAdminShowsSeatsPage(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminShowsSeats(event);
    }
    public void goToLoginPage(ActionEvent event) throws IOException {
        SceneController.SwitchToLogin(event);
    }

}
package com.Frontend.controllers.Admin.Pages;

import com.Backend.Client;
import com.Backend.Entities.Hall;
import com.Backend.Entities.Seat;
import com.Frontend.AlertBox;
import com.Frontend.Main;
import com.Frontend.SceneController;
import javafx.beans.property.SimpleObjectProperty;
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

public class AdminHallsSeats_PageController {

    @FXML
    private Label C_Username;
    @FXML
    private TextField Search;
    @FXML
    private TableView<Hall> HT; // Hall Table
    @FXML
    private TableColumn<Hall, Integer> HT_Hall_number;
    @FXML
    private TableColumn<Hall, Integer> TH_Nseats;
    @FXML
    private TableView<Seat> ST; // Seat Table
    @FXML
    private TableColumn<Seat, Integer> TS_SId;
    @FXML
    private TableColumn<Seat, Integer> TS_Hall_number; // Display Hall Number for Seat
    @FXML
    private TableColumn<Seat, Integer> TS_Seat_number;
    @FXML
    private TableColumn<Seat, Integer> ST_Row_number;

    private final ObservableList<Hall> hallList = FXCollections.observableArrayList();
    private final ObservableList<Seat> seatList = FXCollections.observableArrayList();
    private FilteredList<Hall> filteredHalls;
    private FilteredList<Seat> filteredSeats;

    public void initialize() {
        if (Main.getCurrentUser() != null && Main.getCurrentUser().getName() != null) {
            C_Username.setText(Main.getCurrentUser().getName());
        } else {
            C_Username.setText("Admin"); // Fallback or default name
        }

        setupTables();
        setupSearchFilter();

        // Listener to filter seats when a hall is selected
        HT.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            filterSeatsByHall(newSelection);
        });

        refreshTables(); // Initial data load
    }

    private void setupTables() {
        // Hall Table Setup
        HT_Hall_number.setCellValueFactory(new PropertyValueFactory<>("hallNumber"));
        TH_Nseats.setCellValueFactory(new PropertyValueFactory<>("numberOfSeats"));

        filteredHalls = new FilteredList<>(hallList, p -> true); // Initially show all halls
        SortedList<Hall> sortedHalls = new SortedList<>(filteredHalls);
        sortedHalls.comparatorProperty().bind(HT.comparatorProperty());
        HT.setItems(sortedHalls);
        HT.setPlaceholder(new Label("No halls found."));

        // Seat Table Setup
        TS_SId.setCellValueFactory(new PropertyValueFactory<>("seatId"));
        // Use a cell value factory to get the hall number from the Seat's Hall object
        TS_Hall_number.setCellValueFactory(cellData -> {
            Seat seat = cellData.getValue();
            if (seat != null && seat.getHall() != null) {
                return new SimpleObjectProperty<>(seat.getHall().getHallNumber());
            } else {
                return new SimpleObjectProperty<>(null); // Or handle appropriately
            }
        });
        TS_Seat_number.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        ST_Row_number.setCellValueFactory(new PropertyValueFactory<>("rowNumber"));

        // Initially, the seat list is empty until a hall is selected.
        // The predicate is set in filterSeatsByHall().
        filteredSeats = new FilteredList<>(seatList, p -> false);
        SortedList<Seat> sortedSeats = new SortedList<>(filteredSeats);
        sortedSeats.comparatorProperty().bind(ST.comparatorProperty());
        ST.setItems(sortedSeats);
        ST.setPlaceholder(new Label("Select a hall to view its seats."));
    }

    private void refreshTables() {
        // Store current selection index
        int selectedHallIndex = HT.getSelectionModel().getSelectedIndex();

        // Re-fetch data from Client maps
        hallList.setAll(Client.getHallMap().values());
        seatList.setAll(Client.getSeatMap().values()); // Load all seats initially

        // Refresh TableViews (important for SortedList/FilteredList updates)
        HT.refresh();
        ST.refresh(); // Refresh seat table as well

        // Re-apply filters and restore selection if possible
        applySearchFilter(Search.getText()); // Re-apply search filter first

        // Restore selection
        if (selectedHallIndex >= 0 && selectedHallIndex < HT.getItems().size()) {
            HT.getSelectionModel().select(selectedHallIndex);
        } else {
            HT.getSelectionModel().clearSelection();
        }

        // Re-filter seats based on the potentially restored selection
        filterSeatsByHall(HT.getSelectionModel().getSelectedItem());
    }

    // Filters the Seat Table based on the selected Hall
    private void filterSeatsByHall(Hall selectedHall) {
        String lowerCaseFilter = Search.getText() == null ? "" : Search.getText().toLowerCase().trim();

        filteredSeats.setPredicate(seat -> {
            if (selectedHall == null) {
                return false; // No hall selected, show no seats
            }
            // Check if seat belongs to the selected hall
            boolean hallMatch = seat.getHall() != null && seat.getHall().getHallNumber() == selectedHall.getHallNumber();
            if (!hallMatch) {
                return false; // Doesn't belong to selected hall
            }

            // If search text is empty, show all seats for the selected hall
            if (lowerCaseFilter.isEmpty()) {
                return true;
            }

            // Check if the seat matches the search text
            return String.valueOf(seat.getSeatId()).contains(lowerCaseFilter) ||
                    String.valueOf(seat.getSeatNumber()).contains(lowerCaseFilter) ||
                    String.valueOf(seat.getRowNumber()).contains(lowerCaseFilter);
        });
        ST.refresh(); // Refresh seat table after changing predicate
    }

    private void setupSearchFilter() {
        Search.textProperty().addListener((observable, oldValue, newValue) -> {
            applySearchFilter(newValue);
        });
    }

    // Applies the search filter text to Halls and triggers Seat filtering
    private void applySearchFilter(String newValue) {
        String lowerCaseFilter = newValue == null ? "" : newValue.toLowerCase().trim();

        // Filter Halls
        filteredHalls.setPredicate(hall -> {
            if (lowerCaseFilter.isEmpty()) {
                return true; // No filter text, show all
            }
            // Match against hall number or number of seats
            return String.valueOf(hall.getHallNumber()).contains(lowerCaseFilter) ||
                    String.valueOf(hall.getNumberOfSeats()).contains(lowerCaseFilter);
        });

        // Re-filter seats based on the currently selected hall *after* filtering halls
        Hall selectedHall = HT.getSelectionModel().getSelectedItem();

        // If the currently selected hall is now filtered out, clear the selection and seat table
        if (selectedHall != null && !filteredHalls.getPredicate().test(selectedHall)) {
            HT.getSelectionModel().clearSelection();
            filterSeatsByHall(null); // Clear seats
        } else {
            // Otherwise, re-apply the seat filter for the (potentially still) selected hall
            filterSeatsByHall(selectedHall);
        }
        HT.refresh(); // Refresh hall table
    }

    public void add(ActionEvent event) throws IOException {
        // Open Hall Form for adding a new Hall (pass null)
        SceneController.SwitchToAdminHallForm(event, null);
        refreshTables(); // Refresh after the form is closed
    }

    public void edit(ActionEvent event) throws IOException {
        Hall selectedHall = HT.getSelectionModel().getSelectedItem();
        if (selectedHall != null) {
            // Open Hall Form for editing the selected Hall
            SceneController.SwitchToAdminHallForm(event, selectedHall);
            refreshTables(); // Refresh after the form is closed
        } else {
            AlertBox.alert("Information", "Please select a hall to edit.", "Close");
        }
    }

    public void delete(ActionEvent event) {
        Hall selectedHall = HT.getSelectionModel().getSelectedItem();
        if (selectedHall == null) {
            AlertBox.alert("Information", "Please select a hall to delete.", "Close");
            return;
        }

        // Check for dependencies (Showtimes)
        if (!selectedHall.getShowtimes().isEmpty()) {
            AlertBox.alert("Error", "Cannot delete Hall " + selectedHall.getHallNumber() +
                    " because it has associated showtimes. Please remove the showtimes first.", "Close");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Hall " + selectedHall.getHallNumber() + "?");
        confirmation.setContentText("Are you sure you want to delete this hall and all its seats? This action cannot be undone.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = false;
            String errorMessage = "Failed to delete Hall.";
            try {
                // Attempt to remove the hall (Client.removeHall should handle associated seats)
                success = Client.removeHall(selectedHall);
                if (!success) {
                    errorMessage += " Check database connection or logs.";
                }

                if (success) {
                    AlertBox.alert("Success", "Hall " + selectedHall.getHallNumber() + " deleted successfully.", "Close");
                    refreshTables(); // Refresh view after successful deletion
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
        // Already on this page, just refresh
        refreshTables();
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
package com.Frontend.controllers.Admin.Pages;

import com.Backend.Client;
import com.Backend.Entities.Hall;
import com.Backend.Entities.Seat;
import com.Frontend.Main;
import com.Frontend.SceneController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;

public class AdminHallsSeats_PageController {

    @FXML
    private Label C_Username;
    @FXML
    private TableView<Hall> HT;
    @FXML
    private TableColumn<Hall, Integer> HT_Hall_number;
    @FXML
    private TableView<Seat> ST;
    @FXML
    private TableColumn<Seat, Void> ST_Action;
    @FXML
    private TableColumn<Seat, Integer> ST_Row_number;
    @FXML
    private TextField Search;
    @FXML
    private TableColumn<Hall, Void> TH_Action;
    @FXML
    private TableColumn<Hall, Integer> TH_Nseats;
    @FXML
    private TableColumn<Seat, Integer> TS_Hall_number;
    @FXML
    private TableColumn<Seat, Integer> TS_SId;
    @FXML
    private TableColumn<Seat, Integer> TS_Seat_number;

    private final ObservableList<Hall> hallList = FXCollections.observableArrayList(Client.getHallMap().values());
    private final ObservableList<Seat> seatList = FXCollections.observableArrayList(Client.getSeatMap().values());
    private FilteredList<Hall> filteredHalls;
    private FilteredList<Seat> filteredSeats;

    public void initialize() {
        if (Main.getCurrentUser() != null) {
            C_Username.setText(Main.getCurrentUser().getName());
        } else {
            C_Username.setText("Admin User"); // Fallback or default name
        }

        setupHallTable();
        setupSeatTable();
        setupSearchFilter();

        // Add listener to Hall table selection
        HT.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                filterSeatsByHall(newSelection);
            } else {
                // Show all seats if no hall is selected (or handle as needed)
                filteredSeats.setPredicate(seat -> true); // Show all seats
                // Or clear the seat table: ST.setItems(FXCollections.observableArrayList());
            }
        });
    }

    private void setupHallTable() {
        HT_Hall_number.setCellValueFactory(new PropertyValueFactory<>("hallNumber"));
        TH_Nseats.setCellValueFactory(new PropertyValueFactory<>("numberOfSeats"));

        // Setup Action Column for Halls
        Callback<TableColumn<Hall, Void>, TableCell<Hall, Void>> hallActionCellFactory = param -> {
            final TableCell<Hall, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white;"); // Red delete button
                    deleteButton.setOnAction(event -> {
                        Hall hall = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Hall " + hall.getHallNumber() + "? This will also delete associated seats and showtimes.", ButtonType.YES, ButtonType.NO);
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.YES) {
                                if (Client.removeHall(hall)) {
                                    hallList.remove(hall); // Update ObservableList
                                    seatList.removeIf(seat -> seat.getHall().getHallNumber() == hall.getHallNumber()); // Update seat list
                                    // Optionally refresh showtime/booking lists if they depend on this hall
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "Failed to delete hall.").showAndWait();
                                }
                            }
                        });
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox buttons = new HBox(deleteButton);
                        buttons.setSpacing(10);
                        buttons.setAlignment(Pos.CENTER);
                        setGraphic(buttons);
                    }
                }
            };
            return cell;
        };
        TH_Action.setCellFactory(hallActionCellFactory);

        filteredHalls = new FilteredList<>(hallList, p -> true); // Initially show all halls
        SortedList<Hall> sortedHalls = new SortedList<>(filteredHalls);
        sortedHalls.comparatorProperty().bind(HT.comparatorProperty());
        HT.setItems(sortedHalls);
        HT.setPlaceholder(new Label("No halls found."));
    }

    private void setupSeatTable() {
        TS_SId.setCellValueFactory(new PropertyValueFactory<>("seatId"));
        // Map Hall object to its number for display
TS_Hall_number.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHall().getHallNumber()));
        TS_Seat_number.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        ST_Row_number.setCellValueFactory(new PropertyValueFactory<>("rowNumber"));

        // Setup Action Column for Seats
        Callback<TableColumn<Seat, Void>, TableCell<Seat, Void>> seatActionCellFactory = param -> {
            final TableCell<Seat, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white;"); // Red delete button
                    deleteButton.setOnAction(event -> {
                        Seat seat = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Seat " + seat.getSeatId() + "?", ButtonType.YES, ButtonType.NO);
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.YES) {
                                if (Client.removeSeat(seat)) {
                                    seatList.remove(seat); // Update ObservableList
                                    // Update the hall's seat count in the hallList/hallMap if necessary
                                    Hall associatedHall = Client.getHallMap().get(seat.getHall().getHallNumber());
                                    if (associatedHall != null) {
                                        // Force refresh of the hall table cell if needed, or update hallList item
                                        int index = hallList.indexOf(associatedHall);
                                        if(index != -1) {
                                            // This might not automatically refresh the cell, direct update might be better
                                            // hallList.set(index, Client.getHallMap().get(associatedHall.getHallNumber()));
                                            HT.refresh(); // Refresh the whole table might be easier
                                        }
                                    }
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "Failed to delete seat.").showAndWait();
                                }
                            }
                        });
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox buttons = new HBox(deleteButton);
                        buttons.setSpacing(10);
                        buttons.setAlignment(Pos.CENTER);
                        setGraphic(buttons);
                    }
                }
            };
            return cell;
        };
        ST_Action.setCellFactory(seatActionCellFactory);

        filteredSeats = new FilteredList<>(seatList, p -> false); // Initially show no seats until a hall is selected
        SortedList<Seat> sortedSeats = new SortedList<>(filteredSeats);
        sortedSeats.comparatorProperty().bind(ST.comparatorProperty());
        ST.setItems(sortedSeats);
        ST.setPlaceholder(new Label("Select a hall to view its seats or no seats found."));
    }

    private void refreshTables() {
        // Re-fetch data and update lists - might be inefficient for large datasets
        hallList.setAll(Client.getHallMap().values());
        seatList.setAll(Client.getSeatMap().values());
        HT.refresh();
        ST.refresh();
        // Re-apply filters if needed
        Hall selectedHall = HT.getSelectionModel().getSelectedItem();
        filterSeatsByHall(selectedHall);
    }

    private void filterSeatsByHall(Hall selectedHall) {
        if (selectedHall == null) {
            filteredSeats.setPredicate(seat -> false); // Show no seats if no hall selected
        } else {
            filteredSeats.setPredicate(seat -> seat.getHall().getHallNumber() == selectedHall.getHallNumber());
        }
    }

    private void setupSearchFilter() {
        Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredHalls.setPredicate(hall -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all if search is empty
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Search by Hall Number
                if (String.valueOf(hall.getHallNumber()).contains(lowerCaseFilter)) {
                    return true;
                }
                // Add more search criteria if needed (e.g., number of seats)
                if (String.valueOf(hall.getNumberOfSeats()).contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match
            });

            // Optionally filter seats based on search too, or clear selection
            Hall selectedHall = HT.getSelectionModel().getSelectedItem();
            filterSeatsByHall(selectedHall); // Re-apply seat filter based on current hall selection and search
        });
    }

    public void add1(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminHallForm(event, null); // Pass null for adding new hall
        refreshTables();
    }

    public void goToAdminBookingsTicketsPage(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminBookingsTickets(event);
    }
    public void goToAdminCustomersPage(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminCustomers(event);
    }
    public void goToAdminHallsSeatsPage(ActionEvent event) throws IOException {
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
package com.Frontend.controllers.Admin.Pages;

import com.Backend.Client;
import com.Backend.Entities.Booking;
import com.Backend.Entities.Customer;
import com.Backend.Entities.Ticket;
import com.Frontend.AlertBox;
import com.Frontend.Date;
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

public class AdminBookingsTickets_PageController {

    @FXML
    private TableView<Booking> BT; // Booking Table
    @FXML
    private TableColumn<Booking, Date> BT_B_Date;
    @FXML
    private TableColumn<Booking, Integer> BT_B_ID;
    @FXML
    private TableColumn<Booking, Booking.BookingStatus> BT_B_S; // Booking Status
    @FXML
    private TableColumn<Booking, Integer> BT_C_ID; // Customer ID
    @FXML
    private Label C_Username;
    @FXML
    private TextField Search;
    @FXML
    private TableView<Ticket> TT; // Ticket Table
    @FXML
    private TableColumn<Ticket, Integer> TT_B_ID; // Booking ID (from Ticket's Booking)
    @FXML
    private TableColumn<Ticket, Integer> TT_ST_ID; // Showtime ID (from Ticket's Showtime)
    @FXML
    private TableColumn<Ticket, Integer> TT_S_ID; // Seat ID (from Ticket's Seat)

    private final ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    private FilteredList<Booking> filteredBookings;
    private final ObservableList<Ticket> ticketList = FXCollections.observableArrayList(); // For the selected booking

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
        // --- Booking Table (BT) Setup ---
        BT_B_ID.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        // Use a cell value factory for Customer ID to handle potential null customer
        BT_C_ID.setCellValueFactory(cellData -> {
            Customer customer = cellData.getValue().getCustomer();
            return new SimpleIntegerProperty(customer != null ? customer.getCustomerId() : -1).asObject(); // Display -1 or similar for null customer
        });
        BT_B_Date.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        BT_B_S.setCellValueFactory(new PropertyValueFactory<>("bookingStatus"));

        // Wrap the observable list in a FilteredList (initially display all data)
        filteredBookings = new FilteredList<>(bookingList, p -> true);

        // Wrap the FilteredList in a SortedList
        SortedList<Booking> sortedBookings = new SortedList<>(filteredBookings);

        // Bind the SortedList comparator to the TableView comparator
        sortedBookings.comparatorProperty().bind(BT.comparatorProperty());

        // Add sorted (and filtered) data to the table
        BT.setItems(sortedBookings);
        BT.setPlaceholder(new Label("No bookings found."));

        // --- Ticket Table (TT) Setup ---
        TT_B_ID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBooking().getBookingId()).asObject());
        TT_ST_ID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getShowtime().getShowtimeId()).asObject());
        TT_S_ID.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSeat().getSeatId()).asObject());

        // Tickets are loaded based on selected booking, so just set the list for now
        TT.setItems(ticketList);
        TT.setPlaceholder(new Label("Select a booking to view tickets."));

        // --- Listener for Booking Selection ---
        BT.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Populate ticket table with tickets from the selected booking
                ticketList.setAll(newSelection.getTickets());
            } else {
                // Clear ticket table if no booking is selected
                ticketList.clear();
            }
            TT.refresh(); // Refresh ticket table view
        });
    }

    private void refreshTables() {
        // Store current selection
        int selectedBookingIndex = BT.getSelectionModel().getSelectedIndex();
        Booking selectedBooking = BT.getSelectionModel().getSelectedItem();

        // Fetch latest data from the Client
        bookingList.setAll(Client.getBookingMap().values());

        // Re-apply the filter (important if the list content changes)
        filteredBookings.setPredicate(filteredBookings.getPredicate());

        // Restore selection if possible
        if (selectedBooking != null && BT.getItems().contains(selectedBooking)) {
            BT.getSelectionModel().select(selectedBooking);
        } else if (selectedBookingIndex >= 0 && selectedBookingIndex < BT.getItems().size()) {
            BT.getSelectionModel().select(selectedBookingIndex);
        } else {
            BT.getSelectionModel().clearSelection();
            ticketList.clear(); // Clear tickets if selection is lost
        }

        // Refresh the table views
        BT.refresh();
        TT.refresh(); // Refresh ticket table as well (in case selection changed)
    }

    private void setupSearchFilter() {
        Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredBookings.setPredicate(booking -> {
                // If filter text is empty, display all bookings.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Match against Booking ID
                if (String.valueOf(booking.getBookingId()).contains(lowerCaseFilter)) {
                    return true;
                }
                // Match against Customer ID
                if (booking.getCustomer() != null && String.valueOf(booking.getCustomer().getCustomerId()).contains(lowerCaseFilter)) {
                    return true;
                }
                // Match against Customer Name (if customer exists)
                if (booking.getCustomer() != null && booking.getCustomer().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                // Match against Customer Email (if customer exists)
                if (booking.getCustomer() != null && booking.getCustomer().getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                // Match against Booking Date
                if (booking.getBookingDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                // Match against Booking Status
                if (booking.getBookingStatus().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Does not match.
            });

            // Clear ticket table when filter changes as selection might become invalid
            BT.getSelectionModel().clearSelection();
            ticketList.clear();
            TT.refresh();
            BT.refresh(); // Refresh booking table after filtering
        });
    }

    public void add(ActionEvent event) throws IOException {
        // Assuming SceneController has a method to switch to the Admin Booking Form
        // This form would handle creating a new Booking and associated Tickets
        SceneController.SwitchToAdminBookingForm(event, null); // Pass null for new booking
        refreshTables(); // Refresh after the form is closed
    }

    public void edit(ActionEvent event) throws IOException {
        Booking selectedBooking = BT.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            // Assuming SceneController can pass the selected Booking to the form
            SceneController.SwitchToAdminBookingForm(event, selectedBooking); // Pass selected booking
            refreshTables(); // Refresh after the form is closed
        } else {
            AlertBox.alert("Information", "Please select a booking to edit.", "Close");
        }
    }

    public void delete(ActionEvent event) {
        Booking selectedBooking = BT.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            AlertBox.alert("Information", "Please select a booking to delete.", "Close");
            return;
        }

        // Confirmation Dialog
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Booking ID: " + selectedBooking.getBookingId() + "?");
        confirmation.setContentText("Are you sure you want to delete this booking and all associated tickets? This action cannot be undone.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = false;
            String errorMessage = "Failed to delete booking.";
            try {
                // Attempt to remove the booking via the Client
                // Client.removeBooking should handle removing associated tickets from ticketMap and DB
                success = Client.removeBooking(selectedBooking);
                if (success) {
                    AlertBox.alert("Success", "Booking and associated tickets deleted successfully.", "Close");
                    refreshTables(); // Refresh the table to reflect the deletion
                } else {
                    // Attempt to retrieve a more specific error if available
                    AlertBox.alert("Error", errorMessage, "Close");
                }
            } catch (Exception e) {
                // Catch potential exceptions during the deletion process
                e.printStackTrace(); // Log the full error
                AlertBox.alert("Error", "An error occurred during deletion: " + e.getMessage(), "Close");
            }
        }
    }

    public void goToAdminBookingsTicketsPage(ActionEvent event) throws IOException {
        refreshTables();
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
        Main.setCurrentUser(null);
        Main.setCurrentUserType(Main.UserType.GUEST);
        SceneController.SwitchToLogin(event);
    }

}

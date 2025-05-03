package com.Frontend.controllers.Admin.Pages;

import com.Backend.Client;
import com.Backend.Entities.Customer;
import com.Frontend.AlertBox;
import com.Frontend.Main;
import com.Frontend.SceneController;
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

public class AdminCustomers_PageController {

    @FXML
    private TableView<Customer> CT; // Customer Table
    @FXML
    private TableColumn<Customer, Integer> CT_C_ID;
    @FXML
    private TableColumn<Customer, String> CT_E; // Email
    @FXML
    private TableColumn<Customer, Customer.MembershipStatus> CT_MS; // Membership Status
    @FXML
    private TableColumn<Customer, String> CT_N; // Name
    @FXML
    private TableColumn<Customer, String> CT_P; // Password (Consider masking or not showing this)
    @FXML
    private TableColumn<Customer, String> CT_PN; // Phone Number
    @FXML
    private Label C_Username;
    @FXML
    private TextField Search;

    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private FilteredList<Customer> filteredCustomers;

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
        // Configure table columns to display Customer properties
        CT_C_ID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        CT_N.setCellValueFactory(new PropertyValueFactory<>("name"));
        CT_E.setCellValueFactory(new PropertyValueFactory<>("email"));
        CT_P.setCellValueFactory(new PropertyValueFactory<>("password")); // Consider security implications of showing passwords
        CT_PN.setCellValueFactory(new PropertyValueFactory<>("phone"));
        CT_MS.setCellValueFactory(new PropertyValueFactory<>("membership"));

        // Wrap the observable list in a FilteredList (initially display all data)
        filteredCustomers = new FilteredList<>(customerList, p -> true);

        // Wrap the FilteredList in a SortedList
        SortedList<Customer> sortedCustomers = new SortedList<>(filteredCustomers);

        // Bind the SortedList comparator to the TableView comparator
        sortedCustomers.comparatorProperty().bind(CT.comparatorProperty());

        // Add sorted (and filtered) data to the table
        CT.setItems(sortedCustomers);
        CT.setPlaceholder(new Label("No customers found."));
    }

    private void refreshTables() {
        // Store current selection
        int selectedIndex = CT.getSelectionModel().getSelectedIndex();

        // Fetch latest data from the Client
        customerList.setAll(Client.getCustomerMap().values());

        filteredCustomers.setPredicate(filteredCustomers.getPredicate());

        if (selectedIndex >= 0 && selectedIndex < CT.getItems().size()) {
            CT.getSelectionModel().select(selectedIndex);
        } else {
            CT.getSelectionModel().clearSelection();
        }
        CT.refresh(); // Ensure the table visually updates
    }

    private void setupSearchFilter() {
        Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredCustomers.setPredicate(customer -> {
                // If filter text is empty, display all customers.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name
                } else if (customer.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches email
                } else if (customer.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches phone number
                } else if (String.valueOf(customer.getCustomerId()).contains(lowerCaseFilter)) {
                    return true; // Filter matches customer ID
                } else if (customer.getMembership().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches membership status
                }
                return false; // Does not match.
            });
            // Refresh table after filtering
            CT.refresh();
        });
    }
    private void updateCustomerInList(Customer updatedCustomer) {
        for (int i = 0; i < customerList.size(); i++) {
            if (customerList.get(i).getCustomerId() == updatedCustomer.getCustomerId()) {
                customerList.set(i, updatedCustomer);
                break;
            }
        }
    }
    public void add(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminCustomerForm(event, null);
        refreshTables();
    }

    public void edit(ActionEvent event) throws IOException {
        Customer selectedCustomer = CT.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            if (selectedCustomer.getCustomerId() == 0) { // Adjust this condition based on how Admin is identified
                AlertBox.alert("Information", "The Admin account cannot be edited.", "Close");
                return;
            }
            SceneController.SwitchToAdminCustomerForm(event, selectedCustomer);

            refreshTables(); // Update the observable list with the latest data
            updateCustomerInList(selectedCustomer); // Ensure the edited customer is updated in the observable list

            refreshTables(); // Refresh the table after the form is closed
        } else {
            AlertBox.alert("Information", "Please select a customer to edit.", "Close");
        }
    }

    public void delete(ActionEvent event) {
        Customer selectedCustomer = CT.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            AlertBox.alert("Information", "Please select a customer to delete.", "Close");
            return;
        }

        // Prevent deleting the Admin account
        if (selectedCustomer.getCustomerId() == 0) { // Adjust this condition
            AlertBox.alert("Error", "The Admin account cannot be deleted.", "Close");
            return;
        }

        // Check for dependencies (e.g., existing bookings)
        if (!selectedCustomer.getBookings().isEmpty()) {
            AlertBox.alert("Error", "Cannot delete customer " + selectedCustomer.getName() +
                    " because they have existing bookings. Please manage their bookings first.", "Close");
            return;
        }


        // Confirmation Dialog
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Customer: " + selectedCustomer.getName() + "?");
        confirmation.setContentText("Are you sure you want to delete this customer? This action cannot be undone.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = false;
            String errorMessage = "Failed to delete customer.";
            try {
                // Attempt to remove the customer via the Client
                success = Client.removeCustomer(selectedCustomer);
                if (!success) {
                    // Attempt to retrieve a more specific error if available, otherwise use default
                    // (The Client.removeCustomer might need enhancement to provide better error details)
                    AlertBox.alert("Error", errorMessage, "Close");
                } else {
                    AlertBox.alert("Success", "Customer deleted successfully.", "Close");
                    refreshTables(); // Refresh the table to reflect the deletion
                }
            } catch (Exception e) {
                // Catch potential exceptions during the deletion process
                e.printStackTrace(); // Log the full error
                AlertBox.alert("Error", "An error occurred during deletion: " + e.getMessage(), "Close");
            }
        }
    }

    public void goToAdminBookingsTicketsPage(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminBookingsTickets(event);
    }
    public void goToAdminCustomersPage(ActionEvent event) throws IOException {
        refreshTables();
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
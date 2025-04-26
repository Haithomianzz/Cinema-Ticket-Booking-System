package com.Frontend.controllers;

import com.Frontend.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Set;


public class AppointmentsTableController {

    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, Integer> apptIdCol;
    @FXML private TableColumn<Appointment, Integer> apptPatientIdCol;
    @FXML private TableColumn<Appointment, Integer> apptDoctorIdCol;
    @FXML private TableColumn<Appointment, String> apptDateCol; // Uses getDateString() from Appointment

    @FXML private Button doctorsSideButton;
    @FXML private Button patientsSideButton;
    @FXML private Button roomsSideButton;
    @FXML private Button backSideButton;
    @FXML private Button logoutSideButton;

    @FXML private Button backButton;
    @FXML private Button saveButton;
    @FXML private Button editButton;
    @FXML private Button addButton;
    @FXML private Button deleteButton;

    private ObservableList<Appointment> appointmentsData;

    @FXML
    public void initialize() {
        // Configure table columns
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        apptPatientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        apptDoctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        apptDateCol.setCellValueFactory(new PropertyValueFactory<>("DateString")); // Assumes Appointment has getDateString()

        appointmentsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Allow multi-delete

        // Load data
        loadAppointmentData();
    }

    private void loadAppointmentData() {
        Set<Appointment> appointmentSet = DataService.getAppointments(); // Load all appointments
        appointmentsData = FXCollections.observableArrayList(appointmentSet);
        appointmentsTable.setItems(appointmentsData);
    }

    //--- Side Menu Handlers ---
    @FXML void handleDoctorsSideButton(ActionEvent event) { loadScene("fxml/doctors_table.fxml"); }
    @FXML void handlePatientsSideButton(ActionEvent event) { loadScene("fxml/patients_table.fxml"); }
    @FXML void handleRoomsSideButton(ActionEvent event) { loadScene("fxml/rooms_table.fxml"); }
    @FXML void handleBackSideButton(ActionEvent event) { loadScene("fxml/admin_dashboard.fxml"); }
    @FXML void handleLogoutSideButton(ActionEvent event) { SessionManager.logout(); loadScene("fxml/menu.fxml"); }

    //--- Bottom Button Handlers ---
    @FXML void handleBackButton(ActionEvent event) { loadScene("fxml/admin_dashboard.fxml"); }

    @FXML void handleSaveButton(ActionEvent event) {
        // Saving is likely handled by Add/Edit/Delete actions directly interacting with backend/DB
        boolean success = DataService.saveAllAppointments(appointmentsData); // Example explicit save
        if (success) {
            UIUtils.showAlert("Save Successful", "Successfully saved Appointment Information to server...");
        } else {
            UIUtils.showError("Save Failed", "Could not save Appointment Information.");
        }
    }

    @FXML void handleEditButton(ActionEvent event) {
        ObservableList<Appointment> selectedAppointments = appointmentsTable.getSelectionModel().getSelectedItems();
        if (selectedAppointments.size() == 1) {
            DialogLoader.showEditAppointmentDialog(selectedAppointments, 1); // Mode 1 for Edit
            appointmentsTable.refresh(); // Refresh after potential edit
        } else {
            UIUtils.showAlert("Warning", "Please Select one Appointment at a time to Edit!");
        }
    }

    @FXML void handleAddButton(ActionEvent event) {
        DialogLoader.showEditAppointmentDialog(appointmentsData, 0); // Mode 0 for Add
        appointmentsTable.refresh(); // Refresh after potential add
    }

    @FXML void handleDeleteButton(ActionEvent event) {
        ObservableList<Appointment> selectedAppointments = appointmentsTable.getSelectionModel().getSelectedItems();
        if (!selectedAppointments.isEmpty()) {
            boolean confirmed = UIUtils.showConfirmation("Confirm Deletion", "Are you sure you want to delete the selected appointment(s)?");
            if(confirmed) {
                ObservableList<Appointment> appointmentsToDelete = FXCollections.observableArrayList(selectedAppointments);
                for (Appointment a : appointmentsToDelete) {
                    boolean deleted = Appointment.delete(a); // Backend delete
                    if(deleted) {
                        appointmentsData.remove(a);
                    } else {
                        UIUtils.showError("Deletion Failed", "Could not delete appointment ID: " + a.getID());
                    }
                }
                appointmentsTable.getSelectionModel().clearSelection();
                UIUtils.showAlert("Deletion Complete", "Selected appointments deleted successfully (if possible).");
            }
        } else {
            UIUtils.showAlert("Warning", "No Appointment Selected, Please Select Appointment(s) to Delete!");
        }
    }

    private void loadScene(String fxmlPath) {
        try {
            Main.loadScene(fxmlPath);
        } catch (IOException e) {
            System.err.println("Failed to load scene: " + fxmlPath + " - " + e.getMessage());
            UIUtils.showError("Navigation Error", "Could not load the requested screen.");
        }
    }
}
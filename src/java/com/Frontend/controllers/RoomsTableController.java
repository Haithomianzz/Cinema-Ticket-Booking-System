package com.Frontend.controllers;

import com.Frontend.DataService;
import com.Frontend.Main;
import com.Frontend.SessionManager;
import com.Frontend.UIUtils;
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
import java.util.stream.Collectors;


public class RoomsTableController {

    @FXML private TableView<Patient> roomsTable; // Displaying Patients who are in rooms
    @FXML private TableColumn<Patient, String> roomNumCol; // Use roomNumber property
    @FXML private TableColumn<Patient, Integer> roomIdCol; // Patient ID
    @FXML private TableColumn<Patient, String> roomNameCol; // Patient Name
    @FXML private TableColumn<Patient, String> roomSymptomsCol;
    @FXML private TableColumn<Patient, Boolean> roomEmergencyCol;

    @FXML private Button doctorsSideButton;
    @FXML private Button patientsSideButton;
    @FXML private Button appointmentsSideButton;
    @FXML private Button backSideButton;
    @FXML private Button logoutSideButton;

    @FXML private Button backButton;
    @FXML private Button saveButton;
    @FXML private Button vacateButton;

    private ObservableList<Patient> patientsInRoomsData;

    @FXML
    public void initialize() {
        // Configure table columns
        roomNumCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomIdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        roomNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        roomSymptomsCol.setCellValueFactory(new PropertyValueFactory<>("symptoms"));
        roomEmergencyCol.setCellValueFactory(new PropertyValueFactory<>("emergency"));

        // Allow single selection for vacate action
        roomsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Load data - Filter for patients with assigned rooms
        loadOccupiedRoomsData();
    }

    private void loadOccupiedRoomsData() {
        Set<Patient> allPatients = DataService.getPatients();
        // Filter patients who have a room assigned (roomNumber is not null or "N/A")
        Set<Patient> patientsInRoomsSet = allPatients.stream()
                // .filter(p -> p.getRoomNumber() != null && !p.getRoomNumber().equalsIgnoreCase("N/A")) // Adjust filter logic based on Patient class
                .filter(p -> p.isEmergency()) // Original code logic implies only emergency patients occupy rooms? Clarify this.
                .collect(Collectors.toSet());

        patientsInRoomsData = FXCollections.observableArrayList(patientsInRoomsSet);
        roomsTable.setItems(patientsInRoomsData);
    }

    //--- Side Menu Handlers ---
    @FXML void handleDoctorsSideButton(ActionEvent event) { loadScene("fxml/doctors_table.fxml"); }
    @FXML void handlePatientsSideButton(ActionEvent event) { loadScene("fxml/patients_table.fxml"); } // Navigate to general patients view
    @FXML void handleAppointmentsSideButton(ActionEvent event) { loadScene("fxml/appointments_table.fxml"); }
    @FXML void handleBackSideButton(ActionEvent event) { loadScene("fxml/admin_dashboard.fxml"); }
    @FXML void handleLogoutSideButton(ActionEvent event) { SessionManager.logout(); loadScene("fxml/menu.fxml"); }

    //--- Bottom Button Handlers ---
    @FXML void handleBackButton(ActionEvent event) { loadScene("fxml/admin_dashboard.fxml"); }

    @FXML void handleSaveButton(ActionEvent event) {
        // Save changes if any (likely handled by vacate action)
        boolean success = DataService.saveAllPatients(patientsInRoomsData); // Example
        if(success) {
            UIUtils.showAlert("Save Successful", "Successfully saved Room Information to server...");
        } else {
            UIUtils.showError("Save Failed", "Could not save Room Information.");
        }
    }

    @FXML void handleVacateButton(ActionEvent event) {
        Patient selectedPatient = roomsTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            boolean confirmed = UIUtils.showConfirmation("Confirm Vacate",
                    "Are you sure you want to vacate room " + selectedPatient.getRoomNumber() + " for patient " + selectedPatient.getName() + "?");
            if(confirmed){
                boolean success = selectedPatient.vacate(); // Call backend method in Patient class
                if(success) {
                    // Remove patient from this table view as they no longer occupy a room
                    patientsInRoomsData.remove(selectedPatient);
                    roomsTable.getSelectionModel().clearSelection();
                    UIUtils.showAlert("Vacating Room", "Patient vacated successfully.\nSuccessfully saved Room Information to server...");
                } else {
                    UIUtils.showError("Vacate Failed", "Could not vacate the room for the patient.");
                }
            }
        } else {
            UIUtils.showAlert("Warning", "No Room/Patient Selected, Please Select Room to Vacate!");
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
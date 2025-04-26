package com.Frontend.controllers;

import com.Frontend.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.Frontend.UIUtils;
import java.io.IOException;


public class DoctorPatientsTableController {

    @FXML private TableView<Patient> patientsTable;
    @FXML private TableColumn<Patient, Integer> patIdCol;
    @FXML private TableColumn<Patient, String> patNameCol;
    @FXML private TableColumn<Patient, Boolean> patEmergencyCol; // Use Boolean for checkbox-like display if desired, or String
    @FXML private TableColumn<Patient, String> patRoomCol; // Room number or "N/A"

    @FXML private Button doctorProfileSideButton;
    @FXML private Button appointmentSchedSideButton;
    @FXML private Button diagnosesSideButton;
    @FXML private Button backSideButton;
    @FXML private Button logoutSideButton;

    @FXML private Button backButton;
    @FXML private Button saveButton; // Might not be needed if no inline editing
    @FXML private Button dischargeButton;

    private ObservableList<Patient> doctorPatientsData;
    private Doctor currentDoctor;

    @FXML
    public void initialize() {
        currentDoctor = SessionManager.getCurrentDoctor();
        if (currentDoctor == null) {
            UIUtils.showError("Error", "No doctor logged in. Returning to menu.");
            loadScene("fxml/menu.fxml"); // Or login
            return;
        }

        // Configure table columns
        patIdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        patNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        patEmergencyCol.setCellValueFactory(new PropertyValueFactory<>("emergency")); // 'emergency' property in Patient class
        patRoomCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber")); // 'roomNumber' property

        // Allow single selection for discharge
        patientsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Load data - Doctor sees only their patients
        loadDoctorPatientData();
    }

    private void loadDoctorPatientData() {
        if (currentDoctor != null) {
            // Load patients specifically for the current doctor
            doctorPatientsData = SessionManager.getDoctorPatients(); // Assumes SessionManager holds/loads this list
            if(doctorPatientsData == null){ // Fallback if not preloaded
                doctorPatientsData = FXCollections.observableArrayList(DataService.getPatientsForDoctor(currentDoctor.getID()));
                SessionManager.setDoctorPatients(doctorPatientsData); // Store it maybe
            }
            patientsTable.setItems(doctorPatientsData);
        }
    }

    //--- Side Menu Handlers ---
    @FXML void handleDoctorProfileSideButton(ActionEvent event) {
        if (currentDoctor != null) {
            ObservableList<Doctor> doctorList = FXCollections.obserableArrayList(currentDoctor);
            DialogLoader.showEditDoctorDialog(doctorList, 1); // Mode 1 for Edit
        }
    }
    @FXML void handleAppointmentSchedSideButton(ActionEvent event) { loadScene("fxml/appt_schedule.fxml"); }
    @FXML void handleDiagnosesSideButton(ActionEvent event) { DialogLoader.showDiagnoseWindow(); }
    @FXML void handleBackSideButton(ActionEvent event) { loadScene("fxml/doctor_dashboard.fxml"); }
    @FXML void handleLogoutSideButton(ActionEvent event) { SessionManager.logout(); loadScene("fxml/menu.fxml"); }


    //--- Bottom Button Handlers ---
    @FXML void handleBackButton(ActionEvent event) { loadScene("fxml/doctor_dashboard.fxml"); }

    @FXML void handleSaveButton(ActionEvent event) {
        // Save changes if any (e.g., if discharge status modified backend directly)
        // This might be redundant if actions update backend immediately.
        boolean success = DataService.saveAllPatients(doctorPatientsData);
        if (success) {
            UIUtils.showAlert("Save Successful", "Successfully saved Patient Information to server...");
        } else {
            UIUtils.showError("Save Failed", "Could not save Patient Information.");
        }
    }

    @FXML void handleDischargeButton(ActionEvent event) {
        Patient selectedPatient = patientsTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            // Check if patient is eligible for discharge (e.g., must be an emergency patient not already discharged)
            // The original code checks 'isEmergency()' which might be incorrect logic for discharge?
            // Assuming discharge applies to emergency patients who are *currently* admitted.
            // Let's refine the logic: A patient can be discharged if they are currently marked as 'emergency' (admitted).
            if (selectedPatient.isEmergency()) { // Check if currently marked as emergency (i.e., admitted)
                boolean confirmed = UIUtils.showConfirmation("Confirm Discharge",
                        "Are you sure you want to discharge patient " + selectedPatient.getName() + "?");
                if(confirmed){
                    boolean success = selectedPatient.discharge(); // Call backend method in Patient class
                    if (success) {
                        UIUtils.showAlert("Discharge Successful", "Patient discharged successfully.\nSuccessfully saved Patient Information to server...");
                        patientsTable.refresh(); // Update table view to reflect change
                        // Alternatively, remove from list if discharge means they disappear from this view
                        // doctorPatientsData.remove(selectedPatient);
                    } else {
                        UIUtils.showError("Discharge Failed", "Could not discharge the patient.");
                    }
                }
            } else {
                // If !isEmergency(), they might be already discharged or an outpatient.
                UIUtils.showAlert("Information", "This patient is either not currently admitted under emergency or is an outpatient.");
            }
        } else {
            UIUtils.showAlert("Warning", "No Patient Selected to discharge!");
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
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


public class PatientsTableController {

    @FXML private TableView<Patient> patientsTable;
    @FXML private TableColumn<Patient, Integer> patIdCol;
    @FXML private TableColumn<Patient, String> patNameCol;
    @FXML private TableColumn<Patient, String> patPhoneCol;
    @FXML private TableColumn<Patient, String> patAddressCol;
    @FXML private TableColumn<Patient, String> patGenderCol;

    @FXML private Button doctorsSideButton;
    @FXML private Button appointmentsSideButton;
    @FXML private Button roomsSideButton;
    @FXML private Button backSideButton;
    @FXML private Button logoutSideButton;

    @FXML private Button backButton;
    @FXML private Button saveButton;
    @FXML private Button editButton;
    @FXML private Button addButton;
    @FXML private Button deleteButton;

    private ObservableList<Patient> patientsData;

    @FXML
    public void initialize() {
        // Configure table columns
        patIdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        patNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        patPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        patAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        patGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        patientsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Load data - Admin sees all patients
        loadPatientData();
    }

    private void loadPatientData() {
        Set<Patient> patientSet = DataService.getPatients(); // Load all patients
        patientsData = FXCollections.observableArrayList(patientSet);
        patientsTable.setItems(patientsData);
    }

    //--- Side Menu Handlers ---
    @FXML void handleDoctorsSideButton(ActionEvent event) { loadScene("fxml/doctors_table.fxml"); }
    @FXML void handleAppointmentsSideButton(ActionEvent event) { loadScene("fxml/appointments_table.fxml"); }
    @FXML void handleRoomsSideButton(ActionEvent event) {
        // Navigate and potentially filter data in the target controller
        loadScene("fxml/rooms_table.fxml");
    }
    @FXML void handleBackSideButton(ActionEvent event) { loadScene("fxml/admin_dashboard.fxml"); }
    @FXML void handleLogoutSideButton(ActionEvent event) { SessionManager.logout(); loadScene("fxml/menu.fxml"); }


    //--- Bottom Button Handlers ---
    @FXML void handleBackButton(ActionEvent event) { loadScene("fxml/admin_dashboard.fxml"); }

    @FXML void handleSaveButton(ActionEvent event) {
        boolean success = DataService.saveAllPatients(patientsData); // Example save
        if (success) {
            UIUtils.showAlert("Save Successful", "Successfully saved Patient Information to server...");
        } else {
            UIUtils.showError("Save Failed", "Could not save Patient Information.");
        }
    }

    @FXML void handleEditButton(ActionEvent event) {
        ObservableList<Patient> selectedPatients = patientsTable.getSelectionModel().getSelectedItems();
        if (selectedPatients.size() == 1) {
            DialogLoader.showEditPatientDialog(selectedPatients, 1); // Mode 1 for Edit
            patientsTable.refresh();
        } else {
            UIUtils.showAlert("Warning", "Please Select one Patient at a time to Edit!");
        }
    }

    @FXML void handleAddButton(ActionEvent event) {
        DialogLoader.showEditPatientDialog(patientsData, 0); // Mode 0 for Add
        patientsTable.refresh();
    }

    @FXML void handleDeleteButton(ActionEvent event) {
        ObservableList<Patient> selectedPatients = patientsTable.getSelectionModel().getSelectedItems();
        if (!selectedPatients.isEmpty()) {
            boolean confirmed = UIUtils.showConfirmation("Confirm Deletion", "Are you sure you want to delete the selected patient(s)?");
            if (confirmed) {
                ObservableList<Patient> patientsToDelete = FXCollections.observableArrayList(selectedPatients);
                for (Patient p : patientsToDelete) {
                    boolean deleted = Patient.delete(p); // Backend delete
                    if (deleted) {
                        patientsData.remove(p);
                    } else {
                        UIUtils.showError("Deletion Failed", "Could not delete patient: " + p.getName());
                    }
                }
                patientsTable.getSelectionModel().clearSelection();
                UIUtils.showAlert("Deletion Complete", "Selected patients deleted successfully (if possible).");
            }
        } else {
            UIUtils.showAlert("Warning", "No Patient Selected, Please Select Patient(s) to Delete!");
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
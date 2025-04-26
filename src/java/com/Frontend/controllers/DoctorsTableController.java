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

import java.io.IOException;
import java.util.Set;


public class DoctorsTableController {

    @FXML private TableView<Doctor> doctorsTable;
    @FXML private TableColumn<Doctor, Integer> docIdCol;
    @FXML private TableColumn<Doctor, String> docNameCol;
    @FXML private TableColumn<Doctor, String> docPhoneCol;
    @FXML private TableColumn<Doctor, String> docDepartmentCol;

    @FXML private Button patientsSideButton;
    @FXML private Button appointmentsSideButton;
    @FXML private Button roomsSideButton;
    @FXML private Button backSideButton;
    @FXML private Button logoutSideButton;

    @FXML private Button backButton;
    @FXML private Button saveButton;
    @FXML private Button editButton;
    @FXML private Button addButton;
    @FXML private Button deleteButton;

    private ObservableList<Doctor> doctorsData;

    @FXML
    public void initialize() {
        // Configure table columns
        docIdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        docNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        docPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        docDepartmentCol.setCellValueFactory(new PropertyValueFactory<>("specialty"));

        // Allow multiple selections for deletion
        doctorsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Load data
        loadDoctorData();

        // Apply styles if needed
    }

    private void loadDoctorData() {
        Set<Doctor> doctorSet = DataService.getDoctors(); // Use your data loading method
        doctorsData = FXCollections.observableArrayList(doctorSet);
        doctorsTable.setItems(doctorsData);
    }

    //--- Side Menu Handlers ---
    @FXML void handlePatientsSideButton(ActionEvent event) { loadScene("fxml/patients_table.fxml"); }
    @FXML void handleAppointmentsSideButton(ActionEvent event) { loadScene("fxml/appointments_table.fxml"); }
    @FXML void handleRoomsSideButton(ActionEvent event) { loadScene("fxml/rooms_table.fxml"); }
    @FXML void handleBackSideButton(ActionEvent event) { loadScene("fxml/admin_dashboard.fxml"); }
    @FXML void handleLogoutSideButton(ActionEvent event) { SessionManager.logout(); loadScene("fxml/menu.fxml"); }

    //--- Bottom Button Handlers ---
    @FXML void handleBackButton(ActionEvent event) { loadScene("fxml/admin_dashboard.fxml"); }

    @FXML void handleSaveButton(ActionEvent event) {
        // Logic for saving changes (if edits are done inline, might not be needed)
        // Or save all data back using Doctor.saveAll() or similar
        boolean success = DataService.saveAllDoctors(doctorsData); // Example save method
        if (success) {
            UIUtils.showAlert("Save Successful", "Successfully saved Doctor Information to server...");
        } else {
            UIUtils.showError("Save Failed", "Could not save Doctor Information.");
        }
    }

    @FXML void handleEditButton(ActionEvent event) {
        ObservableList<Doctor> selectedDoctors = doctorsTable.getSelectionModel().getSelectedItems();
        if (selectedDoctors.size() == 1) {
            // Open the edit dialog for the selected doctor
            DialogLoader.showEditDoctorDialog(selectedDoctors, 1); // Mode 1 for Edit
            doctorsTable.refresh(); // Refresh table after dialog closes
        } else {
            UIUtils.showAlert("Warning", "Please Select one Doctor at a time to Edit!");
        }
    }

    @FXML void handleAddButton(ActionEvent event) {
        // Open the add/edit dialog in Add mode
        DialogLoader.showEditDoctorDialog(doctorsData, 0); // Mode 0 for Add, pass the list to add to
        doctorsTable.refresh(); // Refresh table after dialog closes
    }

    @FXML void handleDeleteButton(ActionEvent event) {
        ObservableList<Doctor> selectedDoctors = doctorsTable.getSelectionModel().getSelectedItems();
        if (!selectedDoctors.isEmpty()) {
            boolean confirmed = UIUtils.showConfirmation("Confirm Deletion", "Are you sure you want to delete the selected doctor(s)?");
            if (confirmed) {
                // Create a copy to avoid ConcurrentModificationException if removing directly from selection
                ObservableList<Doctor> doctorsToDelete = FXCollections.observableArrayList(selectedDoctors);
                for (Doctor d : doctorsToDelete) {
                    boolean deleted = Doctor.delete(d); // Call backend delete
                    if (deleted) {
                        doctorsData.remove(d); // Remove from observable list
                    } else {
                        UIUtils.showError("Deletion Failed", "Could not delete doctor: " + d.getName());
                    }
                }
                doctorsTable.getSelectionModel().clearSelection();
                UIUtils.showAlert("Deletion Complete", "Selected doctors deleted successfully (if possible).");
            }
        } else {
            UIUtils.showAlert("Warning", "No Doctor Selected, Please Select Doctor(s) to Delete!");
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
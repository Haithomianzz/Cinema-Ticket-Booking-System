package com.Frontend.controllers;

import com.Frontend.Main;
import javafx.collections.FXCollections; // For list passed to editAppointment
import javafx.collections.ObservableList; // For list passed to editAppointment
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox; // For side menu access

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Set;

public class ApptScheduleController {

    @FXML private Label monthLabel;
    @FXML private GridPane calendarGrid;
    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;

    // Side Menu Buttons
    @FXML private Button doctorProfileSideButton;
    @FXML private Button doctorPatientsSideButton;
    @FXML private Button diagnosesSideButton;
    @FXML private Button backSideButton;
    @FXML private Button logoutSideButton;
    @FXML private VBox sideMenu; // Reference to side menu if needed


    private YearMonth currentMonth;
    private Doctor currentDoctor;
    private Set<Appointment> doctorAppointments; // Cache appointments for the current view?

    @FXML
    public void initialize() {
        currentDoctor = SessionManager.getCurrentDoctor();
        if (currentDoctor == null) {
            UIUtils.showError("Error", "No doctor logged in. Returning to menu.");
            loadScene("fxml/menu.fxml");
            return;
        }

        currentMonth = YearMonth.now();
        // Load appointments relevant to this doctor to check against dates
        loadDoctorAppointments();
        updateCalendar();

        // Optional: Hide doctor-specific side menu items if not logged in as doctor?
        // sideMenu.setVisible(currentDoctor != null);
    }

    private void loadDoctorAppointments() {
        // Load appointments only for the current doctor for the relevant period
        // For simplicity, load all for now, but could optimize later
        this.doctorAppointments = DataService.getAppointmentsForDoctor(currentDoctor.getID());
    }


    private void updateMonthLabel() {
        monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + currentMonth.getYear());
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear(); // Clear previous calendar
        updateMonthLabel();

        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        int firstDayOfWeekValue = firstDayOfMonth.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday

        // Add day headers (Mon, Tue, etc.)
        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < dayNames.length; i++) {
            Label dayHeader = new Label(dayNames[i]);
            dayHeader.setStyle("-fx-font-weight: bold; -fx-padding: 5px; -fx-font-size: 16px;"); // Example style
            GridPane.setHalignment(dayHeader, HPos.CENTER);
            calendarGrid.add(dayHeader, i, 0); // Add to row 0
        }

        int daysInMonth = currentMonth.lengthOfMonth();
        int dayOfMonth = 1;
        int row = 1; // Start adding days from row 1

        // Iterate through grid cells for the month
        while (dayOfMonth <= daysInMonth) {
            for (int col = 0; col < 7; col++) {
                // Offset columns based on the first day of the week
                if (row == 1 && col < firstDayOfWeekValue -1) {
                    continue; // Skip cells before the first day
                }

                if (dayOfMonth > daysInMonth) {
                    break; // Stop if we've added all days
                }

                LocalDate currentDate = currentMonth.atDay(dayOfMonth);
                Button dayButton = new Button(String.valueOf(dayOfMonth));
                dayButton.setPrefSize(120, 100); // Adjust size as needed
                dayButton.setStyle("-fx-font-size: 14px; -fx-alignment: top-left;"); // Basic style

                // Check if there's an appointment for the current doctor on this date
                final Appointment appointmentOnDate = findAppointmentOnDate(currentDate); // Final for lambda

                if (appointmentOnDate != null) {
                    // Highlight the button and set its action
                    dayButton.setStyle(dayButton.getStyle() + "-fx-background-color: #90EE90;"); // Light green highlight
                    ObservableList<Appointment> singleAppointmentList = FXCollections.observableArrayList(appointmentOnDate);
                    dayButton.setOnAction(event -> DialogLoader.showEditAppointmentDialog(singleAppointmentList, 2)); // Mode 2 for View
                } else {
                    // Optional: Add action to create new appointment?
                    // dayButton.setOnAction(event -> handleEmptyDayClick(currentDate));
                }

                calendarGrid.add(dayButton, col, row);
                dayOfMonth++;
            }
            if (dayOfMonth > daysInMonth) {
                break; // Exit outer loop as well
            }
            row++;
        }
    }

    // Helper to find if the current doctor has an appointment on a specific date
    private Appointment findAppointmentOnDate(LocalDate date) {
        if (doctorAppointments == null) return null;
        for (Appointment appt : doctorAppointments) {
            // Ensure Appointment has a getLocalDate() method or similar
            if (appt.getLocalDate() != null && appt.getLocalDate().equals(date)) {
                return appt;
            }
        }
        return null;
    }


    @FXML
    void handlePrevMonth(ActionEvent event) {
        currentMonth = currentMonth.minusMonths(1);
        // Consider reloading appointments if month changes significantly (optimization)
        updateCalendar();
    }

    @FXML
    void handleNextMonth(ActionEvent event) {
        currentMonth = currentMonth.plusMonths(1);
        updateCalendar();
    }

    //--- Side Menu Handlers ---
    @FXML void handleDoctorProfileSideButton(ActionEvent event) {
        if (currentDoctor != null) {
            ObservableList<Doctor> doctorList = FXCollections.observableArrayList(currentDoctor);
            DialogLoader.showEditDoctorDialog(doctorList, 1); // Mode 1 for Edit
        }
    }
    @FXML void handleDoctorPatientsSideButton(ActionEvent event) { loadScene("fxml/doctor_patients_table.fxml"); }
    @FXML void handleDiagnosesSideButton(ActionEvent event) { DialogLoader.showDiagnoseWindow(); }
    @FXML void handleBackSideButton(ActionEvent event) { loadScene("fxml/doctor_dashboard.fxml"); }
    @FXML void handleLogoutSideButton(ActionEvent event) { SessionManager.logout(); loadScene("fxml/menu.fxml"); }


    private void loadScene(String fxmlPath) {
        try {
            Main.loadScene(fxmlPath);
        } catch (IOException e) {
            System.err.println("Failed to load scene: " + fxmlPath + " - " + e.getMessage());
            UIUtils.showError("Navigation Error", "Could not load the requested screen.");
        }
    }

}
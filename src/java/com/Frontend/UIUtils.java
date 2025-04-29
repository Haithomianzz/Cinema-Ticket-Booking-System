//package com.Frontend;
//
//import com.Backend.Entities.Appointment;
//import com.Backend.Entities.Doctor;
//import com.Backend.Entities.Patient;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
//import javafx.scene.control.TextField;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//import javafx.stage.Window;
//
//import java.io.IOException;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * Utility class for common UI operations like showing alerts.
// */
//public class UIUtils {
//
//    // Basic styles (Consider moving to CSS)
//    public static final String H1 = "-fx-font-size: 24px; -fx-font-weight: bold;";
//    public static final String H2 = "-fx-font-size: 18px; -fx-font-weight: bold;";
//    public static final String H3 = "-fx-font-size: 16px;";
//    public static final String ButtonStyle = "-fx-font-size: 16px; -fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 10 20;";
//    public static final String Checkmark = "-fx-font-size: 16px;"; // Placeholder for CheckBox style if needed
//
//
//    /**
//     * Shows a standard information alert dialog.
//     * @param title The title of the alert window.
//     * @param content The main message text of the alert.
//     */
//    public static void showAlert(String title, String content) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null); // No header text
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//
//    /**
//     * Shows a standard warning alert dialog.
//     * @param title The title of the alert window.
//     * @param content The main message text of the alert.
//     */
//    public static void showWarning(String title, String content) {
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//
//    /**
//     * Shows a standard error alert dialog.
//     * @param title The title of the alert window.
//     * @param content The main message text of the alert.
//     */
//    public static void showError(String title, String content) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//
//    /**
//     * Shows a confirmation dialog.
//     * @param title The title of the dialog window.
//     * @param content The confirmation question.
//     * @return true if the user clicked OK, false otherwise.
//     */
//    public static boolean showConfirmation(String title, String content) {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//
//        Optional<ButtonType> result = alert.showAndWait();
//        return result.isPresent() && result.get() == ButtonType.OK;
//    }
//
//    /**
//     * Checks if any text field in the given array is empty.
//     * From original Main.java.
//     * @param fields Array of TextFields to check.
//     * @return true if any field is empty, false otherwise.
//     */
//    public static boolean checkEmptyForm(TextField[] fields) {
//        for (TextField field : fields) {
//            if (field == null || field.getText() == null || field.getText().trim().isEmpty()) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
//
//// ========================================================================
//
///**
// * Manages the current user session state.
// */
//public class SessionManager {
//    private static String userType; // "Admin" or "Doctor"
//    private static Doctor currentDoctor;
//    private static ObservableList<Patient> doctorPatients; // Patients for the logged-in doctor
//
//    public static String getUserType() {
//        return userType;
//    }
//
//    public static void setUserType(String type) {
//        userType = type;
//    }
//
//    public static Doctor getCurrentDoctor() {
//        return currentDoctor;
//    }
//
//    public static void setCurrentDoctor(Doctor doctor) {
//        currentDoctor = doctor;
//        // When setting the doctor, also load their patients
//        if (doctor != null) {
//            loadDoctorPatients();
//        } else {
//            doctorPatients = null; // Clear patients if no doctor
//        }
//    }
//
//    public static ObservableList<Patient> getDoctorPatients() {
//        // Ensure patients are loaded if accessed directly and currentDoctor is set
//        if (doctorPatients == null && currentDoctor != null) {
//            loadDoctorPatients();
//        }
//        return doctorPatients;
//    }
//
//    public static void setDoctorPatients(ObservableList<Patient> patients) {
//        doctorPatients = patients;
//    }
//
//    // Renamed from original Main.java method
//    public static void loadDoctorPatients() {
//        if (currentDoctor != null) {
//            Set<Patient> patientSet = DataService.getPatientsForDoctor(currentDoctor.getID());
//            doctorPatients = FXCollections.observableArrayList(patientSet);
//        } else {
//            doctorPatients = FXCollections.observableArrayList(); // Empty list
//        }
//    }
//
//    public static void addPatientToSession(Patient patient) {
//        if (doctorPatients != null && patient != null && currentDoctor != null && patient.getDoctorInCharge() == currentDoctor.getID()) {
//            doctorPatients.add(patient);
//        }
//    }
//
//    public static void removePatientFromSession(Patient patient) {
//        if (doctorPatients != null && patient != null) {
//            doctorPatients.remove(patient);
//        }
//    }
//
//
//    public static void logout() {
//        userType = null;
//        currentDoctor = null;
//        doctorPatients = null;
//    }
//}
//
//// ========================================================================
//
///**
// * Placeholder for authentication logic.
// * Replace with actual database/credential checking.
// */
//public class AuthService {
//    // From LoginFormController example - adapt as needed
//    static boolean verifyCredentials(String username, String password, String userType) {
//        System.out.println("Verifying: " + username + "/" + password + " as " + userType);
//        // --- !!! DUMMY LOGIC - REPLACE WITH REAL AUTHENTICATION !!! ---
//        if ("Admin".equals(userType) && "admin".equals(username) && "pass".equals(password)) {
//            SessionManager.setUserType("Admin");
//            SessionManager.setCurrentDoctor(null); // No doctor for admin
//            return true;
//        } else if ("Doctor".equals(userType) && "doctor".equals(username) && "pass".equals(password)) {
//            // Simulate fetching the logged-in doctor
//            // In real app, query database based on username/password
//            Doctor loggedInDoctor = DataService.findDoctorByCredentials(username, password); // Example method needed in DataService
//            if (loggedInDoctor != null) {
//                SessionManager.setUserType("Doctor");
//                SessionManager.setCurrentDoctor(loggedInDoctor); // This also loads patients via setter
//                return true;
//            }
//            return false; // Doctor credentials not found
//        }
//        return false; // Invalid userType or credentials
//    }
//
//    // Method to get the doctor object after successful login verification.
//    // Not strictly needed if SessionManager.setCurrentDoctor is called within verifyCredentials.
//    static Doctor getLoggedInDoctor() {
//        return SessionManager.getCurrentDoctor();
//    }
//}
//
//// ========================================================================
//
///**
// * Placeholder service for interacting with backend data entities.
// * Wraps static methods from Patient, Doctor, Appointment classes.
// */
//public class DataService {
//
//    // --- Patient Methods ---
//    public static Set<Patient> getPatients() {
//        return Patient.loadPatients(); // Assumes static method exists
//    }
//
//    public static Set<Patient> getPatientsForDoctor(int doctorId) {
//        // Assumes Patient.loadPatients() returns all, then filter
//        return Patient.loadPatients().stream()
//                .filter(p -> p.getDoctorInCharge() == doctorId)
//                .collect(Collectors.toSet());
//    }
//
//    public static Set<Patient> getPatientsInRooms() {
//        return Patient.loadPatients().stream()
//                .filter(Patient::isEmergency) // Assuming isEmergency means in a room
//                .collect(Collectors.toSet());
//    }
//
//
//    public static boolean addPatient(Patient patient) {
//        return Patient.add(patient); // Assumes static method exists
//    }
//
//    public static boolean savePatient(Patient patient) {
//        return Patient.save(patient); // Assumes static method exists
//    }
//
//    public static boolean deletePatient(Patient patient) {
//        return Patient.delete(patient); // Assumes static method exists
//    }
//
//    public static boolean saveAllPatients(ObservableList<Patient> patients) {
//        // Placeholder: In reality, might save individually or batch update
//        System.out.println("Attempting to save " + patients.size() + " patients...");
//        boolean allSaved = true;
//        for(Patient p : patients) {
//            if (!Patient.save(p)) { // Assumes save returns success/failure
//                allSaved = false;
//                System.err.println("Failed to save patient ID: " + p.getID());
//                // Optionally break or continue saving others
//            }
//        }
//        return allSaved;
//    }
//
//
//    // --- Doctor Methods ---
//    public static Set<Doctor> getDoctors() {
//        return Doctor.loadDoctors(); // Assumes static method exists
//    }
//
//    // Example method needed by AuthService
//    public static Doctor findDoctorByCredentials(String username, String password) {
//        // !!! DUMMY - Replace with actual lookup !!!
//        if ("doctor".equals(username) && "pass".equals(password)) {
//            // Find the specific doctor - requires a lookup method in Doctor or here
//            // For now, return a dummy doctor if credentials match the hardcoded ones
//            Set<Doctor> allDocs = Doctor.loadDoctors();
//            // Find a specific doctor (e.g., the first one for demo)
//            return allDocs.stream().findFirst().orElse(
//                    new Doctor("Dr. Placeholder", "123 Doc St", "555-4321", "General") {{ setID(99); }} // Dummy doctor if load fails
//            );
//        }
//        return null;
//    }
//
//    public static boolean addDoctor(Doctor doctor) {
//        return Doctor.add(doctor); // Assumes static method exists
//    }
//
//    public static boolean saveDoctor(Doctor doctor) {
//        return Doctor.save(doctor); // Assumes static method exists
//    }
//
//    public static boolean deleteDoctor(Doctor doctor) {
//        return Doctor.delete(doctor); // Assumes static method exists
//    }
//
//    public static boolean saveAllDoctors(ObservableList<Doctor> doctors) {
//        System.out.println("Attempting to save " + doctors.size() + " doctors...");
//        boolean allSaved = true;
//        for(Doctor d : doctors) {
//            if (!Doctor.save(d)) {
//                allSaved = false;
//                System.err.println("Failed to save doctor ID: " + d.getID());
//            }
//        }
//        return allSaved;
//    }
//
//    // --- Appointment Methods ---
//    public static Set<Appointment> getAppointments() {
//        return Appointment.loadAppointments(); // Assumes static method exists
//    }
//
//    public static Set<Appointment> getAppointmentsForDoctor(int doctorId) {
//        // Assumes Appointment.loadAppointments() returns all, then filter
//        return Appointment.loadAppointments().stream()
//                .filter(a -> a.getDoctorId() == doctorId)
//                .collect(Collectors.toSet());
//    }
//
//    public static boolean addAppointment(Appointment appointment) {
//        return Appointment.add(appointment); // Assumes static method exists
//    }
//
//    public static boolean saveAppointment(Appointment appointment) {
//        // Assuming Appointment class has an EditAppointment method or similar save logic
//        // This might be handled directly by appointment.EditAppointment(...) in the controller
//        System.out.println("Saving appointment ID: " + appointment.getID());
//        // return Appointment.save(appointment); // If a generic save exists
//        return true; // Placeholder
//    }
//
//
//    public static boolean deleteAppointment(Appointment appointment) {
//        return Appointment.delete(appointment); // Assumes static method exists
//    }
//
//    public static boolean saveAllAppointments(ObservableList<Appointment> appointments) {
//        System.out.println("Attempting to save " + appointments.size() + " appointments...");
//        boolean allSaved = true;
//        for(Appointment a : appointments) {
//            // This might not be needed if add/edit/delete handle saving
//            // if (!saveAppointment(a)) { // Placeholder
//            //     allSaved = false;
//            //     System.err.println("Failed to save appointment ID: " + a.getID());
//            // }
//        }
//        return allSaved; // Placeholder
//    }
//}
//
//
//// ========================================================================
//
///**
// * Utility class for loading FXML dialog windows.
// */
//public class DialogLoader {
//
//    /**
//     * Loads and shows a modal dialog window from an FXML file.
//     *
//     * @param fxmlPath Path to the FXML file (relative to resources).
//     * @param title    Title for the dialog window.
//     * @param owner    The owner window (can be null).
//     * @return The controller instance associated with the loaded FXML, or null if loading fails.
//     */
//    private static <T> T showDialog(String fxmlPath, String title, Window owner) {
//        try {
//            FXMLLoader loader = new FXMLLoader(DialogLoader.class.getResource(fxmlPath));
//            Parent root = loader.load();
//
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle(title);
//            dialogStage.initModality(Modality.APPLICATION_MODAL);
//            if (owner != null) {
//                dialogStage.initOwner(owner);
//            }
//            Scene scene = new Scene(root);
//            dialogStage.setScene(scene);
//
//            // Make the controller available before showing
//            T controller = loader.getController();
//
//            dialogStage.showAndWait(); // Show dialog and wait for it to close
//
//            return controller; // Return controller if needed (e.g., to get results)
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            UIUtils.showError("Dialog Load Error", "Failed to load dialog: " + fxmlPath + "\n" + e.getMessage());
//            return null;
//        } catch (Exception e) {
//            e.printStackTrace();
//            UIUtils.showError("Dialog Error", "An unexpected error occurred while loading the dialog: " + title + "\n" + e.getMessage());
//            return null;
//        }
//    }
//
//    // --- Specific Dialog Loaders ---
//
//    public static void showEditPatientDialog(ObservableList<Patient> patientList, int mode) {
//        try {
//            FXMLLoader loader = new FXMLLoader(DialogLoader.class.getResource("/com/Frontend/fxml/edit_patient.fxml"));
//            Parent root = loader.load();
//
//            EditPatientController controller = loader.getController();
//            controller.setPatientData(patientList, mode); // Pass data BEFORE showing
//
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle((mode == 0) ? "Add New Patient" : "Edit Patient");
//            dialogStage.initModality(Modality.APPLICATION_MODAL);
//            // dialogStage.initOwner(owner); // Optional: Set owner window
//            Scene scene = new Scene(root);
//            dialogStage.setScene(scene);
//            dialogStage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            UIUtils.showError("Dialog Load Error", "Failed to load Edit Patient dialog.");
//        }
//    }
//
//    public static void showEditDoctorDialog(ObservableList<Doctor> doctorList, int mode) {
//        try {
//            FXMLLoader loader = new FXMLLoader(DialogLoader.class.getResource("/com/Frontend/fxml/edit_doctor.fxml"));
//            Parent root = loader.load();
//
//            EditDoctorController controller = loader.getController();
//            controller.setDoctorData(doctorList, mode);
//
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle((mode == 0) ? "Add New Doctor" : "Edit Doctor");
//            dialogStage.initModality(Modality.APPLICATION_MODAL);
//            Scene scene = new Scene(root);
//            dialogStage.setScene(scene);
//            dialogStage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            UIUtils.showError("Dialog Load Error", "Failed to load Edit Doctor dialog.");
//        }
//    }
//
//    public static void showEditAppointmentDialog(ObservableList<Appointment> appointmentList, int mode) {
//        try {
//            FXMLLoader loader = new FXMLLoader(DialogLoader.class.getResource("/com/Frontend/fxml/edit_appointment.fxml"));
//            Parent root = loader.load();
//
//            EditAppointmentController controller = loader.getController();
//            controller.setAppointmentData(appointmentList, mode);
//
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle((mode == 0) ? "Add New Appointment" : (mode == 1 ? "Edit Appointment" : "View Appointment"));
//            dialogStage.initModality(Modality.APPLICATION_MODAL);
//            Scene scene = new Scene(root);
//            dialogStage.setScene(scene);
//            dialogStage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            UIUtils.showError("Dialog Load Error", "Failed to load Edit Appointment dialog.");
//        }
//    }
//
//    public static void showDiagnoseWindow() {
//        // This window shows the TreeView
//        showDialog("/com/Frontend/fxml/diagnose_window.fxml", "Diagnoses Menu", null);
//    }
//
//    public static void showDiagnosePatientDialog(Patient patient, int mode) {
//        // This window shows the details for diagnosing/viewing diagnosis
//        try {
//            FXMLLoader loader = new FXMLLoader(DialogLoader.class.getResource("/com/Frontend/fxml/diagnose_patient.fxml"));
//            Parent root = loader.load();
//
//            DiagnosePatientController controller = loader.getController();
//            controller.setPatient(patient, mode); // Pass patient and mode (0=view, 1=edit/diagnose)
//
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle((mode == 0) ? "View Diagnosis" : "Diagnose Patient");
//            dialogStage.initModality(Modality.APPLICATION_MODAL);
//            Scene scene = new Scene(root);
//            dialogStage.setScene(scene);
//            dialogStage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            UIUtils.showError("Dialog Load Error", "Failed to load Diagnose Patient dialog.");
//        }
//    }
//}

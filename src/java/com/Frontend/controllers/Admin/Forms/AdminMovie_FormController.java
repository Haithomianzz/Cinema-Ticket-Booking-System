package com.Frontend.Controllers; // Adjust package name as needed

import com.Backend.Client; // Import your Client class
import com.Backend.Entities.Movie; // Import your Movie class
import javafx.collections.ObservableList; // Needed for CheckComboBox result
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image; // For previewing image
import javafx.scene.image.ImageView; // For previewing image
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox; // Import CheckComboBox from ControlsFX
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List; // Import List

public class AdminMovie_FormController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField durationField; // Expecting minutes as integer

    @FXML
    private TextField ratingField; // Expecting float/double

    @FXML
    private TextField languageField; // Expecting one of the Language enum values

    @FXML
    private TextArea descriptionArea;

    @FXML
    private DatePicker releaseDatePicker;

    // --- MODIFIED: Changed from ComboBox to CheckComboBox ---
    @FXML
    private CheckComboBox<Movie.Genre> genreCheckComboBox; // Changed type and name
    // --- END MODIFIED ---

    @FXML
    private Button importImageButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label statusLabel; // Optional: For showing feedback to the user

    @FXML
    private ImageView imageViewPreview; // Optional: To show the selected image

    private byte[] selectedImageData; // To store the raw image data
    private File selectedImageFile; // To store the selected file reference

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    @FXML
    void initialize() {
        // --- MODIFIED: Populate the CheckComboBox ---
        // Add all Genre enum values to the CheckComboBox
        genreCheckComboBox.getItems().setAll(Movie.Genre.values());
        // --- END MODIFIED ---

        statusLabel.setText("Please fill in the movie details.");
        addNumericValidationListener(durationField);
        addFloatValidationListener(ratingField);
    }

    @FXML
    void handleImportImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Movie Poster Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        Stage stage = (Stage) importImageButton.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            try {
                selectedImageData = Files.readAllBytes(selectedImageFile.toPath());
                statusLabel.setText("Selected image: " + selectedImageFile.getName());

                if (imageViewPreview != null) {
                    Image image = new Image(new ByteArrayInputStream(selectedImageData));
                    imageViewPreview.setImage(image);
                    imageViewPreview.setPreserveRatio(true);
                    imageViewPreview.setFitHeight(100);
                }

            } catch (IOException e) {
                System.err.println("Error reading image file: " + e.getMessage());
                e.printStackTrace();
                showErrorAlert("Image Read Error", "Could not read the selected image file.");
                selectedImageData = null;
                if (imageViewPreview != null) imageViewPreview.setImage(null);
            }
        } else {
            statusLabel.setText("Image selection cancelled.");
        }
    }

    @FXML
    void handleSaveMovie() {
        // 1. Retrieve data from fields
        String title = titleField.getText();
        String durationStr = durationField.getText();
        String ratingStr = ratingField.getText();
        String languageStr = languageField.getText();
        String description = descriptionArea.getText();
        LocalDate releaseDate = releaseDatePicker.getValue();

        // --- MODIFIED: Get selected genres from CheckComboBox ---
        // getCheckModel().getCheckedItems() returns an ObservableList<Movie.Genre>
        ObservableList<Movie.Genre> checkedGenres = genreCheckComboBox.getCheckModel().getCheckedItems();
        // --- END MODIFIED ---


        // 2. Validate Input
        StringBuilder validationErrors = new StringBuilder();
        int duration = 0;
        float rating = 0.0f;
        Movie.Language language = null;

        if (title == null || title.trim().isEmpty()) {
            validationErrors.append("Title cannot be empty.\n");
        }
        // ... (other validations remain the same) ...
        if (durationStr == null || durationStr.trim().isEmpty()) {
            validationErrors.append("Duration cannot be empty.\n");
        } else {
            try {
                duration = Integer.parseInt(durationStr.trim());
                if (duration <= 0) {
                    validationErrors.append("Duration must be a positive number.\n");
                }
            } catch (NumberFormatException e) {
                validationErrors.append("Duration must be a valid integer.\n");
            }
        }
        if (ratingStr == null || ratingStr.trim().isEmpty()) {
            validationErrors.append("Rating cannot be empty.\n");
        } else {
            try {
                rating = Float.parseFloat(ratingStr.trim());
                if (rating < 0.0f || rating > 10.0f) { // Example rating range
                    validationErrors.append("Rating must be between 0.0 and 10.0.\n");
                }
            } catch (NumberFormatException e) {
                validationErrors.append("Rating must be a valid number (e.g., 7.5).\n");
            }
        }
        if (languageStr == null || languageStr.trim().isEmpty()) {
            validationErrors.append("Language cannot be empty.\n");
        } else {
            try {
                language = Movie.Language.valueOf(languageStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                validationErrors.append("Invalid language specified. Valid options are: ")
                        .append(Arrays.toString(Movie.Language.values())).append("\n");
            }
        }
        if (description == null || description.trim().isEmpty()) {
            validationErrors.append("Description cannot be empty.\n");
        }
        if (releaseDate == null) {
            validationErrors.append("Release Date must be selected.\n");
        }

        // --- MODIFIED: Validate genre selection ---
        if (checkedGenres == null || checkedGenres.isEmpty()) {
            validationErrors.append("At least one Genre must be selected.\n");
        }
        // --- END MODIFIED ---

        if (selectedImageData == null || selectedImageData.length == 0) {
            validationErrors.append("An image must be imported.\n");
        }

        // If validation errors exist, show them and return
        if (validationErrors.length() > 0) {
            showErrorAlert("Validation Error", validationErrors.toString());
            return;
        }

        // 3. Prepare data for Movie object
        String releaseDateStr = releaseDate.format(DATE_FORMATTER);

        // --- MODIFIED: Convert ObservableList to ArrayList for Movie constructor ---
        ArrayList<Movie.Genre> genresList = new ArrayList<>(checkedGenres);
        // --- END MODIFIED ---


        // 4. Create Movie Object
        Movie newMovie = null;
        try {
            newMovie = new Movie(
                    title.trim(),
                    description.trim(),
                    rating,
                    language.name(), // Pass the string name
                    duration,
                    releaseDateStr,
                    genresList, // Pass the ArrayList of selected genres
                    selectedImageData
            );
        } catch (Exception e) {
            System.err.println("Error creating Movie object: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Creation Error", "Failed to create movie object: " + e.getMessage());
            return;
        }


        // 5. Save using Client
        boolean success = Client.addMovie(newMovie);

        // 6. Provide Feedback
        if (success) {
            showInformationAlert("Success", "Movie '" + newMovie.getTitle() + "' saved successfully!");
            clearForm();
        } else {
            showErrorAlert("Save Failed", "Could not save the movie to the database. Check logs for details.");
        }
    }

    @FXML
    void handleCancel() {
        clearForm();
        statusLabel.setText("Operation cancelled. Form cleared.");
    }

    // --- Helper Methods ---

    private void clearForm() {
        titleField.clear();
        durationField.clear();
        ratingField.clear();
        languageField.clear();
        descriptionArea.clear();
        releaseDatePicker.setValue(null);
        // --- MODIFIED: Clear CheckComboBox selection ---
        if (genreCheckComboBox != null) {
            genreCheckComboBox.getCheckModel().clearChecks();
        }
        // --- END MODIFIED ---
        selectedImageData = null;
        selectedImageFile = null;
        if (imageViewPreview != null) {
            imageViewPreview.setImage(null);
        }
        statusLabel.setText("Form cleared.");
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInformationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Basic validation listener for numeric fields
    private void addNumericValidationListener(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                field.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    // Basic validation listener for float fields
    private void addFloatValidationListener(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                String cleaned = newValue.replaceAll("[^\\d.]", "");
                int dotIndex = cleaned.indexOf('.');
                if (dotIndex != -1) {
                    String beforeDot = cleaned.substring(0, dotIndex + 1);
                    String afterDot = cleaned.substring(dotIndex + 1).replace(".", "");
                    cleaned = beforeDot + afterDot;
                }
                field.setText(cleaned);
            }
        });
    }
}

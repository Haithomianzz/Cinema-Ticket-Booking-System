package com.Frontend.controllers.Customer.Pages;
import com.Backend.Client;
import com.Backend.Entities.Movie;
import com.Frontend.AlertBox;
import com.Frontend.Main;
import com.Frontend.SceneController;
import com.Frontend.controllers.Customer.Cards.Movie_CardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashSet;

public class Home_PageController {
    @FXML
    private ScrollPane HP_MList;
    @FXML
    private Label C_Username;
    @FXML
    private VBox HP_Clist;
    @FXML
    private TextField HP_Search;

    private HashSet<Movie> allMovies; // Store all movies for filtering
    private Movie.Genre currentGenre = null; // Track the currently selected genre

    public void initialize() {
        if (Main.getCurrentUserType() != Main.UserType.GUEST) {
            C_Username.setText(Main.getCurrentUser().getName());
        }else{
            C_Username.setText("Guest");
        }

        // Get all movies
        allMovies = new HashSet<>(Client.getMovieMap().values());

        // Create a GridPane for movies
        GridPane movieGrid = new GridPane();
        movieGrid.setHgap(10);
        movieGrid.setVgap(10);
        movieGrid.setPadding(new javafx.geometry.Insets(10));

        // Populate the GridPane with all movies
        populateMovieGrid(movieGrid, allMovies, currentGenre, "");

        // Set the GridPane as the content of the ScrollPane
        HP_MList.setContent(movieGrid);

        // Add a listener to the search bar
        HP_Search.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update the movie grid with the current search query and genre
            populateMovieGrid(movieGrid, allMovies, currentGenre, newValue);
        });

        // Get unique genres
        HashSet<Movie.Genre> genres = new HashSet<>();
        for (Movie movie : allMovies) {
            if (movie.getShowtimes().isEmpty()) {
                continue; // Skip movies without showtimes
            }
            genres.addAll(movie.getGenres());
        }

        // Create buttons for each genre
        for (Movie.Genre genre : genres) {
            Button genreButton = new Button(genre.toString());
            genreButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;");
            genreButton.setOnMouseEntered(event -> genreButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-size: 14px;"));
            genreButton.setOnMouseExited(event -> genreButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;"));
            genreButton.setOnAction(event -> {
                // Update the current genre and refresh the movie grid
                currentGenre = genre;
                populateMovieGrid(movieGrid, allMovies, currentGenre, HP_Search.getText());
            });
            genreButton.setPrefWidth(250);
            genreButton.setPrefHeight(50);
            HP_Clist.getChildren().add(genreButton);
        }
    }

    // Method to populate the GridPane with movies
    private void populateMovieGrid(GridPane movieGrid, HashSet<Movie> movies, Movie.Genre filterGenre, String searchQuery) {
        movieGrid.getChildren().clear(); // Clear existing content

        int row = 0;
        int col = 0;

        for (Movie movie : movies) {
            // Filter by genre and search query
            if (movie.getShowtimes().isEmpty() ||
                    (filterGenre != null && !movie.getGenres().contains(filterGenre)) ||
                    !movie.getTitle().toLowerCase().contains(searchQuery.toLowerCase())) {
                continue; // Skip movies that don't match the criteria
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/Frontend/fxml/Customer/Cards/Movie_Card.fxml"));
                AnchorPane movieCard = loader.load();

                Movie_CardController controller = loader.getController();
                controller.setDate(movie);

                movieGrid.add(movieCard, col, row);

                col++;
                if (col == 4) { // Adjust the number of columns as needed
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void goToHomePage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToHome(event);
    }
    public void goToMoviesPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToMovies(event);
    }
    public void goToBookingPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToBookingPages(event);
    }
    public void goToTicketPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToTicketPage(event);
    }
    public void goToProfilePage(MouseEvent event) throws IOException {
        if (Main.getCurrentUserType() != Main.UserType.GUEST){
            SceneController sceneController = new SceneController();
            sceneController.SwitchToProfileForm(event);
        }
        else
            AlertBox.alert("Error", "You must be logged in to access this page!", "Close");
    }
    public void goToLoginPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToLogin(event);
    }
}
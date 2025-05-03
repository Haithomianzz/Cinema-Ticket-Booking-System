package com.Frontend.controllers.Admin.Pages;

import com.Backend.Client;
import com.Backend.Entities.Movie;
import com.Frontend.Main;
import com.Frontend.SceneController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminMoviesGenres_PageController {

    public static class MovieGenreDisplay {
        private final SimpleIntegerProperty movieId;
        private final SimpleStringProperty genre;
        private final Movie originalMovie; // Reference to the original movie

        public MovieGenreDisplay(int movieId, String genre, Movie originalMovie) {
            this.movieId = new SimpleIntegerProperty(movieId);
            this.genre = new SimpleStringProperty(genre);
            this.originalMovie = originalMovie;
        }

        public int getMovieId() {
            return movieId.get();
        }

        public SimpleIntegerProperty movieIdProperty() {
            return movieId;
        }

        public String getGenre() {
            return genre.get();
        }

        public SimpleStringProperty genreProperty() {
            return genre;
        }

        public Movie getOriginalMovie() {
            return originalMovie;
        }
    }


    @FXML
    private Label C_Username;
    @FXML
    private TextField Search;
    @FXML
    private TableView<Movie> MT;
    @FXML
    private TableColumn<Movie, Integer> MT_D;
    @FXML
    private TableColumn<Movie, String> MT_L;
    @FXML
    private TableColumn<Movie, Integer> MT_M_ID;
    @FXML
    private TableColumn<Movie, Float> MT_R;
    @FXML
    private TableColumn<Movie, String> MT_RD;
    @FXML
    private TableColumn<Movie, String> MT_T;
    @FXML
    private TableView<MovieGenreDisplay> MGT;
    @FXML
    private TableColumn<MovieGenreDisplay, String> MGT_G;
    @FXML
    private TableColumn<MovieGenreDisplay, Integer> MGT_M_ID;

    private ObservableList<Movie> movieList = FXCollections.observableArrayList();
    private ObservableList<MovieGenreDisplay> movieGenreList = FXCollections.observableArrayList(); // List for the genre table
    private FilteredList<Movie> filteredMovies;
    // No separate filtered list needed for genres, it depends on movie selection

    @FXML
    private void initialize() {

        if (Main.getCurrentUser() != null && Main.getCurrentUser().getName() != null) {
            C_Username.setText(Main.getCurrentUser().getName());
        } else {
            C_Username.setText("Admin"); // Fallback or default name
        }


        setupMovieTable();
        setupGenreTable();
        setupSearchFilter();
        loadData(); // Load initial data

        // Add listener to movie table selection
        MT.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            filterGenresByMovie(newSelection);
        });
    }

    private void setupMovieTable() {
        MT_M_ID.setCellValueFactory(new PropertyValueFactory<>("movieId"));
        MT_T.setCellValueFactory(new PropertyValueFactory<>("title"));
        MT_R.setCellValueFactory(new PropertyValueFactory<>("rating"));
        MT_D.setCellValueFactory(new PropertyValueFactory<>("duration"));

        MT_RD.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReleaseDate().toString()));
        MT_L.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLanguage().toString()));

        filteredMovies = new FilteredList<>(movieList, p -> true); // Initially show all movies
        SortedList<Movie> sortedMovies = new SortedList<>(filteredMovies);
        sortedMovies.comparatorProperty().bind(MT.comparatorProperty());
        MT.setItems(sortedMovies);
        MT.setPlaceholder(new Label("No movies found."));
    }

    private void setupGenreTable() {
        MGT_M_ID.setCellValueFactory(new PropertyValueFactory<>("movieId"));
        MGT_G.setCellValueFactory(new PropertyValueFactory<>("genre"));

        SortedList<MovieGenreDisplay> sortedGenres = new SortedList<>(movieGenreList);
        sortedGenres.comparatorProperty().bind(MGT.comparatorProperty());
        MGT.setItems(sortedGenres);
        MGT.setPlaceholder(new Label("Select a movie to view its genres."));
    }

    private void loadData() {
        movieList.setAll(Client.getMovieMap().values());
        // Initially clear genre list or show genres for the first movie if auto-selection is desired
        filterGenresByMovie(null); // Clear genre table initially
        MT.refresh();
    }

    private void refreshTables() {
        // Store selection before refresh
        Movie selectedMovie = MT.getSelectionModel().getSelectedItem();

        // Re-fetch data and update lists
        movieList.setAll(Client.getMovieMap().values());
        MT.refresh(); // Refresh movie table first

        // Re-apply selection and filter genres
        if (selectedMovie != null) {
            // Find the equivalent movie in the updated list (match by ID)
            Movie reselectedMovie = movieList.stream()
                    .filter(m -> m.getMovieId() == selectedMovie.getMovieId())
                    .findFirst()
                    .orElse(null);
            if (reselectedMovie != null) {
                MT.getSelectionModel().select(reselectedMovie);
                filterGenresByMovie(reselectedMovie); // Update genre table based on re-selection
            } else {
                filterGenresByMovie(null); // Clear genre table if movie was deleted
            }
        } else {
            filterGenresByMovie(null); // Clear genre table if nothing was selected
        }
    }

    private void filterGenresByMovie(Movie selectedMovie) {
        movieGenreList.clear(); // Clear previous genres
        if (selectedMovie != null && selectedMovie.getGenres() != null) {
            movieGenreList.setAll(selectedMovie.getGenres().stream()
                    .map(genre -> new MovieGenreDisplay(selectedMovie.getMovieId(), genre.toString(), selectedMovie))
                    .collect(Collectors.toList()));
        }
        MGT.refresh(); // Ensure the table view is refreshed
    }

    private void setupSearchFilter() {
        Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredMovies.setPredicate(movie -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all if search is empty
                }
                String lowerCaseFilter = newValue.toLowerCase();

                // Search by Title
                if (movie.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                // Search by Language
                if (movie.getLanguage().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                // Search by Rating (convert float to string)
                if (String.valueOf(movie.getRating()).contains(lowerCaseFilter)) {
                    return true;
                }
                // Search by Duration (convert int to string)
                if (String.valueOf(movie.getDuration()).contains(lowerCaseFilter)) {
                    return true;
                }
                // Search by Release Date (convert date to string)
                if (movie.getReleaseDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                // Search by Genre (check if any genre matches)
                if (movie.getGenres() != null && movie.getGenres().stream()
                        .anyMatch(genre -> genre.toString().toLowerCase().contains(lowerCaseFilter))) {
                    return true;
                }

                return false; // Does not match
            });

            // Clear genre table when search changes, as movie selection might become invalid
            filterGenresByMovie(MT.getSelectionModel().getSelectedItem());
        });
    }
    private void handleEditMovie(ActionEvent event, Movie movie) {
        try {
            SceneController.SwitchToAdminMovieForm(event, movie); // Pass the selected movie
            refreshTables(); // Refresh after the form is closed
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open edit form.").showAndWait();
        }
    }
    private void handleDeleteMovie(Movie movie) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Movie '" + movie.getTitle() + "'?\nThis will also remove associated showtimes and tickets.", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (Client.removeMovie(movie)) {
                    // No need to manually remove from movieList, refreshTables handles it
                    refreshTables();
                    new Alert(Alert.AlertType.INFORMATION, "Movie deleted successfully.").showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete movie.").showAndWait();
                }
            }
        });
    }



    public void add(ActionEvent event) throws IOException {
        // Assuming SceneController has a method to show the movie form for adding
        SceneController.SwitchToAdminMovieForm(event, null); // Pass null for adding
        refreshTables(); // Refresh after the form is closed (potentially added a movie)
    }


    public void edit(ActionEvent event) throws IOException {
        Movie selectedMovie = MT.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            handleEditMovie(event, selectedMovie);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a movie to edit.");
            alert.showAndWait();
        }
    }

    public void delete(ActionEvent event) {
        Movie selectedMovie = MT.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            handleDeleteMovie(selectedMovie);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a movie to delete.");
            alert.showAndWait();
        }
    }





    public void goToAdminBookingsTicketsPage(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminBookingsTickets(event);
    }
    public void goToAdminCustomersPage(ActionEvent event) throws IOException {
        SceneController.SwitchToAdminCustomers(event);
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
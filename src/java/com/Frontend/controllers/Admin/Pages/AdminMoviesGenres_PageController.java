//package com.Frontend.controllers.Admin.Pages;
//
//import com.Backend.Client;
//import com.Backend.Entities.Movie;
//import com.Frontend.SceneController;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.collections.transformation.FilteredList;
//import javafx.collections.transformation.SortedList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.geometry.Pos;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
//import javafx.util.Callback;
//
//import java.io.IOException;
//
//public class AdminMoviesGenres_PageController {
//
//    @FXML
//    private Label C_Username;
//    @FXML
//    private TextField Search;
//    @FXML
//    private TableView<Movie> MT;
//    @FXML
//    private TableColumn<Movie, Void> MT_Action;
//    @FXML
//    private TableColumn<Movie, String> MT_D;
//    @FXML
//    private TableColumn<Movie, String> MT_L;
//    @FXML
//    private TableColumn<Movie, Integer> MT_M_ID;
//    @FXML
//    private TableColumn<Movie, String> MT_R;
//    @FXML
//    private TableColumn<Movie, String> MT_RD;
//    @FXML
//    private TableColumn<Movie, String> MT_T;
//    @FXML
//    private TableView<Genre> MGT;
//    @FXML
//    private TableColumn<Genre, Void> MGT_Action;
//    @FXML
//    private TableColumn<Genre, String> MGT_G;
//    @FXML
//    private TableColumn<Genre, Integer> MGT_M_ID;
//
//    private ObservableList<Movie> movieList = FXCollections.observableArrayList();
//    private ObservableList<Genre> genreList = FXCollections.observableArrayList();
//    private FilteredList<Movie> filteredMovies;
//    private FilteredList<Genre> filteredGenres;
//
//    @FXML
//    private void initialize() {
//        setupMovieTable();
//        setupGenreTable();
//        setupSearchFilter();
//    }
//
//    private void setupMovieTable() {
//        MT_M_ID.setCellValueFactory(new PropertyValueFactory<>("movieId"));
//        MT_D.setCellValueFactory(new PropertyValueFactory<>("description"));
//        MT_L.setCellValueFactory(new PropertyValueFactory<>("language"));
//        MT_R.setCellValueFactory(new PropertyValueFactory<>("rating"));
//        MT_RD.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
//        MT_T.setCellValueFactory(new PropertyValueFactory<>("title"));
//
//        Callback<TableColumn<Movie, Void>, TableCell<Movie, Void>> movieActionCellFactory = param -> {
//            final TableCell<Movie, Void> cell = new TableCell<>() {
//                private final Button deleteButton = new Button("Delete");
//
//                {
//                    deleteButton.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white;");
//                    deleteButton.setOnAction(event -> {
//                        Movie movie = getTableView().getItems().get(getIndex());
//                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Movie " + movie.getTitle() + "?", ButtonType.YES, ButtonType.NO);
//                        alert.showAndWait().ifPresent(response -> {
//                            if (response == ButtonType.YES) {
//                                if (Client.removeMovie(movie)) {
//                                    movieList.remove(movie);
//                                    MT.refresh();
//                                } else {
//                                    new Alert(Alert.AlertType.ERROR, "Failed to delete movie.").showAndWait();
//                                }
//                            }
//                        });
//                    });
//                }
//
//                @Override
//                public void updateItem(Void item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty) {
//                        setGraphic(null);
//                    } else {
//                        HBox buttons = new HBox(deleteButton);
//                        buttons.setSpacing(10);
//                        buttons.setAlignment(Pos.CENTER);
//                        setGraphic(buttons);
//                    }
//                }
//            };
//            return cell;
//        };
//        MT_Action.setCellFactory(movieActionCellFactory);
//
//        filteredMovies = new FilteredList<>(movieList, p -> true);
//        SortedList<Movie> sortedMovies = new SortedList<>(filteredMovies);
//        sortedMovies.comparatorProperty().bind(MT.comparatorProperty());
//        MT.setItems(sortedMovies);
//        MT.setPlaceholder(new Label("No movies found."));
//    }
//
//    private void setupGenreTable() {
//        MGT_M_ID.setCellValueFactory(new PropertyValueFactory<>("movieId"));
//        MGT_G.setCellValueFactory(new PropertyValueFactory<>("genre"));
//
//        Callback<TableColumn<Genre, Void>, TableCell<Genre, Void>> genreActionCellFactory = param -> {
//            final TableCell<Genre, Void> cell = new TableCell<>() {
//                private final Button deleteButton = new Button("Delete");
//
//                {
//                    deleteButton.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white;");
//                    deleteButton.setOnAction(event -> {
//                        Genre genre = getTableView().getItems().get(getIndex());
//                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Genre " + genre.getGenre() + "?", ButtonType.YES, ButtonType.NO);
//                        alert.showAndWait().ifPresent(response -> {
//                            if (response == ButtonType.YES) {
//                                if (Client.removeGenre(genre)) {
//                                    genreList.remove(genre);
//                                    MGT.refresh();
//                                } else {
//                                    new Alert(Alert.AlertType.ERROR, "Failed to delete genre.").showAndWait();
//                                }
//                            }
//                        });
//                    });
//                }
//
//                @Override
//                public void updateItem(Void item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty) {
//                        setGraphic(null);
//                    } else {
//                        HBox buttons = new HBox(deleteButton);
//                        buttons.setSpacing(10);
//                        buttons.setAlignment(Pos.CENTER);
//                        setGraphic(buttons);
//                    }
//                }
//            };
//            return cell;
//        };
//        MGT_Action.setCellFactory(genreActionCellFactory);
//
//        filteredGenres = new FilteredList<>(genreList, p -> true);
//        SortedList<Genre> sortedGenres = new SortedList<>(filteredGenres);
//        sortedGenres.comparatorProperty().bind(MGT.comparatorProperty());
//        MGT.setItems(sortedGenres);
//        MGT.setPlaceholder(new Label("No genres found."));
//    }
//
//    private void setupSearchFilter() {
//        Search.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredMovies.setPredicate(movie -> {
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                String lowerCaseFilter = newValue.toLowerCase();
//                return movie.getTitle().toLowerCase().contains(lowerCaseFilter) ||
//                        movie.getDescription().toLowerCase().contains(lowerCaseFilter);
//            });
//
//            filteredGenres.setPredicate(genre -> {
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                String lowerCaseFilter = newValue.toLowerCase();
//                return genre.getGenre().toLowerCase().contains(lowerCaseFilter);
//            });
//        });
//    }
//
//    public void refreshTables() {
//        movieList.setAll(Client.getMovieMap().values());
//        genreList.setAll(Client.ge);
//        MT.refresh();
//        MGT.refresh();
//    }
//
//
//    public void goToAdminBookingsTicketsPage(ActionEvent event) throws IOException {
//        SceneController.SwitchToAdminBookingsTickets(event);
//    }
//    public void goToAdminCustomersPage(ActionEvent event) throws IOException {
//        SceneController.SwitchToAdminCustomers(event);
//    }
//    public void goToAdminHallsSeatsPage(ActionEvent event) throws IOException {
//        SceneController.SwitchToAdminHallsSeats(event);
//    }
//    public void goToAdminMovesGenresPage(ActionEvent event) throws IOException {
//        SceneController.SwitchToAdminMovesGenres(event);
//    }
//    public void goToAdminShowsSeatsPage(ActionEvent event) throws IOException {
//        SceneController.SwitchToAdminShowsSeats(event);
//    }
//    public void goToLoginPage(ActionEvent event) throws IOException {
//        SceneController.SwitchToLogin(event);
//    }
//
//}
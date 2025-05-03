package com.Frontend.controllers.Customer.Pages;

import com.Backend.Entities.Customer;
import com.Backend.Entities.Movie;
import com.Backend.Entities.Showtime;
import com.Frontend.AlertBox;
import com.Frontend.Main;
import com.Frontend.SceneController;
import com.Frontend.controllers.Customer.Cards.Movie_CardController;
import com.Frontend.controllers.Customer.Cards.Shows_CardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;

public class MovieDetail_PageController {

    @FXML
    private Label C_Username;
    @FXML
    private ImageView MDP_MImage;
    @FXML
    private Label MDP_Title;
    @FXML
    private Label MDP_Description;
    @FXML
    private Label MDP_Duration;
    @FXML
    private Label MDP_Language;
    @FXML
    private Label MDP_Mrate;
    @FXML
    private Label MDP_RDate;
    @FXML
    private HBox MDP_LGenre;
    @FXML
    private ScrollPane MDP_Shows;

    private Movie movie;
    private Customer customer;

    public void initialize() {
        customer = Main.getCurrentUser();
        if (customer != null) {
            C_Username.setText(customer.getName());
        } else {
            C_Username.setText("Guest");
        }
    }

    public void setMovie(Movie movie) throws IOException {
        MDP_Title.setText(movie.getTitle());
        MDP_Mrate.setText(movie.getRating().toString());
        MDP_Language.setText(movie.getLanguage().toString());
        MDP_Description.setText(movie.getDescription());
        MDP_RDate.setText(movie.getReleaseDate().toString());
        MDP_Duration.setText(movie.getDuration() + " min");
        MDP_MImage.setImage(new Image(new ByteArrayInputStream(movie.getImageData())));

        for(Movie.Genre genre : movie.getGenres()) {
            Label genreLabel = new Label(genre.toString());
            genreLabel.setStyle("-fx-text-fill: white;" +
                            "-fx-font-size: 12px;" +
                            "-fx-background-color: #007bff;" +
                            "-fx-background-radius: 15px;" +
                            "-fx-padding: 5px 10px;"
            );
            MDP_LGenre.getChildren().add(genreLabel);
        }
        GridPane showGrid = new GridPane();
        showGrid.setHgap(10);
        showGrid.setVgap(10);
        showGrid.setPadding(new javafx.geometry.Insets(10));
        populateShowGrid(showGrid,movie);
        MDP_Shows.setContent(showGrid);
    }
    private void populateShowGrid(GridPane showGrid,Movie movie) throws IOException {
        showGrid.getChildren().clear();
        int col = 0;

        for (Showtime show : movie.getShowtimes()) {
            if (movie.getShowtimes().isEmpty() ) return;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/Frontend/fxml/Customer/Cards/Shows_Card.fxml"));
            VBox movieCard = loader.load();

            Shows_CardController controller = loader.getController();
            controller.setData(show);

            showGrid.add(movieCard, col, 0);
            col++;
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
        Main.setCurrentUser(null);
        Main.setCurrentUserType(Main.UserType.GUEST);
        SceneController sceneController = new SceneController();
        sceneController.SwitchToLogin(event);
    }
}
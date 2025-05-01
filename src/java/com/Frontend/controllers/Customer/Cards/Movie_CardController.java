package com.Frontend.controllers.Customer.Cards;

import com.Backend.Entities.Movie;
import com.Frontend.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Movie_CardController {
    @FXML
    private ImageView MC_Mimage;
    @FXML
    private Label MC_Mname;
    @FXML
    private Label MC_Mrate;
    private Movie movie; // Store the movie object

    public void setDate(Movie movie) throws IllegalArgumentException {
        this.movie = movie; // Set the movie object
        MC_Mname.setText(movie.getTitle());
        MC_Mrate.setText(movie.getRating().toString());
        MC_Mimage.setImage(new Image(new ByteArrayInputStream(movie.getImageData())));
    }

    public void goToMovieDetailPage(MouseEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToMovieDetail(event, movie);
    }

}


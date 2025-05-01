package com.Frontend.controllers.Customer.Pages;

import com.Backend.Entities.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class MovieDetail_PageController {
    @FXML
    private Label MDP_Title;
    @FXML
    private Label MDP_Mrate;
    @FXML
    private Label MDP_Language;
    @FXML
    private Label MDP_Description;
    @FXML
    private Label MDP_RDate;
    @FXML
    private Label MDP_Duration;
    @FXML
    private ImageView MDP_MImage;

    public void setMovie(Movie movie) {
        MDP_Title.setText(movie.getTitle());
        MDP_Mrate.setText(movie.getRating().toString());
        MDP_Language.setText(movie.getLanguage().toString());
        MDP_Description.setText(movie.getDescription());
        MDP_RDate.setText(movie.getReleaseDate().toString());
        MDP_Duration.setText(movie.getDuration() + " min");
        MDP_MImage.setImage(new Image(new ByteArrayInputStream(movie.getImageData())));
    }
}
package com.Frontend.controllers.Customer.Cards;

import com.Backend.Entities.Movie;
import com.Frontend.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Movie_CardController {
    @FXML
    private ImageView MC_Mimage;
    @FXML
    private Label MC_Mname;
    @FXML
    private Label MC_Mrate;

    public void setDate(Movie movie) throws IllegalArgumentException {
        MC_Mname.setText(movie.getTitle());
        MC_Mrate.setText(movie.getRating().toString());
        MC_Mimage.setImage(new ImageView( getClass().getResourceAsStream( movie.getImage() ) ));
    }

    public void goToMovieDetailPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToMovieDetail(event);
    }

}


package com.Frontend.controllers.Customer.Cards;

import com.Backend.Entities.Movie;
import com.Backend.Entities.Showtime;
import com.Frontend.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class Shows_CardController {

    @FXML
    private Label SC_Date;
    @FXML
    private Label SC_Day;
    @FXML
    private Label SC_Month;
    @FXML
    private Label SC_Time;


    public void setData(Showtime show) throws IllegalArgumentException {
//        SC_Date.setText(show.getDate());
//        SC_Day.setText(show.getDay());
//        SC_Month.setText(show.getMonth());
        SC_Time.setText(show.getShowTime());
    }

    public void goToShowForm(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../fxml/Customer/Forms/Show_Form.fxml"));
        Parent root = fxmlLoader.load();

        Stage newStage = new Stage();
        newStage.setTitle("Form");
        newStage.setScene(new Scene(root));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        newStage.show();
    }

}

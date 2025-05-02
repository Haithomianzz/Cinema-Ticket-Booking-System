package com.Frontend.controllers.Customer.Cards;

import com.Backend.Entities.Showtime;
import com.Frontend.SceneController;
import com.Frontend.controllers.Customer.Forms.Show_FormController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;

public class Shows_CardController {

    @FXML
    private Label SC_Date;
    @FXML
    private Label SC_Day;
    @FXML
    private Label SC_Month;
    @FXML
    private Label SC_Time;

    private Showtime show;

    public void setData(Showtime show) throws IllegalArgumentException {
        this.show = show;
        LocalDate date = show.getShowDate().getDate();
        String time = show.getShowTime();
        int hour = Integer.parseInt(time.substring(0, 2));
        String period = hour >= 12 ? "PM" : "AM";
        hour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
        String formattedTime = String.format("%d:%s %s", hour, time.substring(3, 5), period);

        SC_Date.setText(String.valueOf(date.getDayOfMonth()));
        SC_Day.setText(String.valueOf(date.getDayOfWeek()));
        SC_Month.setText(String.valueOf(date.getMonth()));
        SC_Time.setText(formattedTime);
    }

    public void goToShowForm(MouseEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToShowForm(event, show);
    }

}

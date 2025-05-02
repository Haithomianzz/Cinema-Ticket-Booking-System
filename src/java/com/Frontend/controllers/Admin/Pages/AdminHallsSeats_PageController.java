package com.Frontend.controllers.Admin.Pages;

import com.Frontend.Main;
import com.Frontend.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AdminHallsSeats_PageController {

    @FXML
    private Label C_Username;

    @FXML
    private TableView<?> HT;

    @FXML
    private TableColumn<?, ?> HT_Hall_number;

    @FXML
    private TableView<?> ST;

    @FXML
    private TableColumn<?, ?> ST_Action;

    @FXML
    private TableColumn<?, ?> ST_Row_number;

    @FXML
    private TextField Search;

    @FXML
    private TableColumn<?, ?> TH_Action;

    @FXML
    private TableColumn<?, ?> TH_Nseats;

    @FXML
    private TableColumn<?, ?> TS_Hall_number;

    @FXML
    private TableColumn<?, ?> TS_SId;

    @FXML
    private TableColumn<?, ?> TS_Seat_number;

//    void initialize() {
//        if (Main.getCurrentUser() != null) {
//            C_Username.setText(Main.getCurrentUser().getName());
//        } else {
//            C_Username.setText("Admin User");
//        }
//
//    }

    public void goToAdminBookingsTicketsPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToAdminBookingsTickets(event);
    }
    public void goToAdminCustomersPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToAdminCustomers(event);
    }
    public void goToAdminHallsSeatsPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToAdminHallsSeats(event);
    }
    public void goToAdminMovesGenresPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToAdminMovesGenres(event);
    }
    public void goToAdminShowsSeatsPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToAdminShowsSeats(event);
    }
    public void goToLoginPage(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.SwitchToLogin(event);
    }
}

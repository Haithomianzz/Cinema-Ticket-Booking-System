package com.Frontend.controllers.Admin.Pages;

import com.Frontend.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AdminBookingsTickets_PageController {

    @FXML
    private TableView<?> BT;
    @FXML
    private TableColumn<?, ?> BT_Action;
    @FXML
    private TableColumn<?, ?> BT_B_Date;
    @FXML
    private TableColumn<?, ?> BT_B_ID;
    @FXML
    private TableColumn<?, ?> BT_B_S;
    @FXML
    private TableColumn<?, ?> BT_C_ID;
    @FXML
    private Label C_Username;
    @FXML
    private TextField Search;
    @FXML
    private TableView<?> TT;
    @FXML
    private TableColumn<?, ?> TT_B_ID;
    @FXML
    private TableColumn<?, ?> TT_ST_ID;
    @FXML
    private TableColumn<?, ?> TT_S_ID;



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

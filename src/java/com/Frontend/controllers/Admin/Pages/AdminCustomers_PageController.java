package com.Frontend.controllers.Admin.Pages;

import com.Frontend.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AdminCustomers_PageController {

    @FXML
    private TableView<?> CT;

    @FXML
    private TableColumn<?, ?> CT_Action;

    @FXML
    private TableColumn<?, ?> CT_C_ID;

    @FXML
    private TableColumn<?, ?> CT_E;

    @FXML
    private TableColumn<?, ?> CT_MS;

    @FXML
    private TableColumn<?, ?> CT_N;

    @FXML
    private TableColumn<?, ?> CT_P;

    @FXML
    private TableColumn<?, ?> CT_PN;

    @FXML
    private Label C_Username;

    @FXML
    private TextField Search;

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

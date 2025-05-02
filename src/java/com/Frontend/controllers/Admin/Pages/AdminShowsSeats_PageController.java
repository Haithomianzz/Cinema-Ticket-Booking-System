package com.Frontend.controllers.Admin.Pages;

import com.Frontend.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AdminShowsSeats_PageController {

    @FXML
    private Label C_Username;
    @FXML
    private TableView<?> SST;
    @FXML
    private TableColumn<?, ?> SST_ST_ID;
    @FXML
    private TableColumn<?, ?> SST_S_ID;
    @FXML
    private TableView<?> ST;
    @FXML
    private TableColumn<?, ?> ST_Action;
    @FXML
    private TableColumn<?, ?> ST_H_N;
    @FXML
    private TableColumn<?, ?> ST_M_ID;
    @FXML
    private TableColumn<?, ?> ST_ST_ID;
    @FXML
    private TableColumn<?, ?> ST_S_D;
    @FXML
    private TableColumn<?, ?> ST_S_P;
    @FXML
    private TableColumn<?, ?> ST_S_T;
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
    public void add(ActionEvent event) throws IOException {

    }
}

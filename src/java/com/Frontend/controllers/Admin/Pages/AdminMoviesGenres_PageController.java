package com.Frontend.controllers.Admin.Pages;

import com.Frontend.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class AdminMoviesGenres_PageController {

    @FXML
    private Label C_Username;
    @FXML
    private TableView<?> MGT;
    @FXML
    private TableColumn<?, ?> MGT_Action;
    @FXML
    private TableColumn<?, ?> MGT_G;
    @FXML
    private TableColumn<?, ?> MGT_M_ID;
    @FXML
    private TableView<?> MT;
    @FXML
    private TableColumn<?, ?> MT_Action;
    @FXML
    private TableColumn<?, ?> MT_D;
    @FXML
    private TableColumn<?, ?> MT_L;
    @FXML
    private TableColumn<?, ?> MT_M_ID;
    @FXML
    private TableColumn<?, ?> MT_R;
    @FXML
    private TableColumn<?, ?> MT_RD;
    @FXML
    private TableColumn<?, ?> MT_T;
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

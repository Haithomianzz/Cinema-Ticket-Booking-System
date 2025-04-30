package com.Frontend.controllers.Customer.Pages;

import com.Backend.Entities.Customer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Label;

public class Booking_PagesController {

    @FXML
    private Label C_Username;
    @FXML
    private VBox BP_Clist;
    @FXML
    private ScrollPane BP_MList;

    private static Customer customer;

    public static void setCustomer(Customer customer) {


    }

    public void initialize(URL location, ResourceBundle resources)  {

    }


}

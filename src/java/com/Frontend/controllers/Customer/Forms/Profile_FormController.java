package com.Frontend.controllers.Customer.Forms;

import com.Backend.Client;
import com.Backend.Entities.Customer;
import com.Backend.Entities.Movie;
import com.Frontend.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.security.PublicKey;

public class Profile_FormController {

    @FXML
    private TextField PF_Name;
    @FXML
    private TextField PF_Phone;
    @FXML
    private TextField PF_Email;
    @FXML
    private TextField PF_Password;
    @FXML
    private Label PF_Membership;

    private Customer customer;

    public void initialize( ) {
        customer = Main.getCurrentUser();
        if (customer != null) {
            setData(customer);
        }
    }
    public void setData(Customer customer) throws IllegalArgumentException {
        if(customer != null) {
            PF_Name.setText(customer.getName());
            PF_Phone.setText(customer.getPhone());
            PF_Email.setText(customer.getEmail());
            PF_Password.setText(customer.getPassword());
            PF_Membership.setText(customer.getMembership().toString());
        }
    }

    public void updateData() {
        Customer newCustomer = new Customer( customer.getCustomerId(),
                PF_Name.getText(),
                PF_Email.getText(),
                PF_Password.getText(),
                PF_Phone.getText(),
                customer.getMembership()
        );
        if (Client.updateCustomer(newCustomer)) {
            customer = newCustomer;
            Main.setCurrentUser(customer);
            System.out.println("Update successful");
        } else {
            System.out.println("Update failed");
        }
    }

    public void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}

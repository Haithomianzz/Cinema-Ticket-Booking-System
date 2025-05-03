package com.Frontend.controllers.Admin.Forms;

import com.Backend.Client;
import com.Backend.Entities.Customer;
import com.Frontend.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminCustomer_FormController {

    @FXML
    private TextField ACF_Email;
    @FXML
    private TextField ACF_Name;
    @FXML
    private TextField ACF_Password;
    @FXML
    private TextField ACF_Phone;
    @FXML
    private ChoiceBox<String> ACF_Membership;

    private Customer customer;

    public void initialize() {
        ObservableList<String> membershipTypes = FXCollections.observableArrayList(
                Customer.MembershipStatus.REGULAR.toString(),
                Customer.MembershipStatus.PREMIUM.toString(),
                Customer.MembershipStatus.VIP.toString()
        );
        ACF_Membership.setItems(membershipTypes);
    }

    public void setData(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            ACF_Email.setText(customer.getEmail());
            ACF_Name.setText(customer.getName());
            ACF_Password.setText(customer.getPassword());
            ACF_Phone.setText(customer.getPhone());
            ACF_Membership.setValue(customer.getMembership().toString());
        } else {
            ACF_Email.clear();
            ACF_Name.clear();
            ACF_Password.clear();
            ACF_Phone.clear();
            ACF_Membership.setValue(null);
        }
    }

    public void save(ActionEvent event) {
        String name = ACF_Name.getText();
        String email = ACF_Email.getText();
        String phoneStr = ACF_Phone.getText();
        String password = ACF_Password.getText();
        String membership = ACF_Membership.getValue();

        if (name.isEmpty() || email.isEmpty() || phoneStr.isEmpty() || password.isEmpty() || membership == null) {
            AlertBox.alert("Error", "Please fill in all fields!", "Close");
            return;
        }
        if (!phoneStr.matches("\\d+")) {
            AlertBox.alert("Error", "Phone number must be numeric!", "Close");
            return;
        }

        if (customer != null) {
            Customer newCustomer = new Customer(customer.getCustomerId(), name, email, phoneStr, password, Customer.MembershipStatus.valueOf(membership));
            if (!Client.updateCustomer(newCustomer)) {
                AlertBox.alert("Error", "Customer already exists!", "Close");
                return;
            }
            customer.editProfile(name, email, phoneStr, password, Customer.MembershipStatus.valueOf(membership));
            System.out.println("Updating customer...");

        } else {
            System.out.println("Adding new customer...");
            customer = new Customer(name, email, phoneStr, password, Customer.MembershipStatus.valueOf(membership));
            if (!Client.addCustomer(customer)) {
                AlertBox.alert("Error", "Customer already exists!", "Close");
                return;
            }
        }
        cancel(event);
    }

    public void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

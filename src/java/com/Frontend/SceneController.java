package com.Frontend;

import com.Backend.Entities.*;

import com.Frontend.controllers.Admin.Forms.*;
import com.Frontend.controllers.Customer.Forms.Show_FormController;
import com.Frontend.controllers.Customer.Pages.MovieDetail_PageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public static void SwitchToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Login_Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void SwitchToSignup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Signup_Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void SwitchToForgotPassword(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/ForgotPassword_page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void SwitchToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Customer/Pages/Home_Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void SwitchToMovies(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Customer/Pages/Movies_Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void SwitchToMovieDetail(MouseEvent event, Movie movie) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Customer/Pages/MovieDetail_Page.fxml"));
        Parent root = loader.load();
        // Pass the movie object to the MovieDetail_PageController
        MovieDetail_PageController controller = loader.getController();
        controller.setMovie(movie);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void SwitchToBookingPages(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Customer/Pages/Booking_Pages.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void SwitchToTicketPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Customer/Pages/Ticket_Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void SwitchToProfileForm(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Customer/Forms/Profile_Form.fxml"));
        Parent root = loader.load();

        Stage newStage = new Stage();
        newStage.setTitle("Profile Form");

        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setScene(new Scene(root));
        newStage.showAndWait();
    }
    public static void SwitchToShowForm(MouseEvent event, Showtime show) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Customer/Forms/Show_Form.fxml"));
        AnchorPane root = loader.load();

        Show_FormController controller = loader.getController();
        controller.setData(show);

        Stage newStage = new Stage();
        newStage.setTitle("Show Form");

        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setScene(new Scene(root));
        newStage.showAndWait();
    }

    public static void SwitchToAdminBookingsTickets(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Admin/Pages/AdminBookingsTickets_Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void SwitchToAdminCustomers(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Admin/Pages/AdminCustomers_Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void SwitchToAdminHallsSeats(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Admin/Pages/AdminHallsSeats_Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void SwitchToAdminMovesGenres(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Admin/Pages/AdminMoviesGenres_Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static void SwitchToAdminShowsSeats(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Admin/Pages/AdminShowsSeats_Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void SwitchToAdminBookingForm(ActionEvent event,Booking book) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Admin/Forms/AdminBooking_Form.fxml"));
        Parent root = loader.load();

        AdminBooking_FormController controller = loader.getController();

        Stage newStage = new Stage();
        newStage.setTitle("Booking Form");

        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setScene(new Scene(root));
        newStage.showAndWait();
    }
    public static void SwitchToAdminCustomerForm(ActionEvent event, Customer customer) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Admin/Forms/AdminCustomer_Form.fxml"));
        Parent root = loader.load();

        AdminCustomer_FormController controller = loader.getController();
        controller.setData(customer);

        Stage newStage = new Stage();
        newStage.setTitle("Customer Form");

        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setScene(new Scene(root));
        newStage.showAndWait();
    }
    public static void SwitchToAdminHallForm(ActionEvent event, Hall hall) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Admin/Forms/AdminHall_Form.fxml"));
        Parent root = loader.load();

        AdminHall_FormController controller = loader.getController();
        controller.setData(hall);

        Stage newStage = new Stage();
        newStage.setTitle("Hall Form");

        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setScene(new Scene(root));
        newStage.showAndWait();
    }
    public static void SwitchToAdminMovieForm(ActionEvent event, Movie movie) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Admin/Forms/AdminMovie_Form.fxml"));
        Parent root = loader.load();

        AdminMovie_FormController controller = loader.getController();
        controller.setData(movie);

        Stage newStage = new Stage();
        newStage.setTitle("Movie Form");

        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setScene(new Scene(root));
        newStage.showAndWait();
    }
    public static void SwitchToAdminShowForm(ActionEvent event,Showtime show) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("fxml/Admin/Forms/AdminShow_Form.fxml"));
        AnchorPane root = loader.load();

        AdminShow_FormController controller = loader.getController();
        controller.setData(show);

        Stage newStage = new Stage();
        newStage.setTitle("Show Form");

        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setScene(new Scene(root));
        newStage.showAndWait();
    }

}
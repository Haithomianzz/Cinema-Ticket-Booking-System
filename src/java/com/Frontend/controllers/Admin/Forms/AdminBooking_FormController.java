package com.Frontend.controllers.Admin.Forms;

import com.Backend.Client;
import com.Backend.Entities.*;
import com.Frontend.AlertBox;
import com.Frontend.Main;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import java.util.ArrayList;

public class AdminBooking_FormController {

    @FXML
    private MaterialIconView A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6, C1, C2, C3, C4, C5, C6;
    @FXML
    private Label ABF_Date;
    @FXML
    private Label ABF_Discount;
    @FXML
    private Label ABF_SeatPrice; 
    @FXML
    private Label ABF_Seats;
    @FXML
    private Label ABF_TPrice;
    @FXML
    private Label ABF_Time;
    @FXML
    private Label availableSeatsLabel;
    @FXML
    private Label bookedSeatsLabel;
    @FXML
    private GridPane gridSeats;
    @FXML
    private ChoiceBox<String> ABF_Show;
    @FXML
    private ChoiceBox<String> ABF_Movie;
    @FXML
    private ChoiceBox<String> ABF_Customer;
    @FXML
    private Label totalSeatsLabel;
    @FXML
    private AnchorPane ABF_SS;

    private ArrayList<String> selectedSeats = new ArrayList<>();

    private Customer customer;
    private Movie movie;
    private Showtime showtime;

    public void initialize() {
        ABF_SS.setVisible(false);
        ABF_SS.setDisable(true);
        totalSeatsLabel.setVisible(false);
        bookedSeatsLabel.setVisible(false);
        availableSeatsLabel.setVisible(false);

        for (Customer customer : Client.getCustomerMap().values()) {
            ABF_Customer.getItems().add(customer.getName());
        }
        for (Movie movie : Client.getMovieMap().values()) {
            if (movie.getShowtimes().isEmpty()) continue;
            ABF_Movie.getItems().add(movie.getTitle());
        }
        ABF_Customer.getSelectionModel().selectedIndexProperty().addListener((_,_, newValue) -> {
            for (Customer customer : Client.getCustomerMap().values()) {
                if (customer.getName().equals(ABF_Customer.getItems().get(newValue.intValue()))) {
                    this.customer = customer;
                    break;
                }
            }
        });
        ABF_Movie.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            ABF_Show.getItems().clear();
            ABF_SS.setVisible(false);
            ABF_SS.setDisable(true);
            totalSeatsLabel.setVisible(false);
            bookedSeatsLabel.setVisible(false);
            availableSeatsLabel.setVisible(false);
            for (Movie movie : Client.getMovieMap().values()) {
                if (movie.getTitle().equals(newValue)) {
                    this.movie = movie;
                    break;
                }
            }
            for (Showtime showtime : movie.getShowtimes()) {
                ABF_Show.getItems().add(showtime.getShowTime());
            }
        });
        ABF_Show.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            for (Showtime showtime : movie.getShowtimes()) {
                if (showtime.getShowTime().equals(newValue)) {
                    this.showtime = showtime;
                    break;
                }
            }
            if (showtime == null) return;
            ABF_SS.setVisible(true);
            ABF_SS.setDisable(false);
            totalSeatsLabel.setVisible(true);
            bookedSeatsLabel.setVisible(true);
            availableSeatsLabel.setVisible(true);
            setData(showtime);
        });

    }


    public void setData(Showtime showtime) {
        this.showtime = showtime;
        Hall hall = showtime.getHall();

        String time = showtime.getShowTime();
        int hour = Integer.parseInt(time.substring(0, 2));
        String period = hour >= 12 ? "PM" : "AM";
        hour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
        String formattedTime = String.format("%d:%s %s", hour, time.substring(3, 5), period);


        for (Ticket ticket : showtime.getTickets()) {
            int row = ticket.getSeat().getRowNumber() - 1; // Adjust for 1-indexed data
            int col = ticket.getSeat().getSeatNumber() - 1; // Adjust for 1-indexed data
            ((MaterialIconView) gridSeats.getChildren().get(row * 6 + col)).setFill(javafx.scene.paint.Color.web("#c9b3b3"));
        }
        for (Node node : gridSeats.getChildren()) {
            int row = GridPane.getRowIndex(node) + 1;
            int col = GridPane.getColumnIndex(node) + 1;
            boolean seatExists = showtime.getHall().getSeats().stream()
                    .anyMatch(seat -> seat.getRowNumber() == row && seat.getSeatNumber() == col);
            if (!seatExists) {
                node.setVisible(false);
            }
        }

        ABF_Date.setText(showtime.getShowDate().toString());
        ABF_Time.setText(formattedTime);
        ABF_Discount.setText((Customer.MembershipStatus.valueOf(Main.getCurrentUser().getMembership().toString()).ordinal() * 10) + "%");
        ABF_SeatPrice.setText(String.valueOf(showtime.getPricePerSeat()));
        availableSeatsLabel.setText("Available Seats: " + showtime.getAvailableSeats().size());
        bookedSeatsLabel.setText("Booked Seats: " + (showtime.getHall().getNumberOfSeats() - showtime.getAvailableSeats().size()));
        totalSeatsLabel.setText("Total Seats: " + showtime.getHall().getNumberOfSeats());
    }


    public void selectSeat(MouseEvent e) {
        Node source = (Node) e.getSource();
        String seatId = source.getId();
        if (source instanceof MaterialIconView && ((MaterialIconView) source).getFill().equals(javafx.scene.paint.Color.web("#c9b3b3"))) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The seat " + seatId + " is already booked!", ButtonType.OK);
            alert.showAndWait();
        }
        else if (source instanceof MaterialIconView && ((MaterialIconView) source).getFill().equals(javafx.scene.paint.Color.BLACK)) {
            ((MaterialIconView) source).setFill(javafx.scene.paint.Color.RED);
            selectedSeats.add(seatId);
            updateSeatLabels();
        } else if (source instanceof MaterialIconView && ((MaterialIconView) source).getFill().equals(javafx.scene.paint.Color.RED)) {
            ((MaterialIconView) source).setFill(javafx.scene.paint.Color.BLACK);
            selectedSeats.remove(seatId);
            updateSeatLabels();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The seat " + seatId + " is not available for selection!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void updateSeatLabels() {
        String selectedSeatsText = String.join(", ", selectedSeats);
        ABF_TPrice.setText("" + ((selectedSeats.size() * showtime.getPricePerSeat() * (100 - Customer.MembershipStatus.valueOf(Main.getCurrentUser().getMembership().toString()).ordinal() * 10)) / 100));
        ABF_Seats.setText("" + selectedSeatsText);
        availableSeatsLabel.setText("Available Seats: " + (showtime.getAvailableSeats().size() - selectedSeats.size()));
        bookedSeatsLabel.setText("Booked Seats: " + (showtime.getHall().getNumberOfSeats() - showtime.getAvailableSeats().size()));
        totalSeatsLabel.setText("Total Seats: " + showtime.getHall().getNumberOfSeats());
    }

    public void save(ActionEvent event) {
        if (selectedSeats.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select at least one seat!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        int totalPrice = Integer.parseInt(ABF_TPrice.getText());
        Booking booking = new Booking(customer, totalPrice, showtime.getShowDate(), Booking.BookingStatus.CONFIRMED.toString());
        if (!Client.addBooking(booking)) {
            AlertBox.alert("Error", "Failed to create booking.", "Close");
            return;
        }

        for (String seatId : selectedSeats) {
            int row = seatId.charAt(0) - 'A' + 1; // Adjust for 1-indexed data
            int col = Integer.parseInt(seatId.substring(1)); // Already 1-indexed
            Seat seat = showtime.getAvailableSeats().stream()
                    .filter(s -> s.getRowNumber() == row && s.getSeatNumber() == col)
                    .findFirst()
                    .orElse(null);

            if (seat != null) {
                Ticket ticket = new Ticket(booking, showtime, seat);
                if (!Client.addTicket(ticket)){
                    AlertBox.alert("Error", "Failed to create ticket.", "Close");
                    booking.removeTicket(ticket);
                    showtime.removeTicket(ticket);
                    seat.removeTicket(ticket);
                    Client.removeBooking(booking);
                    return;
                }

                showtime.getAvailableSeats().remove(seat);
            }
        }
        close(event);
    }

    public void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
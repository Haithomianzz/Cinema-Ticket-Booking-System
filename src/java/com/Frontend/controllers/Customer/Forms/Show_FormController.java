package com.Frontend.controllers.Customer.Forms;

import com.Backend.Client;
import com.Backend.Entities.*;
import com.Frontend.AlertBox;
import com.Frontend.Main;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import java.util.ArrayList;

public class Show_FormController {

    @FXML
    private MaterialIconView A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6, C1, C2, C3, C4, C5, C6;
    @FXML
    private Label BF_Date;
    @FXML
    private Label BF_Discount;
    @FXML
    private Label BF_PSeat;
    @FXML
    private Label BF_Seats;
    @FXML
    private Label BF_TPrice;
    @FXML
    private Label BF_Time;
    @FXML
    private Label BF_Title;
    @FXML
    private Label availableSeatsLabel;
    @FXML
    private Label bookedSeatsLabel;
    @FXML
    private GridPane gridSeats;
    @FXML
    private Label totalSeatsLabel;
    private ArrayList<String> selectedSeats = new ArrayList<>();

    private Showtime showtime;

    public void initialize() {
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

        BF_Title.setText(showtime.getMovie().getTitle());
        BF_Date.setText(showtime.getShowDate().toString());
        BF_Time.setText(formattedTime);
        BF_Discount.setText((Customer.MembershipStatus.valueOf(Main.getCurrentUser().getMembership().toString()).ordinal() * 10) + "%");
        BF_PSeat.setText(String.valueOf(showtime.getPricePerSeat()));
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
        BF_TPrice.setText("" + ((selectedSeats.size() * showtime.getPricePerSeat() * (100 - Customer.MembershipStatus.valueOf(Main.getCurrentUser().getMembership().toString()).ordinal() * 10)) / 100));
        BF_Seats.setText("" + selectedSeatsText);
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
        int totalPrice = Integer.parseInt(BF_TPrice.getText());
        Booking booking = new Booking(Main.getCurrentUser(), totalPrice, showtime.getShowDate(), Booking.BookingStatus.PENDING.toString());
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
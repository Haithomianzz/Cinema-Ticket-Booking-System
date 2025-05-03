package com.Backend.Entities;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;

public class Ticket {
    private byte[] qrCode;
    private final Booking booking;
    private final Showtime showtime;
    private final Seat seat;

    public Ticket(Booking booking, Showtime showtime,Seat seat) {
        this.booking = booking;
        this.showtime = showtime;
        this.seat = seat;
        generateQRCode();
        booking.addTicket(this);
        showtime.addTicket(this);
        seat.addTicket(this);
    }
    public void generateQRCode() {
        try {
            String content = this.toString(); // Use the class's toString method
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 500, 500);

            // Convert the BitMatrix to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
            qrCode = byteArrayOutputStream.toByteArray();

            System.out.println("QR Code Generated and Stored Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cancelTicket() {
        booking.removeTicket(this);
        showtime.removeTicket(this);
        showtime.getAvailableSeats().add(seat);
        seat.removeTicket(this);
    }
    @Override
    public String toString() {
        return  "----------------------------"+
                "\nTicket Information\n" + booking +
                "\nShowtime Information\n " + showtime +
                "\nSeat Information\n" + seat +
                "\n----------------------------";
    }

    public byte[] getQrCode() {
        return qrCode;
    }

    public Booking getBooking() {
        return booking;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public Seat getSeat() {
        return seat;
    }
}

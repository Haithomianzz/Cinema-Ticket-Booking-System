package com.Entities;

public class Payment {
    public enum PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD,
        PAYPAL,
        CASH,
        BANK_TRANSFER
    }
    public enum PaymentStatus {
        COMPLETED,
        FAILED,
        REFUNDED
    }
    private PaymentMethod paymentMethod;
    private String transactionId;
    private String paymentDate;
    private PaymentStatus status;

    private Booking booking;

    public Payment(Booking booking,PaymentMethod paymentMethod, String transactionId, String paymentDate, PaymentStatus status) {
        this.booking = booking;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.paymentDate = paymentDate;
        this.status = status;
    }
    public Booking getBooking() { return booking; }


    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public String getTransactionId() { return transactionId; }
    public String getPaymentDate() { return paymentDate; }
    public PaymentStatus getStatus() { return status; }


    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public void refund() {
        status = PaymentStatus.REFUNDED;
    }
    public void fail() {
        status = PaymentStatus.FAILED;
    }
    public void complete() {
        status = PaymentStatus.COMPLETED;
    }

    @Override
    public String toString() {
        return "\nPayment Method: " + paymentMethod +
                "\nTransaction ID: " + transactionId +
                "\nPayment Date: " + paymentDate +
                "\nStatus: " + status;
    }
}

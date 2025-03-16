package com.Entities;


import java.util.Objects;

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
    private double amount;
    private String paymentDate;
    private PaymentStatus status;

    private Booking booking;


    public Payment(PaymentMethod paymentMethod, String transactionId, double amount, String paymentDate, PaymentStatus status) {
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public String getTransactionId() { return transactionId; }
    public double getAmount() { return amount; }
    public String getPaymentDate() { return paymentDate; }
    public PaymentStatus getStatus() { return status; }

    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "\nPayment Method: " + paymentMethod +
                "\nTransaction ID: " + transactionId +
                "\nAmount: $" + amount +
                "\nPayment Date: " + paymentDate +
                "\nStatus: " + status;
    }
}

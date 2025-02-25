package tn.esprit.atlas.entities;

import java.time.LocalDateTime;

public class FlightReservation {
    private int reservationId; // Primary key
    private int userId; // Foreign key to Utilisateur
    private int volId; // Foreign key to Vol
    private String passengerName;
    private String passengerEmail;
    private String passengerPhone;
    private LocalDateTime reservationDate;
    private int numberOfPassengers;
    private double totalPrice;
    private String paymentStatus; // ENUM: Pending, Paid, Cancelled
    private String reservationStatus; // ENUM: Confirmed, Pending, Cancelled
    private String specialRequests;

    // Constructors
    public FlightReservation() {
    }

    public FlightReservation(int userId, int volId, String passengerName, String passengerEmail, String passengerPhone, LocalDateTime reservationDate, int numberOfPassengers, double totalPrice, String paymentStatus, String reservationStatus, String specialRequests) {
        this.userId = userId;
        this.volId = volId;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.passengerPhone = passengerPhone;
        this.reservationDate = reservationDate;
        this.numberOfPassengers = numberOfPassengers;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
        this.reservationStatus = reservationStatus;
        this.specialRequests = specialRequests;
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVolId() {
        return volId;
    }

    public void setVolId(int volId) {
        this.volId = volId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    @Override
    public String toString() {
        return "FlightReservation{" +
                "reservationId=" + reservationId +
                ", userId=" + userId +
                ", volId=" + volId +
                ", passengerName='" + passengerName + '\'' +
                ", passengerEmail='" + passengerEmail + '\'' +
                ", passengerPhone='" + passengerPhone + '\'' +
                ", reservationDate=" + reservationDate +
                ", numberOfPassengers=" + numberOfPassengers +
                ", totalPrice=" + totalPrice +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", specialRequests='" + specialRequests + '\'' +
                '}';
    }
}
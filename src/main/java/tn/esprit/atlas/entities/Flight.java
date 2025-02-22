package tn.esprit.atlas.entities;

import java.time.LocalDate;

public class Flight {
    private int vol_id;
    private String departure;
    private String destination;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private int availableSeats;
    private double price;
    private int airline_id; // Foreign key to AirLine

    // Default constructor
    public Flight() {
    }

    // Constructor with all fields
    public Flight(int vol_id, String departure, String destination, LocalDate departureDate, LocalDate returnDate, int availableSeats, double price, int airline_id) {
        this.vol_id = vol_id;
        this.departure = departure;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.availableSeats = availableSeats;
        this.price = price;
        this.airline_id = airline_id;
    }

    // Constructor without ID (for insertion)
    public Flight(String departure, String destination, LocalDate departureDate, LocalDate returnDate, int availableSeats, double price, int airline_id) {
        this.departure = departure;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.availableSeats = availableSeats;
        this.price = price;
        this.airline_id = airline_id;
    }

    // Getters and Setters
    public int getVol_id() {
        return vol_id;
    }

    public void setVol_id(int vol_id) {
        this.vol_id = vol_id;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAirline_id() {
        return airline_id;
    }

    public void setAirline_id(int airline_id) {
        this.airline_id = airline_id;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "vol_id=" + vol_id +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", departureDate=" + departureDate +
                ", returnDate=" + returnDate +
                ", availableSeats=" + availableSeats +
                ", price=" + price +
                ", airline_id=" + airline_id +
                '}';
    }
}
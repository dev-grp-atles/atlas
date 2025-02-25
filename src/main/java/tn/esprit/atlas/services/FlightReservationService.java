package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.FlightReservation;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightReservationService {

    private Connection cnx;

    public FlightReservationService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    // Add a new flight reservation
    public void addFlightReservation(FlightReservation reservation) {
        String req = "INSERT INTO FlightReservation (user_id, vol_id, passenger_name, passenger_email, passenger_phone, reservation_date, number_of_passengers, total_price, payment_status, reservation_status, special_requests) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, reservation.getUserId());
            stm.setInt(2, reservation.getVolId());
            stm.setString(3, reservation.getPassengerName());
            stm.setString(4, reservation.getPassengerEmail());
            stm.setString(5, reservation.getPassengerPhone());
            stm.setTimestamp(6, Timestamp.valueOf(reservation.getReservationDate()));
            stm.setInt(7, reservation.getNumberOfPassengers());
            stm.setDouble(8, reservation.getTotalPrice());
            stm.setString(9, reservation.getPaymentStatus());
            stm.setString(10, reservation.getReservationStatus());
            stm.setString(11, reservation.getSpecialRequests());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Get all flight reservations
    public List<FlightReservation> getAllFlightReservations() {
        List<FlightReservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM FlightReservation";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                FlightReservation reservation = new FlightReservation();
                reservation.setReservationId(rs.getInt("reservation_id"));
                reservation.setUserId(rs.getInt("user_id"));
                reservation.setVolId(rs.getInt("vol_id"));
                reservation.setPassengerName(rs.getString("passenger_name"));
                reservation.setPassengerEmail(rs.getString("passenger_email"));
                reservation.setPassengerPhone(rs.getString("passenger_phone"));
                reservation.setReservationDate(rs.getTimestamp("reservation_date").toLocalDateTime());
                reservation.setNumberOfPassengers(rs.getInt("number_of_passengers"));
                reservation.setTotalPrice(rs.getDouble("total_price"));
                reservation.setPaymentStatus(rs.getString("payment_status"));
                reservation.setReservationStatus(rs.getString("reservation_status"));
                reservation.setSpecialRequests(rs.getString("special_requests"));

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }

    // Update a flight reservation
    public void updateFlightReservation(FlightReservation reservation) {
        String req = "UPDATE FlightReservation SET user_id = ?, vol_id = ?, passenger_name = ?, passenger_email = ?, passenger_phone = ?, reservation_date = ?, number_of_passengers = ?, total_price = ?, payment_status = ?, reservation_status = ?, special_requests = ? WHERE reservation_id = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, reservation.getUserId());
            stm.setInt(2, reservation.getVolId());
            stm.setString(3, reservation.getPassengerName());
            stm.setString(4, reservation.getPassengerEmail());
            stm.setString(5, reservation.getPassengerPhone());
            stm.setTimestamp(6, Timestamp.valueOf(reservation.getReservationDate()));
            stm.setInt(7, reservation.getNumberOfPassengers());
            stm.setDouble(8, reservation.getTotalPrice());
            stm.setString(9, reservation.getPaymentStatus());
            stm.setString(10, reservation.getReservationStatus());
            stm.setString(11, reservation.getSpecialRequests());
            stm.setInt(12, reservation.getReservationId());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Delete a flight reservation
    public void deleteFlightReservation(int reservationId) {
        String req = "DELETE FROM FlightReservation WHERE reservation_id = ?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, reservationId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
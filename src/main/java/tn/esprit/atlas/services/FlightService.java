package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.Flight;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlightService {

    private Connection cnx;

    public FlightService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public void add(Flight flight) {
        String req = "INSERT INTO Vol (departure, destination, departureDate, returnDate, availableSeats, price, airline_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, flight.getDeparture());
            stm.setString(2, flight.getDestination());
            stm.setDate(3, Date.valueOf(flight.getDepartureDate()));
            stm.setDate(4, Date.valueOf(flight.getReturnDate()));
            stm.setInt(5, flight.getAvailableSeats());
            stm.setDouble(6, flight.getPrice());
            stm.setInt(7, flight.getAirline_id());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void update(Flight flight) {
        String req = "UPDATE Vol SET departure = ?, destination = ?, departureDate = ?, returnDate = ?, availableSeats = ?, price = ?, airline_id = ? WHERE vol_id = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, flight.getDeparture());
            stm.setString(2, flight.getDestination());
            stm.setDate(3, Date.valueOf(flight.getDepartureDate()));
            stm.setDate(4, Date.valueOf(flight.getReturnDate()));
            stm.setInt(5, flight.getAvailableSeats());
            stm.setDouble(6, flight.getPrice());
            stm.setInt(7, flight.getAirline_id());
            stm.setInt(8, flight.getVol_id());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void delete(Flight flight) {
        String req = "DELETE FROM Vol WHERE vol_id = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, flight.getVol_id());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Flight> getall() {
        List<Flight> flights = new ArrayList<>();
        String req = "SELECT * FROM Vol";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                Flight flight = new Flight();
                flight.setVol_id(rs.getInt("vol_id"));
                flight.setDeparture(rs.getString("departure"));
                flight.setDestination(rs.getString("destination"));
                flight.setDepartureDate(rs.getDate("departureDate").toLocalDate());
                flight.setReturnDate(rs.getDate("returnDate").toLocalDate());
                flight.setAvailableSeats(rs.getInt("availableSeats"));
                flight.setPrice(rs.getDouble("price"));
                flight.setAirline_id(rs.getInt("airline_id"));
                flights.add(flight);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return flights;
    }



    public Flight getone() {
        // Implement this method if needed
        return null;
    }

    // Additional method to search flights by departure and destination
    public List<Flight> searchFlights(String departure, String destination) {
        List<Flight> flights = new ArrayList<>();
        String req = "SELECT * FROM Vol WHERE departure = ? AND destination = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, departure);
            stm.setString(2, destination);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Flight flight = new Flight();
                flight.setVol_id(rs.getInt("vol_id"));
                flight.setDeparture(rs.getString("departure"));
                flight.setDestination(rs.getString("destination"));
                flight.setDepartureDate(rs.getDate("departureDate").toLocalDate());
                flight.setReturnDate(rs.getDate("returnDate").toLocalDate());
                flight.setAvailableSeats(rs.getInt("availableSeats"));
                flight.setPrice(rs.getDouble("price"));
                flight.setAirline_id(rs.getInt("airline_id"));
                flights.add(flight);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return flights;
    }
}
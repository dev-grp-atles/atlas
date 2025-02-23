package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.Hotel;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelService implements IService<Hotel> {

    private Connection connection;

    public HotelService() {
        connection = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void add(Hotel hotel) {
        String query = "INSERT INTO Hotel (name, address, rating, image_url, available_rooms, hotel_rent, facilities, check_in_time, check_out_time, contact_number, city, latitude, longitude) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getAddress());
            stmt.setFloat(3, hotel.getRating());
            stmt.setString(4, hotel.getImageUrl());
            stmt.setInt(5, hotel.getAvailableRooms());
            stmt.setDouble(6, hotel.getAvailableRooms());
            stmt.setString(7, String.join(",", hotel.getFacilities())); // Convert list to string
            stmt.setString(8, hotel.getCheckInTime());
            stmt.setString(9, hotel.getCheckOutTime());
            stmt.setString(10, hotel.getContactNumber());
            stmt.setString(11, hotel.getCity());
            stmt.setDouble(12, hotel.getLatitude());
            stmt.setDouble(13, hotel.getLongitude());
            stmt.executeUpdate();
            System.out.println("Hotel added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Hotel hotel) {
        String query = "UPDATE Hotel SET name = ?, address = ?, rating = ?, image_url = ?, available_rooms = ?, hotel_rent = ?, facilities = ?, check_in_time = ?, check_out_time = ?, contact_number = ?, city = ?, latitude = ?, longitude = ? " +
                "WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getAddress());
            stmt.setFloat(3, hotel.getRating());
            stmt.setString(4, hotel.getImageUrl());
            stmt.setInt(5, hotel.getAvailableRooms());
            stmt.setDouble(6, hotel.getAvailableRooms());
            stmt.setString(7, String.join(",", hotel.getFacilities())); // Convert list to string
            stmt.setString(8, hotel.getCheckInTime());
            stmt.setString(9, hotel.getCheckOutTime());
            stmt.setString(10, hotel.getContactNumber());
            stmt.setString(11, hotel.getCity());
            stmt.setDouble(12, hotel.getLatitude());
            stmt.setDouble(13, hotel.getLongitude());
            stmt.setInt(14, hotel.getId());
            stmt.executeUpdate();
            System.out.println("Hotel updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Hotel hotel) {
        String query = "DELETE FROM Hotel WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, hotel.getId());
            stmt.executeUpdate();
            System.out.println("Hotel deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Hotel> getAll() {
        List<Hotel> hotels = new ArrayList<>();
        String query = "SELECT * FROM Hotel";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Hotel hotel = new Hotel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getFloat("rating"),
                        rs.getString("image_url"),
                        rs.getInt("available_rooms"),
                        rs.getDouble("hotel_rent"),
                        List.of(rs.getString("facilities").split(",")), // Convert string to list
                        rs.getString("check_in_time"),
                        rs.getString("check_out_time"),
                        rs.getString("contact_number"),
                        rs.getString("city"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
                hotels.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }

    @Override
    public Hotel getOne() {
        // Example to get a specific hotel by ID (modify as needed)
        return null;
    }
}

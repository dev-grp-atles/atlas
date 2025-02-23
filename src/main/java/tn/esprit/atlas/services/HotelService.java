package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.Hotel;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
        // Update the query to select all the necessary columns, excluding id
        String query = "SELECT name, address, rating, image_url, available_rooms, hotel_rent, facilities, check_in_time, check_out_time, contact_number, city, latitude, longitude FROM Hotel";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Hotel hotel = new Hotel();

                // Set all the attributes of the hotel object
                hotel.setName(rs.getString("name"));
                hotel.setAddress(rs.getString("address"));
                hotel.setRating(rs.getFloat("rating"));
                hotel.setImageUrl(rs.getString("image_url"));
                hotel.setAvailableRooms(rs.getInt("available_rooms"));
                hotel.setPricePerNight(rs.getDouble("hotel_rent"));  // Using hotel_rent instead of price_per_night
                String facilitiesString = rs.getString("facilities");
                if (facilitiesString != null && !facilitiesString.isEmpty()) {
                    List<String> facilitiesList = Arrays.asList(facilitiesString.split(",\\s*"));
                    hotel.setFacilities(facilitiesList);
                } else {
                    hotel.setFacilities(new ArrayList<>());  // If no facilities, set an empty list
                }
                hotel.setCheckInTime(rs.getString("check_in_time"));
                hotel.setCheckOutTime(rs.getString("check_out_time"));
                hotel.setContactNumber(rs.getString("contact_number"));
                hotel.setCity(rs.getString("city"));
                hotel.setLatitude(rs.getDouble("latitude"));
                hotel.setLongitude(rs.getDouble("longitude"));

                hotels.add(hotel);  // Add the Hotel object to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotels;  // Return the list of Hotel objects
    }






    @Override
    public Hotel getOne() {
        // Example to get a specific hotel by ID (modify as needed)
        return null;
    }

    public List<Hotel> search(String query) {
        // Initialize an empty list to hold the search results
        List<Hotel> result = new ArrayList<>();

        // Check if the query is not null or empty
        if (query == null || query.trim().isEmpty()) {
            return result; // Return an empty list if the query is null or empty
        }

        // SQL query to search for hotels by name, address, or city
        String searchQuery = "SELECT * FROM Hotel WHERE name LIKE ? OR address LIKE ? OR city LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(searchQuery)) {
            // Use the query parameter to find matches (case-insensitive search)
            String searchTerm = "%" + query.trim() + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);

            // Execute the query and process the results
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Handle null values for facilities column
                    List<String> facilities = rs.getString("facilities") != null
                            ? List.of(rs.getString("facilities").split(","))
                            : new ArrayList<>();

                    // Create a Hotel object and add it to the result list
                    Hotel hotel = new Hotel(
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getFloat("rating"),
                            rs.getString("image_url"),
                            rs.getInt("available_rooms"),
                            rs.getDouble("hotel_rent"),
                            facilities, // Pass the list as a parameter
                            rs.getString("check_in_time"),
                            rs.getString("check_out_time"),
                            rs.getString("contact_number"),
                            rs.getString("city"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude")
                    );
                    result.add(hotel); // Add the found hotel to the result list
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception
        }

        return result; // Return the list of matching hotels
    }


}

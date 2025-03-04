package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.Hotel;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelService implements IService<Hotel> {

    private static final Logger LOGGER = Logger.getLogger(HotelService.class.getName());
    private final Connection connection;

    public HotelService() {
        connection = DatabaseConnection.getInstance().getCnx();
    }

    // Helper method to set common parameters for add/update
    private void setCommonParameters(PreparedStatement stmt, Hotel hotel) throws SQLException {
        stmt.setString(1, hotel.getName());
        stmt.setString(2, hotel.getAddress());
        stmt.setFloat(3, hotel.getRating());
        stmt.setString(4, hotel.getImageUrl());
        stmt.setInt(5, hotel.getAvailableRooms());
        stmt.setDouble(6, hotel.getPricePerNight());
        stmt.setString(7, String.join(",", hotel.getFacilities()));
        stmt.setString(8, hotel.getCheckInTime());
        stmt.setString(9, hotel.getCheckOutTime());
        stmt.setString(10, hotel.getContactNumber());
        stmt.setString(11, hotel.getCity());
        stmt.setDouble(12, hotel.getLatitude());
        stmt.setDouble(13, hotel.getLongitude());
    }

    @Override
    public void add(Hotel hotel) {
        String query = "INSERT INTO Hotel (name, address, rating, image_url, available_rooms, hotel_rent, facilities, check_in_time, check_out_time, contact_number, city, latitude, longitude) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            setCommonParameters(stmt, hotel);
            stmt.executeUpdate();
            LOGGER.info("Hotel added successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding hotel", e);
        }
    }

    @Override
    public void update(Hotel hotel) {
        String query = "UPDATE Hotel SET name = ?, address = ?, rating = ?, image_url = ?, available_rooms = ?, hotel_rent = ?, facilities = ?, check_in_time = ?, check_out_time = ?, contact_number = ?, city = ?, latitude = ?, longitude = ? " +
                "WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            setCommonParameters(stmt, hotel);
            stmt.setInt(14, hotel.getId());

            int rowsUpdated = stmt.executeUpdate();
            LOGGER.info(() -> rowsUpdated + " row(s) updated");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating hotel", e);
        }
    }

    @Override
    public void delete(Hotel hotel) {
        String query = "DELETE FROM Hotel WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, hotel.getId());
            stmt.executeUpdate();
            LOGGER.info("Hotel deleted successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting hotel", e);
        }
    }

    // Helper method to create Hotel objects from ResultSet
    private Hotel mapResultSetToHotel(ResultSet rs) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setId(rs.getInt("id"));
        hotel.setName(rs.getString("name"));
        hotel.setAddress(rs.getString("address"));
        hotel.setRating(rs.getFloat("rating"));
        hotel.setImageUrl(rs.getString("image_url"));
        hotel.setAvailableRooms(rs.getInt("available_rooms"));
        hotel.setPricePerNight(rs.getDouble("hotel_rent"));

        String facilitiesString = rs.getString("facilities");
        hotel.setFacilities(facilitiesString != null && !facilitiesString.isEmpty()
                ? Arrays.asList(facilitiesString.split(",\\s*"))
                : new ArrayList<>());

        hotel.setCheckInTime(rs.getString("check_in_time"));
        hotel.setCheckOutTime(rs.getString("check_out_time"));
        hotel.setContactNumber(rs.getString("contact_number"));
        hotel.setCity(rs.getString("city"));
        hotel.setLatitude(rs.getDouble("latitude"));
        hotel.setLongitude(rs.getDouble("longitude"));
        return hotel;
    }

    @Override
    public List<Hotel> getAll() {
        List<Hotel> hotels = new ArrayList<>();
        String query = "SELECT id, name, address, rating, image_url, available_rooms, hotel_rent, facilities, " +
                "check_in_time, check_out_time, contact_number, city, latitude, longitude FROM Hotel";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                hotels.add(mapResultSetToHotel(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving hotels", e);
        }
        return hotels;
    }

    public List<Hotel> search(String query) {
        List<Hotel> result = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return result;
        }

        String searchQuery = "SELECT * FROM Hotel WHERE LOWER(name) LIKE LOWER(?) OR LOWER(address) LIKE LOWER(?) OR LOWER(city) LIKE LOWER(?)";
        try (PreparedStatement stmt = connection.prepareStatement(searchQuery)) {
            String searchTerm = "%" + query.trim() + "%";
            for (int i = 1; i <= 3; i++) {
                stmt.setString(i, searchTerm);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToHotel(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching hotels", e);
        }
        return result;
    }


    public List<Hotel> advancedSearch(Map<String, Object> filters) {
        List<Hotel> result = new ArrayList<>();
        try {
            StringBuilder query = new StringBuilder("SELECT * FROM Hotel WHERE 1=1");
            List<Object> parameters = new ArrayList<>();

            // Star Rating
            if (filters.containsKey("minRating")) {
                query.append(" AND rating >= ?");
                parameters.add((Float) filters.get("minRating"));
            }

            // Price Range
            if (filters.containsKey("maxPrice")) {
                query.append(" AND hotel_rent <= ?");
                parameters.add((Double) filters.get("maxPrice"));
            }

            // Facilities
            if (filters.containsKey("facilities")) {
                @SuppressWarnings("unchecked")
                List<String> facilities = (List<String>) filters.get("facilities");
                for (String facility : facilities) {
                    query.append(" AND facilities LIKE ?");
                    parameters.add("%" + facility + "%");
                }
            }

            // Property Type (assuming you have a 'type' column)
            if (filters.containsKey("propertyType")) {
                query.append(" AND type = ?");
                parameters.add((String) filters.get("propertyType"));
            }

            // Available Rooms
            if (filters.containsKey("minRooms")) {
                query.append(" AND available_rooms >= ?");
                parameters.add((Integer) filters.get("minRooms"));
            }

            try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
                for (int i = 0; i < parameters.size(); i++) {
                    stmt.setObject(i + 1, parameters.get(i));
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        result.add(mapResultSetToHotel(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in advanced search", e);
        }
        return result;
    }


    public void toggleFavorite(Hotel hotel) {
        // Here, you can create logic to toggle the favorite status
        // For simplicity, let's assume there's a column in the database called 'is_favorite'
        String query = "UPDATE Hotel SET is_favorite = NOT is_favorite WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, hotel.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                LOGGER.info("Hotel favorite status toggled successfully!");
            } else {
                LOGGER.warning("Hotel not found, no changes made.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error toggling hotel favorite status", e);
        }
    }



    @Override
    public Hotel getOne() {
        // Implement single hotel retrieval logic
        return null;
    }
}
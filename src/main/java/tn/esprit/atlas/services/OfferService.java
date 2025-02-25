package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.Offer;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfferService {

    private Connection cnx;

    public OfferService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    // Add a new offer
    public void addOffer(Offer offer) {
        String req = "INSERT INTO Forfait (name, description, price, duration, destinations, availableSeats, packageImage) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, offer.getName());
            stm.setString(2, offer.getDescription());
            stm.setDouble(3, offer.getPrice());
            stm.setInt(4, offer.getDuration());
            stm.setString(5, offer.getDestinations());
            stm.setInt(6, offer.getAvailableSeats());
            stm.setString(7, offer.getPackageImage());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Update an existing offer
    public void updateOffer(Offer offer) {
        String req = "UPDATE Forfait SET name = ?, description = ?, price = ?, duration = ?, destinations = ?, availableSeats = ?, packageImage = ? WHERE packageld = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, offer.getName());
            stm.setString(2, offer.getDescription());
            stm.setDouble(3, offer.getPrice());
            stm.setInt(4, offer.getDuration());
            stm.setString(5, offer.getDestinations());
            stm.setInt(6, offer.getAvailableSeats());
            stm.setString(7, offer.getPackageImage());
            stm.setInt(8, offer.getPackageld());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Delete an offer by ID
    public void deleteOffer(int packageld) {
        String req = "DELETE FROM Forfait WHERE packageld = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, packageld);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Fetch all offers
    public List<Offer> getAllOffers() {
        List<Offer> offers = new ArrayList<>();
        String req = "SELECT * FROM Forfait";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                Offer offer = new Offer();
                offer.setPackageld(rs.getInt("packageld"));
                offer.setName(rs.getString("name"));
                offer.setDescription(rs.getString("description"));
                offer.setPrice(rs.getDouble("price"));
                offer.setDuration(rs.getInt("duration"));
                offer.setDestinations(rs.getString("destinations"));
                offer.setAvailableSeats(rs.getInt("availableSeats"));
                offer.setPackageImage(rs.getString("packageImage"));

                offers.add(offer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return offers;
    }
}
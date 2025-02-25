package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.Reservation;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private Connection cnx;

    public ReservationService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public void createReservation(String prenom, String nom, String email, String telephone, String typeChambre, int nombrePersonnes, Date dateArrivee, Date dateDepart, int nombreNuits, boolean petitDejeuner, boolean litSupplementaire, String vueSpecifique, String commentaire, double montantTotal) {
        String req = "INSERT INTO Reservation (prenom, nom, email, telephone, typeChambre, nombrePersonnes, dateArrivee, dateDepart, nombreNuits, petitDejeuner, litSupplementaire, vueSpecifique, commentaire, montantTotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, prenom);
            stm.setString(2, nom);
            stm.setString(3, email);
            stm.setString(4, telephone);
            stm.setString(5, typeChambre);
            stm.setInt(6, nombrePersonnes);
            stm.setDate(7, dateArrivee);
            stm.setDate(8, dateDepart);
            stm.setInt(9, nombreNuits);
            stm.setBoolean(10, petitDejeuner);
            stm.setBoolean(11, litSupplementaire);
            stm.setString(12, vueSpecifique); // Set the view from the ComboBox
            stm.setString(13, commentaire);
            stm.setDouble(14, montantTotal);

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Fetch all reservations from the database
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM Reservation";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setPrenom(rs.getString("prenom"));
                reservation.setNom(rs.getString("nom"));
                reservation.setEmail(rs.getString("email"));
                reservation.setTelephone(rs.getString("telephone"));
                reservation.setTypeChambre(rs.getString("typeChambre"));
                reservation.setNombrePersonnes(rs.getInt("nombrePersonnes"));
                reservation.setDateArrivee(rs.getDate("dateArrivee"));
                reservation.setDateDepart(rs.getDate("dateDepart"));
                reservation.setNombreNuits(rs.getInt("nombreNuits"));
                reservation.setPetitDejeuner(rs.getBoolean("petitDejeuner"));
                reservation.setLitSupplementaire(rs.getBoolean("litSupplementaire"));
                reservation.setVueSpecifique(rs.getString("vueSpecifique"));
                reservation.setCommentaire(rs.getString("commentaire"));
                reservation.setMontantTotal(rs.getDouble("montantTotal"));

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservations;
    }

    // Delete a reservation by ID
    public void deleteReservation(int id) {
        String req = "DELETE FROM Reservation WHERE id = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.AirLine;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirLineService implements Iservice<AirLine> {

    private Connection cnx;

    public AirLineService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(AirLine airLine) {
        String req = "INSERT INTO airline (nom, pays, logo) VALUES (?, ?, ?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, airLine.getNom());
            stm.setString(2, airLine.getPays());
            stm.setString(3, airLine.getLogo());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(AirLine airLine) {
        String req = "UPDATE airline SET nom = ?, pays = ?, logo = ? WHERE airline_id = ?";  // Utilisation de 'airline_id'

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, airLine.getNom());
            stm.setString(2, airLine.getPays());
            stm.setString(3, airLine.getLogo());
            stm.setInt(4, airLine.getAirline_id());  // Utilisation de 'getAirline_id'
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimer(AirLine airLine) {
        String req = "DELETE FROM airline WHERE airline_id = ?";  // Utilisation de 'airline_id'

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, airLine.getAirline_id());  // Utilisation de 'getAirline_id'
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AirLine> getall() {
        List<AirLine> airLines = new ArrayList<>();
        String req = "SELECT * FROM airline";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                AirLine airLine = new AirLine();
                airLine.setAirline_id(rs.getInt("airline_id"));  // Utilisation de 'setAirline_id'
                airLine.setNom(rs.getString("nom"));
                airLine.setPays(rs.getString("pays"));
                airLine.setLogo(rs.getString("logo"));
                airLines.add(airLine);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return airLines;
    }

    @Override
    public AirLine getone() {
        return null; // À implémenter si nécessaire
    }
}

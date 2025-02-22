package tn.esprit.atlas.entities;

public class AirLine {
    private int airline_id;
    private String nom;
    private String pays;
    private String logo;

    public AirLine() {
    }

    public AirLine(int airline_id, String nom, String pays, String logo) {
        this.airline_id = airline_id;
        this.nom = nom;
        this.pays = pays;
        this.logo = logo;
    }

    public AirLine(String nom, String pays, String logo) {
        this.nom = nom;
        this.pays = pays;
        this.logo = logo;
    }

    public int getAirline_id() {
        return airline_id;
    }

    public void setAirline_id(int airline_id) {
        this.airline_id = airline_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "CompagnieAerienne{" +
                "airline_id=" + airline_id +
                ", nom='" + nom + '\'' +
                ", pays='" + pays + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
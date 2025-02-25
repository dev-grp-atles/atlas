package tn.esprit.atlas.entities;

import java.util.Date;

public class Reservation {
    private int id;
    private String prenom;
    private String nom;
    private String email;
    private String telephone;
    private String typeChambre;
    private int nombrePersonnes;
    private Date dateArrivee;
    private Date dateDepart;
    private int nombreNuits;
    private boolean petitDejeuner;
    private boolean litSupplementaire;
    private String vueSpecifique;
    private String commentaire;
    private double montantTotal;

    // Constructors
    public Reservation() {
    }

    public Reservation(String prenom, String nom, String email, String telephone, String typeChambre, int nombrePersonnes, Date dateArrivee, Date dateDepart, int nombreNuits, boolean petitDejeuner, boolean litSupplementaire, String vueSpecifique, String commentaire, double montantTotal) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.typeChambre = typeChambre;
        this.nombrePersonnes = nombrePersonnes;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nombreNuits = nombreNuits;
        this.petitDejeuner = petitDejeuner;
        this.litSupplementaire = litSupplementaire;
        this.vueSpecifique = vueSpecifique;
        this.commentaire = commentaire;
        this.montantTotal = montantTotal;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTypeChambre() {
        return typeChambre;
    }

    public void setTypeChambre(String typeChambre) {
        this.typeChambre = typeChambre;
    }

    public int getNombrePersonnes() {
        return nombrePersonnes;
    }

    public void setNombrePersonnes(int nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    public Date getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public int getNombreNuits() {
        return nombreNuits;
    }

    public void setNombreNuits(int nombreNuits) {
        this.nombreNuits = nombreNuits;
    }

    public boolean isPetitDejeuner() {
        return petitDejeuner;
    }

    public void setPetitDejeuner(boolean petitDejeuner) {
        this.petitDejeuner = petitDejeuner;
    }

    public boolean isLitSupplementaire() {
        return litSupplementaire;
    }

    public void setLitSupplementaire(boolean litSupplementaire) {
        this.litSupplementaire = litSupplementaire;
    }

    public String getVueSpecifique() {
        return vueSpecifique;
    }

    public void setVueSpecifique(String vueSpecifique) {
        this.vueSpecifique = vueSpecifique;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", typeChambre='" + typeChambre + '\'' +
                ", nombrePersonnes=" + nombrePersonnes +
                ", dateArrivee=" + dateArrivee +
                ", dateDepart=" + dateDepart +
                ", nombreNuits=" + nombreNuits +
                ", petitDejeuner=" + petitDejeuner +
                ", litSupplementaire=" + litSupplementaire +
                ", vueSpecifique='" + vueSpecifique + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", montantTotal=" + montantTotal +
                '}';
    }
}
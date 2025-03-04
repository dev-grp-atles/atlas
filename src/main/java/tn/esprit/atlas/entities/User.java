package tn.esprit.atlas.entities;

public class User {
    private int id;
    private String name;
    private String surname;
    private int age;
    private String email;
    private String password;
    private String adresse;
    private String role;  // Voyageur, Admin, SupportClient
    private String profileImage;
    private String numTel;
    private String voyageurPreferences;
    private String destinationsPreferrees;
    private double budget;

    // Constructors
    public User() {}

    public User(int id, String name, String surname, int age, String email, String password, String adresse,
                       String role, String profileImage, String numTel, String voyageurPreferences,
                       String destinationsPreferrees, double budget) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.password = password;
        this.adresse = adresse;
        this.role = role;
        this.profileImage = profileImage;
        this.numTel = numTel;
        this.voyageurPreferences = voyageurPreferences;
        this.destinationsPreferrees = destinationsPreferrees;
        this.budget = budget;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getNumTel() { return numTel; }
    public void setNumTel(String numTel) { this.numTel = numTel; }

    public String getVoyageurPreferences() { return voyageurPreferences; }
    public void setVoyageurPreferences(String voyageurPreferences) { this.voyageurPreferences = voyageurPreferences; }

    public String getDestinationsPreferrees() { return destinationsPreferrees; }
    public void setDestinationsPreferrees(String destinationsPreferrees) { this.destinationsPreferrees = destinationsPreferrees; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    // To String (For Debugging)
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", role='" + role + '\'' +
                ", budget=" + budget +
                '}';
    }
}

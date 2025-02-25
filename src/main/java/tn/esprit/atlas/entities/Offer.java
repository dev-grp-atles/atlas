package tn.esprit.atlas.entities;

public class Offer {
    private int packageld;
    private String name;
    private String description;
    private double price;
    private int duration;
    private String destinations;
    private int availableSeats;
    private String packageImage;

    // Constructors
    public Offer() {
    }

    public Offer(String name, String description, double price, int duration, String destinations, int availableSeats, String packageImage) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.destinations = destinations;
        this.availableSeats = availableSeats;
        this.packageImage = packageImage;
    }

    // Getters and Setters
    public int getPackageld() {
        return packageld;
    }

    public void setPackageld(int packageld) {
        this.packageld = packageld;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDestinations() {
        return destinations;
    }

    public void setDestinations(String destinations) {
        this.destinations = destinations;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(String packageImage) {
        this.packageImage = packageImage;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "packageld=" + packageld +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", destinations='" + destinations + '\'' +
                ", availableSeats=" + availableSeats +
                ", packageImage='" + packageImage + '\'' +
                '}';
    }
}
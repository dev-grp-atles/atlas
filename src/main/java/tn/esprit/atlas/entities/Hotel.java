package tn.esprit.atlas.entities;

import java.util.List;

public class Hotel {
    private int id;
    private String name;
    private String address;
    private float rating;
    private String imageUrl; // Path or URL of the uploaded image
    private int availableRooms; // Number of available rooms
    private double pricePerNight; // Price per night
    private List<String> facilities; // List of facilities (e.g., Pool, Gym, WiFi)
    private String checkInTime; // Check-in time (e.g., "14:00")
    private String checkOutTime; // Check-out time (e.g., "12:00")
    private String contactNumber; // Hotel's contact number
    private String city; // City where the hotel is located
    private double latitude; // Geographical latitude
    private double longitude; // Geographical longitude

    // Constructor
    public Hotel() {}

    public Hotel(int id, String name, String address, float rating, String imageUrl, int availableRooms, double pricePerNight, List<String> facilities, String checkInTime, String checkOutTime, String contactNumber, String city, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.availableRooms = availableRooms;
        this.pricePerNight = pricePerNight;
        this.facilities = facilities;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.contactNumber = contactNumber;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", rating=" + rating +
                ", imageUrl='" + imageUrl + '\'' +
                ", availableRooms=" + availableRooms +
                ", pricePerNight=" + pricePerNight +
                ", facilities=" + facilities +
                ", checkInTime='" + checkInTime + '\'' +
                ", checkOutTime='" + checkOutTime + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", city='" + city + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
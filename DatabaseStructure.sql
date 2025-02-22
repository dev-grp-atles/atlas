-- Create the database
CREATE DATABASE IF NOT EXISTS atlas;
USE atlas;

-- Create the Utilisateur table with roles for Voyageur, SupportClient, and Admin
CREATE TABLE Utilisateur (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    surname VARCHAR(50),
    age INT,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    adresse VARCHAR(255),
    role ENUM('Voyageur', 'SupportClient', 'Admin') NOT NULL,
    profileImage VARCHAR(255),
    
    -- Voyageur-specific fields
    num_telph VARCHAR(20),
    voyageurPreferences VARCHAR(255),
    destinations_preferrees TEXT,
    budget DOUBLE,
    reservationHistorique TEXT,
    completedTrips TEXT,
    
    -- SupportClient-specific fields
    assignedTickets TEXT,
    
    -- Admin-specific fields
    adminPrivileges TEXT
);
-- Create the AirLine table if it does not exist
CREATE TABLE IF NOT EXISTS AirLine (
    airline_id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255),
    pays VARCHAR(255),
    logo VARCHAR(255)
    );

-- Créer la table Vol (Vol)
CREATE TABLE IF NOT EXISTS Vol (
    vol_id INT PRIMARY KEY AUTO_INCREMENT,
    departure VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    departureDate DATE NOT NULL,
    returnDate DATE NOT NULL,
    availableSeats INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    airline_id INT, -- Clé étrangère pointant vers Airline
    FOREIGN KEY (airline_id) REFERENCES Airline(airline_id)
    );

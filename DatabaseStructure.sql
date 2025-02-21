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
CREATE TABLE Reservation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    prenom VARCHAR(50) NOT NULL,
    nom VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    typeChambre VARCHAR(20) NOT NULL,
    nombrePersonnes INT NOT NULL,
    dateArrivee DATE NOT NULL,
    dateDepart DATE NOT NULL,
    nombreNuits INT NOT NULL,
    petitDejeuner BOOLEAN NOT NULL,
    litSupplementaire BOOLEAN NOT NULL,
    vueSpecifique VARCHAR(20),
    commentaire TEXT,
    montantTotal DECIMAL(10, 2) NOT NULL
);
CREATE TABLE Forfait (
    packageld INT AUTO_INCREMENT PRIMARY KEY, 
    name VARCHAR(255) NOT NULL,               
    description TEXT,                        
    price DOUBLE NOT NULL,                  
    duration INT NOT NULL,                    
    destinations TEXT,                         
    availableSeats INT NOT NULL,              
    packageImage VARCHAR(255)                
);

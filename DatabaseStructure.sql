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

-- Create the categorie table
CREATE TABLE categorie (
                           categorie_id INT AUTO_INCREMENT PRIMARY KEY,  -- Unique ID for the category
                           name VARCHAR(255) NOT NULL                   -- Name of the category
);

-- Create the forum table with a foreign key relationship to categorie
CREATE TABLE forum (
                       post_id INT AUTO_INCREMENT PRIMARY KEY,    -- Unique ID for the post
                       title VARCHAR(255) NOT NULL,                -- Title of the post
                       content TEXT NOT NULL,                      -- Content of the post
                       createdAt DATE NOT NULL,                    -- Creation date of the post
                       updatedAt DATE NOT NULL,                    -- Last updated date of the post
                       viewcount INT DEFAULT 0,                    -- View count of the post
                       categorie_id INT,                           -- Foreign key referencing categorie
                       FOREIGN KEY (categorie_id) REFERENCES categorie(categorie_id)  -- Foreign key relationship
);

-- Create the commentaire table with a foreign key relationship to forum
CREATE TABLE commentaire (
                             comment_id INT AUTO_INCREMENT PRIMARY KEY,  -- Unique ID for the comment
                             content TEXT NOT NULL,                      -- Content of the comment
                             createdAt DATE NOT NULL,                    -- Creation date of the comment
                             updatedAt DATE NOT NULL,                    -- Last updated date of the comment
                             post_id INT,                                -- Foreign key referencing forum
                             FOREIGN KEY (post_id) REFERENCES forum(post_id)  -- Foreign key relationship
);

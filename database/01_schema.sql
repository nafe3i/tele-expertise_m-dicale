-- Schéma initial du projet Gestion Clinique.
-- À exécuter dans MySQL après `00_create_database.sql`.

USE gestion_clinique;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('INFIRMIER', 'MEDECIN') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE patients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    last_name VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    social_security_number VARCHAR(50) NOT NULL UNIQUE,

    blood_pressure VARCHAR(30) NOT NULL,
    heart_rate INT NOT NULL,
    temperature DECIMAL(4,1) NOT NULL,
    respiratory_rate INT NOT NULL,
    arrived_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_patients_heart_rate_positive CHECK (heart_rate > 0),
    CONSTRAINT chk_patients_temperature_positive CHECK (temperature > 0),
    CONSTRAINT chk_patients_respiratory_rate_positive CHECK (respiratory_rate > 0)
);

CREATE TABLE consultations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    reason VARCHAR(255) NOT NULL,
    observations TEXT NOT NULL,
    diagnosis TEXT NOT NULL,
    prescribed_treatment TEXT NOT NULL,
    cost DECIMAL(10,2) NOT NULL DEFAULT 150.00,
    status ENUM('TERMINEE') NOT NULL,
    closed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_consultations_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id),
    CONSTRAINT fk_consultations_doctor
        FOREIGN KEY (doctor_id) REFERENCES users(id),
    CONSTRAINT chk_consultations_cost_positive CHECK (cost >= 0)
);

-- Accélère les listes de patients arrivés à une date donnée.
CREATE INDEX idx_patients_arrived_at ON patients(arrived_at);

-- Accélère la recherche des consultations d'un patient.
CREATE INDEX idx_consultations_patient_id ON consultations(patient_id);

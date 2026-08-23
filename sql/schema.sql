-- Run this in MySQL Workbench before starting the app.
CREATE DATABASE IF NOT EXISTS dental_clinic;
USE dental_clinic;

-- Login accounts: ADMIN, RECEPTION, DOCTOR (no patient login — reception books for walk-ins)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,          -- ADMIN | RECEPTION | DOCTOR
    full_name VARCHAR(100),
    contact_number VARCHAR(20)
);

-- Extra profile info only doctors have
CREATE TABLE IF NOT EXISTS doctor_profile (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    full_name VARCHAR(100),
    specialization VARCHAR(100),
    contact_number VARCHAR(20),
    consultation_fee DOUBLE DEFAULT 0,
    available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS treatment_type (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    cost DOUBLE NOT NULL
);

-- Doctor's own bookable time slots
CREATE TABLE IF NOT EXISTS schedule (
    id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    schedule_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (doctor_id) REFERENCES doctor_profile(id) ON DELETE CASCADE
);

-- Appointment: patient is a walk-in, so name/contact are typed by reception (no patient login)
CREATE TABLE IF NOT EXISTS appointment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_number VARCHAR(20) UNIQUE NOT NULL,
    patient_name VARCHAR(100) NOT NULL,
    patient_contact VARCHAR(20),
    doctor_id INT NOT NULL,
    treatment_type_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',   -- PENDING | ACCEPTED | REJECTED
    token_number VARCHAR(20),
    notes VARCHAR(500),
    total_cost DOUBLE DEFAULT 0,
    created_by VARCHAR(50),                  -- reception username who booked it
    created_at DATETIME,
    FOREIGN KEY (doctor_id) REFERENCES doctor_profile(id),
    FOREIGN KEY (treatment_type_id) REFERENCES treatment_type(id)
);

-- Default admin login: username = admin, password = admin123 (SHA-256 hash below)
-- Change the password after first login.
INSERT INTO users (username, password, role, full_name, contact_number)
SELECT 'admin', SHA2('admin123', 256), 'ADMIN', 'Clinic Administrator', ''
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO treatment_type (name, cost)
SELECT * FROM (SELECT 'Consultation' AS name, 1000.0 AS cost) t
WHERE NOT EXISTS (SELECT 1 FROM treatment_type WHERE name = 'Consultation');
INSERT INTO treatment_type (name, cost)
SELECT * FROM (SELECT 'Scaling', 3000.0) t
WHERE NOT EXISTS (SELECT 1 FROM treatment_type WHERE name = 'Scaling');
INSERT INTO treatment_type (name, cost)
SELECT * FROM (SELECT 'Filling', 5000.0) t
WHERE NOT EXISTS (SELECT 1 FROM treatment_type WHERE name = 'Filling');
INSERT INTO treatment_type (name, cost)
SELECT * FROM (SELECT 'Extraction', 4000.0) t
WHERE NOT EXISTS (SELECT 1 FROM treatment_type WHERE name = 'Extraction');
INSERT INTO treatment_type (name, cost)
SELECT * FROM (SELECT 'Root Canal', 15000.0) t
WHERE NOT EXISTS (SELECT 1 FROM treatment_type WHERE name = 'Root Canal');

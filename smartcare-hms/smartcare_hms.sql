-- =====================================================================
-- SmartCare Hospital Management System
-- Complete MySQL Database Script
-- Covers: DDL (tables, keys, constraints), sample data, queries,
--         views, stored procedures, functions, triggers
-- =====================================================================

DROP DATABASE IF EXISTS smartcare_hms;
CREATE DATABASE smartcare_hms;
USE smartcare_hms;

-- =====================================================================
-- 1. TABLE CREATION
-- =====================================================================

-- ---------- DEPARTMENTS ----------
CREATE TABLE departments (
    department_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    department_name    VARCHAR(100) NOT NULL UNIQUE,
    location            VARCHAR(100),
    head_doctor_id      BIGINT NULL              -- FK added after doctors table exists
);

-- ---------- DOCTORS ----------
CREATE TABLE doctors (
    doctor_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name           VARCHAR(100) NOT NULL,
    contact_number       VARCHAR(15)  NOT NULL,
    qualification        VARCHAR(150) NOT NULL,
    specialization        VARCHAR(100) NOT NULL,
    consultation_fee      DECIMAL(10,2) NOT NULL CHECK (consultation_fee > 0),
    department_id        BIGINT,
    CONSTRAINT fk_doctor_department
        FOREIGN KEY (department_id) REFERENCES departments(department_id)
        ON DELETE SET NULL
);

-- Now that doctors exists, add the head_doctor FK to departments
ALTER TABLE departments
    ADD CONSTRAINT fk_department_head_doctor
        FOREIGN KEY (head_doctor_id) REFERENCES doctors(doctor_id)
        ON DELETE SET NULL;

-- ---------- STAFF ----------
CREATE TABLE staff (
    staff_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name       VARCHAR(100) NOT NULL,
    contact_number   VARCHAR(15)  NOT NULL,
    role_title       VARCHAR(50)  NOT NULL
);

-- ---------- PATIENTS ----------
CREATE TABLE patients (
    patient_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name          VARCHAR(100) NOT NULL,
    date_of_birth       DATE NOT NULL,
    gender              VARCHAR(10) NOT NULL,
    address             VARCHAR(200),
    contact_number       VARCHAR(15) NOT NULL,
    blood_group          VARCHAR(5)  NOT NULL,
    emergency_contact     VARCHAR(15)
);

-- ---------- ROOMS ----------
CREATE TABLE rooms (
    room_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_number     VARCHAR(10) NOT NULL UNIQUE,
    room_category    ENUM('GENERAL_WARD','PRIVATE_ROOM','ICU') NOT NULL,
    is_available     BOOLEAN NOT NULL DEFAULT TRUE,
    daily_charge      DECIMAL(10,2) NOT NULL CHECK (daily_charge >= 0)
);

-- ---------- APPOINTMENTS ----------
CREATE TABLE appointments (
    appointment_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id           BIGINT NOT NULL,
    doctor_id            BIGINT NOT NULL,
    appointment_date      DATE NOT NULL,
    appointment_time      TIME NOT NULL,
    consultation_room      VARCHAR(20),
    appointment_status     ENUM('SCHEDULED','COMPLETED','CANCELLED') NOT NULL DEFAULT 'SCHEDULED',
    CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    CONSTRAINT fk_appt_doctor  FOREIGN KEY (doctor_id)  REFERENCES doctors(doctor_id)   ON DELETE CASCADE,
    -- Business rule: prevent appointment clashes for the same doctor at the same date/time
    CONSTRAINT uq_doctor_datetime UNIQUE (doctor_id, appointment_date, appointment_time)
);

-- ---------- ADMISSIONS ----------
CREATE TABLE admissions (
    admission_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id         BIGINT NOT NULL,
    room_id             BIGINT NOT NULL,
    admission_date       DATE NOT NULL,
    discharge_date        DATE NULL,
    bed_number           VARCHAR(10),
    admission_status      ENUM('ADMITTED','DISCHARGED') NOT NULL DEFAULT 'ADMITTED',
    CONSTRAINT fk_admission_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    CONSTRAINT fk_admission_room    FOREIGN KEY (room_id)    REFERENCES rooms(room_id)
);

-- ---------- TREATMENTS ----------
CREATE TABLE treatments (
    treatment_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id         BIGINT NOT NULL,
    doctor_id           BIGINT NOT NULL,
    diagnosis           VARCHAR(255) NOT NULL,
    prescription         VARCHAR(255),
    treatment_notes       VARCHAR(500),
    treatment_date         DATE NOT NULL,
    CONSTRAINT fk_treatment_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    CONSTRAINT fk_treatment_doctor  FOREIGN KEY (doctor_id)  REFERENCES doctors(doctor_id)
);

-- ---------- LAB TESTS ----------
CREATE TABLE lab_tests (
    lab_test_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id            BIGINT NOT NULL,
    requested_by_doctor_id  BIGINT,
    test_name              VARCHAR(100) NOT NULL,
    test_date                DATE NOT NULL,
    test_result              VARCHAR(255),
    technician_name           VARCHAR(100),
    test_status                ENUM('PENDING','IN_PROGRESS','COMPLETED') NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_lab_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    CONSTRAINT fk_lab_doctor  FOREIGN KEY (requested_by_doctor_id) REFERENCES doctors(doctor_id)
);

-- ---------- BILLS ----------
CREATE TABLE bills (
    bill_id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id            BIGINT NOT NULL,
    bill_date              DATE NOT NULL,
    consultation_charge      DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (consultation_charge >= 0),
    room_charge               DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (room_charge >= 0),
    lab_charge                 DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (lab_charge >= 0),
    medicine_charge              DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (medicine_charge >= 0),
    total_amount                  DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (total_amount >= 0),
    payment_status                  ENUM('PAID','UNPAID','PARTIALLY_PAID') NOT NULL DEFAULT 'UNPAID',
    payment_method                   ENUM('CASH','CARD','ONLINE') NULL,
    CONSTRAINT fk_bill_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
);

-- =====================================================================
-- 2. SAMPLE DATA
-- =====================================================================

-- ---- Departments (5) ----
INSERT INTO departments (department_name, location) VALUES
('Cardiology', 'Block A - Floor 1'),
('Neurology', 'Block A - Floor 2'),
('Pediatrics', 'Block B - Floor 1'),
('Orthopedics', 'Block B - Floor 2'),
('Radiology', 'Block C - Floor 1');

-- ---- Doctors (5) ----
INSERT INTO doctors (full_name, contact_number, qualification, specialization, consultation_fee, department_id) VALUES
('Dr. Nimal Perera', '0771234567', 'MBBS, MD (Cardiology)', 'Cardiologist', 3500.00, 1),
('Dr. Kavindi Silva', '0772345678', 'MBBS, MD (Neurology)', 'Neurologist', 4000.00, 2),
('Dr. Ruwan Fernando', '0773456789', 'MBBS, DCH', 'Pediatrician', 2500.00, 3),
('Dr. Ishara Jayasuriya', '0774567890', 'MBBS, MS (Ortho)', 'Orthopedic Surgeon', 4500.00, 4),
('Dr. Chathura Bandara', '0775678901', 'MBBS, DMRD', 'Radiologist', 3000.00, 5);

-- Assign heads of department
UPDATE departments SET head_doctor_id = 1 WHERE department_id = 1;
UPDATE departments SET head_doctor_id = 2 WHERE department_id = 2;
UPDATE departments SET head_doctor_id = 3 WHERE department_id = 3;
UPDATE departments SET head_doctor_id = 4 WHERE department_id = 4;
UPDATE departments SET head_doctor_id = 5 WHERE department_id = 5;

-- ---- Patients (10) ----
INSERT INTO patients (full_name, date_of_birth, gender, address, contact_number, blood_group, emergency_contact) VALUES
('Amal Perera', '1985-03-12', 'Male', 'No 12, Galle Road, Colombo', '0711112222', 'O+', '0712223333'),
('Nadeesha Kumari', '1990-07-25', 'Female', 'No 45, Kandy Road, Kurunegala', '0713334444', 'A+', '0714445555'),
('Sunil Jayawardena', '1978-11-02', 'Male', 'No 8, Negombo Road, Gampaha', '0715556666', 'B+', '0716667777'),
('Priyanka De Silva', '1995-01-19', 'Female', 'No 21, Matara Road, Galle', '0717778888', 'AB+', '0718889999'),
('Kasun Wickramasinghe', '1982-09-08', 'Male', 'No 3, Main Street, Jaffna', '0719990000', 'O-', '0710001111');
INSERT INTO patients (full_name, date_of_birth, gender, address, contact_number, blood_group, emergency_contact) VALUES
('Hasini Rathnayake', '2001-05-30', 'Female', 'No 67, Temple Road, Kandy', '0721112233', 'A-', '0722223344'),
('Chamara Gunasekara', '1970-12-14', 'Male', 'No 14, Beach Road, Negombo', '0723334455', 'B-', '0724445566'),
('Dilani Abeywickrama', '1993-04-22', 'Female', 'No 5, Hill Street, Nuwara Eliya', '0725556677', 'O+', '0726667788'),
('Ravindu Senanayake', '1988-08-17', 'Male', 'No 99, Station Road, Anuradhapura', '0727778899', 'AB-', '0728889900'),
('Tharushi Wijesinghe', '1999-02-28', 'Female', 'No 33, Lake Road, Batticaloa', '0729990011', 'A+', '0720001122');

-- ---- Rooms ----
INSERT INTO rooms (room_number, room_category, is_available, daily_charge) VALUES
('G-101', 'GENERAL_WARD', TRUE, 3000.00),
('G-102', 'GENERAL_WARD', TRUE, 3000.00),
('P-201', 'PRIVATE_ROOM', TRUE, 8000.00),
('P-202', 'PRIVATE_ROOM', TRUE, 8000.00),
('ICU-01', 'ICU', TRUE, 20000.00),
('ICU-02', 'ICU', TRUE, 20000.00);

-- ---- Appointments (15) ----
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, consultation_room, appointment_status) VALUES
(1, 1, '2026-08-20', '09:00:00', 'CR-1', 'SCHEDULED'),
(2, 2, '2026-08-20', '09:30:00', 'CR-2', 'SCHEDULED'),
(3, 3, '2026-08-20', '10:00:00', 'CR-3', 'SCHEDULED'),
(4, 4, '2026-08-21', '10:30:00', 'CR-4', 'SCHEDULED'),
(5, 5, '2026-08-21', '11:00:00', 'CR-5', 'SCHEDULED'),
(6, 1, '2026-08-21', '11:30:00', 'CR-1', 'SCHEDULED'),
(7, 2, '2026-08-22', '09:00:00', 'CR-2', 'SCHEDULED'),
(8, 3, '2026-08-22', '09:30:00', 'CR-3', 'SCHEDULED'),
(9, 4, '2026-08-22', '10:00:00', 'CR-4', 'SCHEDULED'),
(10, 5, '2026-08-23', '10:30:00', 'CR-5', 'SCHEDULED'),
(1, 2, '2026-08-23', '11:00:00', 'CR-2', 'SCHEDULED'),
(2, 3, '2026-08-24', '09:00:00', 'CR-3', 'SCHEDULED'),
(3, 1, '2026-08-24', '09:30:00', 'CR-1', 'SCHEDULED'),
(4, 5, '2026-08-25', '10:00:00', 'CR-5', 'SCHEDULED'),
(5, 4, '2026-08-25', '10:30:00', 'CR-4', 'SCHEDULED');

-- ---- Admissions ----
INSERT INTO admissions (patient_id, room_id, admission_date, discharge_date, bed_number, admission_status) VALUES
(3, 5, '2026-08-10', NULL, 'B1', 'ADMITTED'),
(7, 3, '2026-08-12', '2026-08-15', 'B2', 'DISCHARGED'),
(9, 1, '2026-08-14', NULL, 'B3', 'ADMITTED');

UPDATE rooms SET is_available = FALSE WHERE room_id IN (5, 1);

-- ---- Treatments ----
INSERT INTO treatments (patient_id, doctor_id, diagnosis, prescription, treatment_notes, treatment_date) VALUES
(1, 1, 'Hypertension', 'Amlodipine 5mg once daily', 'Monitor BP weekly', '2026-08-20'),
(2, 2, 'Migraine', 'Sumatriptan 50mg as needed', 'Avoid triggers, follow up in 2 weeks', '2026-08-20'),
(3, 3, 'Viral fever', 'Paracetamol 500mg', 'Rest and fluids', '2026-08-20');

-- ---- Lab Tests ----
INSERT INTO lab_tests (patient_id, requested_by_doctor_id, test_name, test_date, test_result, technician_name, test_status) VALUES
(1, 1, 'Lipid Profile', '2026-08-20', 'LDL slightly elevated', 'T. Rangana', 'COMPLETED'),
(2, 2, 'MRI Brain', '2026-08-21', 'No abnormality detected', 'T. Fernando', 'COMPLETED'),
(3, 3, 'Full Blood Count', '2026-08-20', NULL, 'T. Perera', 'PENDING');

-- ---- Bills (10) ----
INSERT INTO bills (patient_id, bill_date, consultation_charge, room_charge, lab_charge, medicine_charge, total_amount, payment_status, payment_method) VALUES
(1, '2026-08-20', 3500, 0, 2500, 800, 6800, 'PAID', 'CARD'),
(2, '2026-08-20', 4000, 0, 6000, 500, 10500, 'PAID', 'CASH'),
(3, '2026-08-20', 2500, 20000, 1500, 400, 24400, 'UNPAID', NULL),
(4, '2026-08-21', 4500, 0, 0, 1200, 5700, 'PAID', 'ONLINE'),
(5, '2026-08-21', 3000, 0, 0, 0, 3000, 'UNPAID', NULL),
(6, '2026-08-21', 3500, 0, 0, 300, 3800, 'PAID', 'CARD'),
(7, '2026-08-22', 4000, 8000, 0, 700, 12700, 'PAID', 'CASH'),
(8, '2026-08-22', 2500, 0, 0, 0, 2500, 'UNPAID', NULL),
(9, '2026-08-22', 4500, 3000, 0, 900, 8400, 'PAID', 'ONLINE'),
(10, '2026-08-23', 3000, 0, 0, 0, 3000, 'UNPAID', NULL);

-- =====================================================================
-- 3. SQL QUERIES (Task 06)
-- =====================================================================

-- 1. Display all patient details
SELECT * FROM patients;

-- 2. List doctors by department
SELECT d.doctor_id, d.full_name, d.specialization, dept.department_name
FROM doctors d
JOIN departments dept ON d.department_id = dept.department_id
ORDER BY dept.department_name;

-- 3. Display appointments scheduled for a specific doctor (example: doctor_id = 1)
SELECT a.appointment_id, p.full_name AS patient_name, a.appointment_date, a.appointment_time, a.appointment_status
FROM appointments a
JOIN patients p ON a.patient_id = p.patient_id
WHERE a.doctor_id = 1
ORDER BY a.appointment_date, a.appointment_time;

-- 4. Find patients admitted to ICU rooms
SELECT p.patient_id, p.full_name, r.room_number, ad.admission_date, ad.admission_status
FROM admissions ad
JOIN patients p ON ad.patient_id = p.patient_id
JOIN rooms r ON ad.room_id = r.room_id
WHERE r.room_category = 'ICU';

-- 5. Display unpaid bills
SELECT b.bill_id, p.full_name, b.total_amount, b.bill_date
FROM bills b
JOIN patients p ON b.patient_id = p.patient_id
WHERE b.payment_status = 'UNPAID';

-- 6. Calculate total revenue generated by the hospital (paid bills only)
SELECT SUM(total_amount) AS total_revenue
FROM bills
WHERE payment_status = 'PAID';

-- 7. Find the most frequently visited doctor
SELECT d.doctor_id, d.full_name, COUNT(a.appointment_id) AS visit_count
FROM appointments a
JOIN doctors d ON a.doctor_id = d.doctor_id
GROUP BY d.doctor_id, d.full_name
ORDER BY visit_count DESC
LIMIT 1;

-- 8. Display patients with multiple appointments
SELECT p.patient_id, p.full_name, COUNT(a.appointment_id) AS appointment_count
FROM appointments a
JOIN patients p ON a.patient_id = p.patient_id
GROUP BY p.patient_id, p.full_name
HAVING COUNT(a.appointment_id) > 1;

-- 9. List laboratory tests completed within a given date range
SELECT lab_test_id, test_name, test_date, test_status
FROM lab_tests
WHERE test_date BETWEEN '2026-08-01' AND '2026-08-31'
  AND test_status = 'COMPLETED';

-- 10. Display room availability
SELECT room_id, room_number, room_category, is_available
FROM rooms
ORDER BY room_category, room_number;

-- =====================================================================
-- 4. VIEWS (Task 07) - at least TWO
-- =====================================================================

-- View 1: Doctor schedule overview (joins doctor + patient + appointment)
CREATE OR REPLACE VIEW vw_doctor_schedule AS
SELECT a.appointment_id, d.doctor_id, d.full_name AS doctor_name, dept.department_name,
       p.full_name AS patient_name, a.appointment_date, a.appointment_time, a.appointment_status
FROM appointments a
JOIN doctors d ON a.doctor_id = d.doctor_id
JOIN patients p ON a.patient_id = p.patient_id
LEFT JOIN departments dept ON d.department_id = dept.department_id;

-- View 2: Patient billing summary
CREATE OR REPLACE VIEW vw_patient_billing_summary AS
SELECT p.patient_id, p.full_name,
       COUNT(b.bill_id) AS total_bills,
       SUM(b.total_amount) AS total_billed,
       SUM(CASE WHEN b.payment_status = 'UNPAID' THEN b.total_amount ELSE 0 END) AS total_outstanding
FROM patients p
LEFT JOIN bills b ON p.patient_id = b.patient_id
GROUP BY p.patient_id, p.full_name;

-- Example usage:
-- SELECT * FROM vw_doctor_schedule;
-- SELECT * FROM vw_patient_billing_summary;

-- =====================================================================
-- 5. STORED PROCEDURES (Task 08) - at least TWO
-- =====================================================================

DELIMITER $$

-- Procedure 1: Book an appointment safely (checks for a doctor clash first)
CREATE PROCEDURE sp_book_appointment (
    IN p_patient_id BIGINT,
    IN p_doctor_id BIGINT,
    IN p_appointment_date DATE,
    IN p_appointment_time TIME,
    IN p_consultation_room VARCHAR(20)
)
BEGIN
    DECLARE clash_count INT;

    SELECT COUNT(*) INTO clash_count
    FROM appointments
    WHERE doctor_id = p_doctor_id
      AND appointment_date = p_appointment_date
      AND appointment_time = p_appointment_time
      AND appointment_status <> 'CANCELLED';

    IF clash_count > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Doctor already has an appointment at this date and time';
    ELSE
        INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, consultation_room, appointment_status)
        VALUES (p_patient_id, p_doctor_id, p_appointment_date, p_appointment_time, p_consultation_room, 'SCHEDULED');
    END IF;
END$$

-- Procedure 2: Discharge a patient and free up their room
CREATE PROCEDURE sp_discharge_patient (
    IN p_admission_id BIGINT
)
BEGIN
    DECLARE v_room_id BIGINT;

    SELECT room_id INTO v_room_id FROM admissions WHERE admission_id = p_admission_id;

    UPDATE admissions
    SET discharge_date = CURDATE(), admission_status = 'DISCHARGED'
    WHERE admission_id = p_admission_id;

    UPDATE rooms SET is_available = TRUE WHERE room_id = v_room_id;
END$$

DELIMITER ;

-- Example usage:
-- CALL sp_book_appointment(1, 3, '2026-09-01', '14:00:00', 'CR-3');
-- CALL sp_discharge_patient(1);

-- =====================================================================
-- 6. FUNCTIONS (Task 09) - at least TWO
-- =====================================================================

DELIMITER $$

-- Function 1: Calculate a patient's current age from date_of_birth
CREATE FUNCTION fn_patient_age (p_patient_id BIGINT)
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_dob DATE;
    DECLARE v_age INT;

    SELECT date_of_birth INTO v_dob FROM patients WHERE patient_id = p_patient_id;
    SET v_age = TIMESTAMPDIFF(YEAR, v_dob, CURDATE());

    RETURN v_age;
END$$

-- Function 2: Get total outstanding (unpaid) balance for a patient
CREATE FUNCTION fn_patient_outstanding_balance (p_patient_id BIGINT)
RETURNS DECIMAL(10,2)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_total DECIMAL(10,2);

    SELECT IFNULL(SUM(total_amount), 0) INTO v_total
    FROM bills
    WHERE patient_id = p_patient_id AND payment_status = 'UNPAID';

    RETURN v_total;
END$$

DELIMITER ;

-- Example usage:
-- SELECT fn_patient_age(1);
-- SELECT fn_patient_outstanding_balance(3);

-- =====================================================================
-- 7. TRIGGERS (Task 10) - at least TWO
-- =====================================================================

DELIMITER $$

-- Trigger 1: Automatically recalculate total_amount before a bill is inserted
CREATE TRIGGER trg_bill_before_insert
BEFORE INSERT ON bills
FOR EACH ROW
BEGIN
    SET NEW.total_amount = IFNULL(NEW.consultation_charge, 0) + IFNULL(NEW.room_charge, 0)
                          + IFNULL(NEW.lab_charge, 0) + IFNULL(NEW.medicine_charge, 0);
END$$

-- Trigger 2: Automatically recalculate total_amount whenever a bill's charges are updated
CREATE TRIGGER trg_bill_before_update
BEFORE UPDATE ON bills
FOR EACH ROW
BEGIN
    SET NEW.total_amount = IFNULL(NEW.consultation_charge, 0) + IFNULL(NEW.room_charge, 0)
                          + IFNULL(NEW.lab_charge, 0) + IFNULL(NEW.medicine_charge, 0);
END$$

-- Trigger 3 (bonus): keep a room marked unavailable automatically when admitted
CREATE TRIGGER trg_admission_after_insert
AFTER INSERT ON admissions
FOR EACH ROW
BEGIN
    UPDATE rooms SET is_available = FALSE WHERE room_id = NEW.room_id;
END$$

DELIMITER ;

-- =====================================================================
-- End of Script
-- =====================================================================

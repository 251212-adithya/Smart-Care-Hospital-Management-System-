-- =====================================================================
-- SmartCare HMS - Sample Data (auto-loaded by Spring Boot on startup)
-- Uses INSERT IGNORE to prevent duplicate key errors on restart
-- =====================================================================

-- ---- Departments (5) ----
INSERT IGNORE INTO departments (department_id, department_name, location) VALUES
(1, 'Cardiology', 'Block A - Floor 1'),
(2, 'Neurology', 'Block A - Floor 2'),
(3, 'Pediatrics', 'Block B - Floor 1'),
(4, 'Orthopedics', 'Block B - Floor 2'),
(5, 'Radiology', 'Block C - Floor 1');

-- ---- Doctors (5) ----
INSERT IGNORE INTO doctors (doctor_id, full_name, contact_number, qualification, specialization, consultation_fee, department_id) VALUES
(1, 'Dr. Nimal Perera', '0771234567', 'MBBS, MD (Cardiology)', 'Cardiologist', 3500.00, 1),
(2, 'Dr. Kavindi Silva', '0772345678', 'MBBS, MD (Neurology)', 'Neurologist', 4000.00, 2),
(3, 'Dr. Ruwan Fernando', '0773456789', 'MBBS, DCH', 'Pediatrician', 2500.00, 3),
(4, 'Dr. Ishara Jayasuriya', '0774567890', 'MBBS, MS (Ortho)', 'Orthopedic Surgeon', 4500.00, 4),
(5, 'Dr. Chathura Bandara', '0775678901', 'MBBS, DMRD', 'Radiologist', 3000.00, 5);

-- Assign heads of department (only if not already set)
UPDATE departments SET head_doctor_id = 1 WHERE department_id = 1 AND head_doctor_id IS NULL;
UPDATE departments SET head_doctor_id = 2 WHERE department_id = 2 AND head_doctor_id IS NULL;
UPDATE departments SET head_doctor_id = 3 WHERE department_id = 3 AND head_doctor_id IS NULL;
UPDATE departments SET head_doctor_id = 4 WHERE department_id = 4 AND head_doctor_id IS NULL;
UPDATE departments SET head_doctor_id = 5 WHERE department_id = 5 AND head_doctor_id IS NULL;

-- ---- Staff ----
INSERT IGNORE INTO staff (staff_id, full_name, contact_number, role_title) VALUES
(1, 'Kumara Wijesinghe', '0781234567', 'Receptionist'),
(2, 'Nilmini Perera', '0782345678', 'Nurse'),
(3, 'Saman Jayawardena', '0783456789', 'Lab Technician');

-- ---- Patients (10) ----
INSERT IGNORE INTO patients (patient_id, full_name, date_of_birth, gender, address, contact_number, blood_group, emergency_contact) VALUES
(1, 'Amal Perera', '1985-03-12', 'Male', 'No 12, Galle Road, Colombo', '0711112222', 'O+', '0712223333'),
(2, 'Nadeesha Kumari', '1990-07-25', 'Female', 'No 45, Kandy Road, Kurunegala', '0713334444', 'A+', '0714445555'),
(3, 'Sunil Jayawardena', '1978-11-02', 'Male', 'No 8, Negombo Road, Gampaha', '0715556666', 'B+', '0716667777'),
(4, 'Priyanka De Silva', '1995-01-19', 'Female', 'No 21, Matara Road, Galle', '0717778888', 'AB+', '0718889999'),
(5, 'Kasun Wickramasinghe', '1982-09-08', 'Male', 'No 3, Main Street, Jaffna', '0719990000', 'O-', '0710001111'),
(6, 'Hasini Rathnayake', '2001-05-30', 'Female', 'No 67, Temple Road, Kandy', '0721112233', 'A-', '0722223344'),
(7, 'Chamara Gunasekara', '1970-12-14', 'Male', 'No 14, Beach Road, Negombo', '0723334455', 'B-', '0724445566'),
(8, 'Dilani Abeywickrama', '1993-04-22', 'Female', 'No 5, Hill Street, Nuwara Eliya', '0725556677', 'O+', '0726667788'),
(9, 'Ravindu Senanayake', '1988-08-17', 'Male', 'No 99, Station Road, Anuradhapura', '0727778899', 'AB-', '0728889900'),
(10, 'Tharushi Wijesinghe', '1999-02-28', 'Female', 'No 33, Lake Road, Batticaloa', '0729990011', 'A+', '0720001122');

-- ---- Rooms ----
INSERT IGNORE INTO rooms (room_id, room_number, room_category, is_available, daily_charge) VALUES
(1, 'G-101', 'GENERAL_WARD', TRUE, 3000.00),
(2, 'G-102', 'GENERAL_WARD', TRUE, 3000.00),
(3, 'P-201', 'PRIVATE_ROOM', TRUE, 8000.00),
(4, 'P-202', 'PRIVATE_ROOM', TRUE, 8000.00),
(5, 'ICU-01', 'ICU', TRUE, 20000.00),
(6, 'ICU-02', 'ICU', TRUE, 20000.00);

-- ---- Treatments ----
INSERT IGNORE INTO treatments (treatment_id, patient_id, doctor_id, diagnosis, prescription, treatment_notes, treatment_date) VALUES
(1, 1, 1, 'Hypertension', 'Amlodipine 5mg once daily', 'Monitor BP weekly', '2026-08-20'),
(2, 2, 2, 'Migraine', 'Sumatriptan 50mg as needed', 'Avoid triggers, follow up in 2 weeks', '2026-08-20'),
(3, 3, 3, 'Viral fever', 'Paracetamol 500mg', 'Rest and fluids', '2026-08-20');

-- ---- Lab Tests ----
INSERT IGNORE INTO lab_tests (lab_test_id, patient_id, requested_by_doctor_id, test_name, test_date, test_result, technician_name, test_status) VALUES
(1, 1, 1, 'Lipid Profile', '2026-08-20', 'LDL slightly elevated', 'T. Rangana', 'COMPLETED'),
(2, 2, 2, 'MRI Brain', '2026-08-21', 'No abnormality detected', 'T. Fernando', 'COMPLETED'),
(3, 3, 3, 'Full Blood Count', '2026-08-20', NULL, 'T. Perera', 'PENDING');

-- ---- Bills (10) ----
INSERT IGNORE INTO bills (bill_id, patient_id, bill_date, consultation_charge, room_charge, lab_charge, medicine_charge, total_amount, payment_status, payment_method) VALUES
(1, 1, '2026-08-20', 3500, 0, 2500, 800, 6800, 'PAID', 'CARD'),
(2, 2, '2026-08-20', 4000, 0, 6000, 500, 10500, 'PAID', 'CASH'),
(3, 3, '2026-08-20', 2500, 20000, 1500, 400, 24400, 'UNPAID', NULL),
(4, 4, '2026-08-21', 4500, 0, 0, 1200, 5700, 'PAID', 'ONLINE'),
(5, 5, '2026-08-21', 3000, 0, 0, 0, 3000, 'UNPAID', NULL),
(6, 6, '2026-08-21', 3500, 0, 0, 300, 3800, 'PAID', 'CARD'),
(7, 7, '2026-08-22', 4000, 8000, 0, 700, 12700, 'PAID', 'CASH'),
(8, 8, '2026-08-22', 2500, 0, 0, 0, 2500, 'UNPAID', NULL),
(9, 9, '2026-08-22', 4500, 3000, 0, 900, 8400, 'PAID', 'ONLINE'),
(10, 10, '2026-08-23', 3000, 0, 0, 0, 3000, 'UNPAID', NULL);

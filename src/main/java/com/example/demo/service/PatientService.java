package com.example.demo.service;

import com.example.demo.entity.Patient;
import com.example.demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    
    @Autowired
    private PatientRepository patientRepository;
    
    // CREATE - Save a new patient
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }
    
    // READ - Get all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
    // READ - Get patient by ID
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }
    
    // READ - Get patient by email
    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }
    
    // READ - Search patients by name
    public List<Patient> searchPatientsByName(String firstName, String lastName) {
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstName, lastName);
    }
    
    // READ - Get patients by status
    public List<Patient> getPatientsByStatus(String status) {
        return patientRepository.findByStatus(status);
    }
    
    // UPDATE - Update patient information
    public Patient updatePatient(Long id, Patient patientDetails) {
        Optional<Patient> patient = patientRepository.findById(id);
        
        if (patient.isPresent()) {
            Patient existingPatient = patient.get();
            
            if (patientDetails.getFirstName() != null) {
                existingPatient.setFirstName(patientDetails.getFirstName());
            }
            if (patientDetails.getLastName() != null) {
                existingPatient.setLastName(patientDetails.getLastName());
            }
            if (patientDetails.getEmail() != null) {
                existingPatient.setEmail(patientDetails.getEmail());
            }
            if (patientDetails.getPhone() != null) {
                existingPatient.setPhone(patientDetails.getPhone());
            }
            if (patientDetails.getDateOfBirth() != null) {
                existingPatient.setDateOfBirth(patientDetails.getDateOfBirth());
            }
            if (patientDetails.getAddress() != null) {
                existingPatient.setAddress(patientDetails.getAddress());
            }
            if (patientDetails.getCity() != null) {
                existingPatient.setCity(patientDetails.getCity());
            }
            if (patientDetails.getState() != null) {
                existingPatient.setState(patientDetails.getState());
            }
            if (patientDetails.getZipCode() != null) {
                existingPatient.setZipCode(patientDetails.getZipCode());
            }
            if (patientDetails.getMedicalHistory() != null) {
                existingPatient.setMedicalHistory(patientDetails.getMedicalHistory());
            }
            if (patientDetails.getStatus() != null) {
                existingPatient.setStatus(patientDetails.getStatus());
            }
            
            return patientRepository.save(existingPatient);
        }
        
        return null;
    }
    
    // DELETE - Delete patient by ID
    public boolean deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // DELETE - Delete all patients
    public void deleteAllPatients() {
        patientRepository.deleteAll();
    }
}

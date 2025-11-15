package com.example.demo.controller;

import com.example.demo.entity.Patient;
import com.example.demo.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PatientController {
    
    @Autowired
    private PatientService patientService;
    
    // CREATE - Add a new patient
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        try {
            Patient createdPatient = patientService.createPatient(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    // READ - Get all patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }
    
    // READ - Get patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.getPatientById(id);
        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // READ - Get patient by email
    @GetMapping("/email/{email}")
    public ResponseEntity<Patient> getPatientByEmail(@PathVariable String email) {
        Optional<Patient> patient = patientService.getPatientByEmail(email);
        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // READ - Search patients by name
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        
        String first = firstName != null ? firstName : "";
        String last = lastName != null ? lastName : "";
        
        List<Patient> patients = patientService.searchPatientsByName(first, last);
        return ResponseEntity.ok(patients);
    }
    
    // READ - Get patients by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Patient>> getPatientsByStatus(@PathVariable String status) {
        List<Patient> patients = patientService.getPatientsByStatus(status);
        return ResponseEntity.ok(patients);
    }
    
    // UPDATE - Update patient information
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patientDetails) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patientDetails);
            if (updatedPatient != null) {
                return ResponseEntity.ok(updatedPatient);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    // DELETE - Delete patient by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        if (patientService.deletePatient(id)) {
            return ResponseEntity.ok("Patient with ID " + id + " deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE - Delete all patients
    @DeleteMapping
    public ResponseEntity<String> deleteAllPatients() {
        patientService.deleteAllPatients();
        return ResponseEntity.ok("All patients deleted successfully");
    }
}

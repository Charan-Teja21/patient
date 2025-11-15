package com.example.demo.service;

import com.example.demo.entity.Patient;
import com.example.demo.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PatientServiceTest {
    
    @Mock
    private PatientRepository patientRepository;
    
    @InjectMocks
    private PatientService patientService;
    
    private Patient patient;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("john@example.com");
        patient.setPhone("555-1234");
        patient.setStatus("Active");
    }
    
    @Test
    void testCreatePatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        
        Patient result = patientService.createPatient(patient);
        
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("john@example.com", result.getEmail());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }
    
    @Test
    void testGetAllPatients() {
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setFirstName("Jane");
        patient2.setLastName("Smith");
        
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient, patient2));
        
        List<Patient> result = patientService.getAllPatients();
        
        assertEquals(2, result.size());
        verify(patientRepository, times(1)).findAll();
    }
    
    @Test
    void testGetPatientById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        
        Optional<Patient> result = patientService.getPatientById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(patientRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetPatientByEmail() {
        when(patientRepository.findByEmail("john@example.com")).thenReturn(Optional.of(patient));
        
        Optional<Patient> result = patientService.getPatientByEmail("john@example.com");
        
        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
        verify(patientRepository, times(1)).findByEmail("john@example.com");
    }
    
    @Test
    void testDeletePatient() {
        when(patientRepository.existsById(1L)).thenReturn(true);
        
        boolean result = patientService.deletePatient(1L);
        
        assertTrue(result);
        verify(patientRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testDeletePatientNotFound() {
        when(patientRepository.existsById(999L)).thenReturn(false);
        
        boolean result = patientService.deletePatient(999L);
        
        assertFalse(result);
        verify(patientRepository, never()).deleteById(999L);
    }
}

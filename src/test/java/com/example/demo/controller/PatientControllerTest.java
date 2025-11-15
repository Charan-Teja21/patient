package com.example.demo.controller;

import com.example.demo.entity.Patient;
import com.example.demo.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PatientControllerTest {
    
    @Mock
    private PatientService patientService;
    
    @InjectMocks
    private PatientController patientController;
    
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Patient patient;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
        objectMapper = new ObjectMapper();
        
        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("john@example.com");
        patient.setPhone("555-1234");
        patient.setStatus("Active");
    }
    
    @Test
    void testCreatePatient() throws Exception {
        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);
        
        mockMvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
        
        verify(patientService, times(1)).createPatient(any(Patient.class));
    }
    
    @Test
    void testGetAllPatients() throws Exception {
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setFirstName("Jane");
        patient2.setLastName("Smith");
        
        when(patientService.getAllPatients()).thenReturn(Arrays.asList(patient, patient2));
        
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
        
        verify(patientService, times(1)).getAllPatients();
    }
    
    @Test
    void testGetPatientById() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(patient));
        
        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.id", is(1)));
        
        verify(patientService, times(1)).getPatientById(1L);
    }
    
    @Test
    void testGetPatientByIdNotFound() throws Exception {
        when(patientService.getPatientById(999L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isNotFound());
        
        verify(patientService, times(1)).getPatientById(999L);
    }
    
    @Test
    void testUpdatePatient() throws Exception {
        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(patient);
        
        mockMvc.perform(put("/api/patients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")));
        
        verify(patientService, times(1)).updatePatient(eq(1L), any(Patient.class));
    }
    
    @Test
    void testDeletePatient() throws Exception {
        when(patientService.deletePatient(1L)).thenReturn(true);
        
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted successfully")));
        
        verify(patientService, times(1)).deletePatient(1L);
    }
    
    @Test
    void testDeletePatientNotFound() throws Exception {
        when(patientService.deletePatient(999L)).thenReturn(false);
        
        mockMvc.perform(delete("/api/patients/999"))
                .andExpect(status().isNotFound());
        
        verify(patientService, times(1)).deletePatient(999L);
    }
}

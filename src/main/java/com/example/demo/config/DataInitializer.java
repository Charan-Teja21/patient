package com.example.demo.config;

import com.example.demo.entity.Patient;
import com.example.demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            // Create sample patients for testing
            Patient patient1 = new Patient();
            patient1.setFirstName("John");
            patient1.setLastName("Doe");
            patient1.setEmail("john.doe@example.com");
            patient1.setPhone("555-0001");
            patient1.setDateOfBirth("1990-01-15");
            patient1.setAddress("123 Main St");
            patient1.setCity("Springfield");
            patient1.setState("IL");
            patient1.setZipCode("62701");
            patient1.setMedicalHistory("No known allergies");
            patient1.setStatus("Active");
            
            Patient patient2 = new Patient();
            patient2.setFirstName("Jane");
            patient2.setLastName("Smith");
            patient2.setEmail("jane.smith@example.com");
            patient2.setPhone("555-0002");
            patient2.setDateOfBirth("1985-05-20");
            patient2.setAddress("456 Oak Ave");
            patient2.setCity("Portland");
            patient2.setState("OR");
            patient2.setZipCode("97201");
            patient2.setMedicalHistory("Allergic to penicillin");
            patient2.setStatus("Active");
            
            Patient patient3 = new Patient();
            patient3.setFirstName("Robert");
            patient3.setLastName("Johnson");
            patient3.setEmail("robert.johnson@example.com");
            patient3.setPhone("555-0003");
            patient3.setDateOfBirth("1975-12-10");
            patient3.setAddress("789 Pine Rd");
            patient3.setCity("Denver");
            patient3.setState("CO");
            patient3.setZipCode("80202");
            patient3.setMedicalHistory("Diabetes Type 2");
            patient3.setStatus("Active");
            
            Patient patient4 = new Patient();
            patient4.setFirstName("Sarah");
            patient4.setLastName("Williams");
            patient4.setEmail("sarah.williams@example.com");
            patient4.setPhone("555-0004");
            patient4.setDateOfBirth("1995-07-08");
            patient4.setAddress("321 Elm St");
            patient4.setCity("Boston");
            patient4.setState("MA");
            patient4.setZipCode("02101");
            patient4.setMedicalHistory("No known conditions");
            patient4.setStatus("Inactive");
            
            // Save sample patients to database
            patientRepository.save(patient1);
            patientRepository.save(patient2);
            patientRepository.save(patient3);
            patientRepository.save(patient4);
            
            System.out.println("Sample patient data loaded successfully!");
        };
    }
}

# Patient Management System - Quick Start Guide

## Prerequisites
- Java 21 or higher installed
- Maven installed (or use the included mvnw)

## Quick Start Steps

### 1. Navigate to Project Directory
```bash
cd /Users/makacharanteja/Downloads/demo
```

### 2. Build the Project
```bash
./mvnw clean install
```

### 3. Run the Application
```bash
./mvnw spring-boot:run
```

You should see output similar to:
```
Started DemoApplication in X.XXX seconds
```

### 4. Test the API

The application will be available at: **http://localhost:8080**

#### Create a Patient (POST)
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "555-1234",
    "dateOfBirth": "1990-01-15",
    "address": "123 Main St",
    "city": "Springfield",
    "state": "IL",
    "zipCode": "62701",
    "medicalHistory": "No known allergies",
    "status": "Active"
  }'
```

#### Get All Patients (GET)
```bash
curl http://localhost:8080/api/patients
```

#### Get Patient by ID (GET)
```bash
curl http://localhost:8080/api/patients/1
```

#### Search by Name (GET)
```bash
curl "http://localhost:8080/api/patients/search?firstName=John"
```

#### Update Patient (PUT)
```bash
curl -X PUT http://localhost:8080/api/patients/1 \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "555-9999",
    "medicalHistory": "Allergic to penicillin"
  }'
```

#### Delete Patient (DELETE)
```bash
curl -X DELETE http://localhost:8080/api/patients/1
```

### 5. View H2 Database Console (Optional)
Navigate to: http://localhost:8080/h2-console

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:testdb`
- User Name: `sa`
- Password: (leave blank)

## Available Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/patients` | Create a new patient |
| GET | `/api/patients` | Get all patients |
| GET | `/api/patients/{id}` | Get patient by ID |
| GET | `/api/patients/email/{email}` | Get patient by email |
| GET | `/api/patients/search` | Search patients by name |
| GET | `/api/patients/status/{status}` | Get patients by status |
| PUT | `/api/patients/{id}` | Update patient |
| DELETE | `/api/patients/{id}` | Delete patient by ID |
| DELETE | `/api/patients` | Delete all patients |

## Troubleshooting

### Port Already in Use
If port 8080 is already in use, change it in `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Database Issues
The H2 in-memory database is automatically created on startup. No manual setup required.

### Module Not Found
If you get module not found errors, run:
```bash
./mvnw clean install
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── entity/Patient.java
│   │   ├── repository/PatientRepository.java
│   │   ├── service/PatientService.java
│   │   ├── controller/PatientController.java
│   │   └── DemoApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/demo/
        ├── service/PatientServiceTest.java
        └── controller/PatientControllerTest.java
```

## Next Steps

- Add user authentication
- Integrate with a real database (PostgreSQL, MySQL)
- Add Swagger/OpenAPI documentation
- Implement email notifications
- Add appointment scheduling
- Create a web UI

For detailed documentation, see `README_PATIENT_MANAGEMENT.md`

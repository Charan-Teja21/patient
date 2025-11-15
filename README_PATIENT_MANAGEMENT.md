# Patient Management System - CRUD Operations

A Spring Boot application that provides comprehensive CRUD (Create, Read, Update, Delete) operations for managing patient records.

## Features

- ✅ **Create**: Add new patient records with complete information
- ✅ **Read**: Retrieve patient information by ID, email, or search by name
- ✅ **Update**: Modify existing patient records
- ✅ **Delete**: Remove patient records individually or in bulk
- ✅ **Search**: Find patients by name, email, or status
- ✅ **Status Management**: Filter patients by active/inactive status

## Technology Stack

- **Framework**: Spring Boot 3.5.7
- **Java Version**: 21
- **Database**: H2 (in-memory, can be switched to PostgreSQL)
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Lombok**: For reducing boilerplate code

## Project Structure

```
src/main/java/com/example/demo/
├── entity/
│   └── Patient.java          # Patient entity with JPA annotations
├── repository/
│   └── PatientRepository.java # Data access layer
├── service/
│   └── PatientService.java   # Business logic layer
├── controller/
│   └── PatientController.java # REST API endpoints
└── DemoApplication.java       # Main Spring Boot application
```

## API Endpoints

### Base URL: `/api/patients`

#### 1. **Create Patient**
- **POST** `/api/patients`
- **Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "555-1234",
  "dateOfBirth": "1990-01-15",
  "address": "123 Main St",
  "city": "Springfield",
  "state": "IL",
  "zipCode": "62701",
  "medicalHistory": "Allergic to penicillin",
  "status": "Active"
}
```

#### 2. **Get All Patients**
- **GET** `/api/patients`
- **Response**: List of all patients

#### 3. **Get Patient by ID**
- **GET** `/api/patients/{id}`
- **Response**: Patient object with given ID

#### 4. **Get Patient by Email**
- **GET** `/api/patients/email/{email}`
- **Response**: Patient object with given email

#### 5. **Search Patients by Name**
- **GET** `/api/patients/search?firstName=John&lastName=Doe`
- **Parameters**: 
  - `firstName` (optional)
  - `lastName` (optional)
- **Response**: List of matching patients

#### 6. **Get Patients by Status**
- **GET** `/api/patients/status/{status}`
- **Response**: List of patients with given status

#### 7. **Update Patient**
- **PUT** `/api/patients/{id}`
- **Request Body**: Patient object with fields to update
- **Response**: Updated patient object

#### 8. **Delete Patient**
- **DELETE** `/api/patients/{id}`
- **Response**: Success message

#### 9. **Delete All Patients**
- **DELETE** `/api/patients`
- **Response**: Success message

## Database Schema

The `patients` table contains the following columns:

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| first_name | VARCHAR | NOT NULL |
| last_name | VARCHAR | NOT NULL |
| email | VARCHAR | NOT NULL, UNIQUE |
| phone | VARCHAR | NOT NULL |
| date_of_birth | VARCHAR | - |
| address | VARCHAR | - |
| city | VARCHAR | - |
| state | VARCHAR | - |
| zip_code | VARCHAR | - |
| medical_history | VARCHAR | - |
| status | VARCHAR | NOT NULL |

## Setup and Running

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher

### Installation

1. **Clone the repository** (if using git):
```bash
cd /Users/makacharanteja/Downloads/demo
```

2. **Build the project**:
```bash
mvn clean install
```

3. **Run the application**:
```bash
mvn spring-boot:run
```

4. **Access the application**:
   - Main API: `http://localhost:8080/api/patients`
   - H2 Console: `http://localhost:8080/h2-console`
   - Database URL for H2 Console: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave blank)

## Configuration

### Switch from H2 to PostgreSQL

Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/patient_db
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

## Testing

### Example cURL Commands

**Create a patient**:
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@example.com",
    "phone": "555-5678",
    "dateOfBirth": "1985-05-20",
    "address": "456 Oak Ave",
    "city": "Portland",
    "state": "OR",
    "zipCode": "97201"
  }'
```

**Get all patients**:
```bash
curl http://localhost:8080/api/patients
```

**Get patient by ID**:
```bash
curl http://localhost:8080/api/patients/1
```

**Update patient**:
```bash
curl -X PUT http://localhost:8080/api/patients/1 \
  -H "Content-Type: application/json" \
  -d '{"phone": "555-9999"}'
```

**Delete patient**:
```bash
curl -X DELETE http://localhost:8080/api/patients/1
```

## Future Enhancements

- User authentication and authorization
- Role-based access control (RBAC)
- Appointment management
- Medical records and prescriptions
- Email notifications
- API documentation with Swagger/OpenAPI
- Unit and integration tests
- Audit logging
- Data validation and error handling improvements

## License

This project is open source and available under the MIT License.

## Author

Created as a demonstration of Spring Boot CRUD operations for patient management.

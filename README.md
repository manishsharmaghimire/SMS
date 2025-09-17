# Student Management System (SMS)

A comprehensive Student Management System built with Spring Boot 3, featuring JWT authentication, role-based access control, and separate profiles for students and teachers.

## 🚀 Features

- **User Authentication**
  - JWT-based authentication
  - Role-based access control (STUDENT, TEACHER, ADMIN)
  - Secure password hashing

- **User Management**
  - Single `User` table for authentication
  - Separate `StudentProfile` and `TeacherProfile` for role-specific data
  - Admin dashboard for user management

- **Technology Stack**
  - **Backend**: Spring Boot 3, Spring Security, JPA
  - **Database**: H2 (in-memory)
  - **Build Tool**: Maven
  - **Authentication**: JWT (JSON Web Tokens)

## 🛠️ Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- Git (optional)

## 🚀 Getting Started

### Clone the Repository
```bash
git clone <repository-url>
cd SMS
```

### Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## 🔐 Authentication

### Register a New User

#### Student Registration
```bash
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "Ram Sharma",
  "email": "ram@mail.com",
  "password": "pass123",
  "role": "STUDENT",
  "grade": "10",
  "rollNumber": "32"
}
```

#### Teacher Registration
```bash
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "Sita Gurung",
  "email": "sita@mail.com",
  "password": "pass123",
  "role": "TEACHER",
  "subject": "Math",
  "salary": 55000
}
```

### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "role": "STUDENT",
  "fullName": "Ram Sharma",
  "email": "ram@mail.com"
}
```

## 🔒 Secured Endpoints

Include the JWT token in the Authorization header for protected endpoints:
```
Authorization: Bearer <your-jwt-token>
```

### Admin Endpoints
- `GET /api/admin/students` - List all students
- `GET /api/admin/teachers` - List all teachers
- `GET /api/admin/student-profiles` - List all student profiles
- `GET /api/admin/teacher-profiles` - List all teacher profiles

## 🌐 API Documentation

API documentation is available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 🛡️ Security

- All passwords are hashed using BCrypt
- JWT tokens have a limited lifespan
- Role-based access control for all endpoints
- CSRF protection enabled
- CORS configuration for secure cross-origin requests

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

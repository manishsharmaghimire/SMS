# Student Management System (JWT + Profiles)

- Single `User` table for authentication
- Separate `StudentProfile` and `TeacherProfile` tables for role-specific data
- JWT-based auth, Spring Boot 3, H2 DB

## Run

```bash
mvn spring-boot:run
```

## Endpoints

- `POST /api/auth/register`
```json
{
  "fullName": "Ram Sharma",
  "email": "ram@mail.com",
  "password": "pass123",
  "role": "STUDENT",
  "grade": "10",
  "rollNumber": "32"
}
```
or
```json
{
  "fullName": "Sita Gurung",
  "email": "sita@mail.com",
  "password": "pass123",
  "role": "TEACHER",
  "subject": "Math",
  "salary": 55000
}
```

- `POST /api/auth/login` → returns `{ token, role, fullName, email }`

Use token:
```
Authorization: Bearer <token>
```

- Admin-only (requires user with role `ADMIN` and its token):
  - `GET /api/admin/students`
  - `GET /api/admin/teachers`
  - `GET /api/admin/student-profiles`
  - `GET /api/admin/teacher-profiles`

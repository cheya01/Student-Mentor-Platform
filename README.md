📚 Student-Mentor Platform API
A Spring Boot REST API built for managing an educational platform where students and mentors can connect, schedule sessions, track performance, and manage roles — all while following modern best practices in security, caching, and scalability.

![API Architecture Overview for Student Platform](https://github.com/user-attachments/assets/47a018ca-be29-41b9-846a-fa9fa6b040d3)


🚀 Features
✅ JWT-Based Authentication
✅ Role-Based Authorization (ADMIN, MENTOR, STUDENT, VISITOR)
✅ CRUD operations for Users, Mentors, Students, Sessions, and Classrooms
✅ Mentor Earnings Reports (filtered by date range)
✅ Session Fee Calculation
✅ Redis Caching for expensive queries like getAllUsers
✅ Validation Groups for contextual field validation
✅ Swagger UI documentation with OpenAPI spec
✅ Centralized Global Exception Handling
✅ Logging with SLF4J & Logback
✅ Actuator Integration (health checks, metrics)
✅ MySQL as the primary database

| Tech              | Description                      |
| ----------------- | -------------------------------- |
| Spring Boot       | Core application framework       |
| Spring Data JPA   | ORM and database interaction     |
| Spring Security   | Authentication and Authorization |
| JWT               | Stateless user authentication    |
| Redis             | In-memory caching layer          |
| MySQL             | Relational data persistence      |
| Swagger / OpenAPI | API documentation and testing    |
| Lombok            | Boilerplate reduction            |
| SLF4J + Logback   | Logging infrastructure           |



📡 API Endpoints
Auth
POST /api/v1/auth/sign-up — Sign up

POST /api/v1/auth/login — Login & receive token

User
GET /api/v1/user — [ADMIN only] Get all users

PUT /api/v1/user/{id} — Update a user

Session
POST /api/v1/session — Create session

PUT /api/v1/session/end/{id} — Mark session as finished and calculate fee

Mentor
GET /api/v1/mentor/{id}/earnings?start_date=...&end_date=... — Get mentor earnings

Student / Mentor / Classroom
Full CRUD available with role-based access


| Role    | Capabilities                                         |
| ------- | ---------------------------------------------------- |
| ADMIN   | Full access to all operations                        |
| MENTOR  | Can create/update sessions, manage their own profile |
| STUDENT | Can manage their own account                         |
| VISITOR | Can only sign up                                     |


JWT tokens must be sent via Authorization: Bearer <token> header.


<img width="801" alt="Screenshot 2025-06-05 at 12 28 08 PM" src="https://github.com/user-attachments/assets/1e5c074c-5559-4a3e-a14e-1dc7b40451ff" />

📘 Swagger UI
Once app is running:
http://localhost:8080/swagger-ui/index.html


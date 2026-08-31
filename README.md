# InPoll - A Poll Management Application

A web application for creating and managing polls with invitations, participation, and result aggregation.  
Developed for the Web Technology Project (WTP) course in the International Computer Science (ICS) program at OTH Regensburg.

## Tech Stack

### Backend
- Java 25 with Spring Boot
- Spring Data JPA
- Spring Security
- MariaDB
- SpringDoc OpenAPI
- Maven

### Frontend
- React
- React Router
- Bootstrap 5
- Vite
- ESLint

### Infrastructure
- Docker & Docker Compose
- MariaDB container

## Architecture

The project follows a layered Spring Boot architecture:

- Controllers: REST API endpoints
- Services: business logic
- Repositories: database access layer
- DTOs: data transfer between backend and frontend
- Entities: database models

Frontend communicates with backend via REST API using HTTP Basic Authentication.

## Use Cases

- User registration and login
- Creating polls with multiple question types (_TEXT, BOOLEAN, NUMERIC_)
- Editing titles, descriptions and due dates of owned polls
- Inviting users to polls
- Viewing owned polls and pending invitations to polls
- Participating in polls
- Viewing individual answers
- Viewing aggregated results for finished polls
- Deleting polls

## Getting Started

### Running Locally

```bash
# Build and start all services
docker-compose up --build

# Access the application:
# - Frontend: http://localhost
# - Backend API: http://localhost:8080
# - Adminer (DB UI): http://localhost:7070
# - API Docs: http://localhost:8080/swagger-ui/index.html
```

### Configuration

Before running the application, you need to set a database password:

1. **In the root `docker-compose.yml`**: Replace `<YOUR_PASSWORD_HERE>` with a strong password in two places:
   - `MARIADB_ROOT_PASSWORD` environment variable
   - The healthcheck `--password` parameter

2. **In `inpoll-backend/src/main/resources/application.properties`**: Update `spring.datasource.password` to match the same password

**Example:**
```yaml
# docker-compose.yml
environment:
  MARIADB_ROOT_PASSWORD: my_secure_password_123

healthcheck:
  test: [ "CMD", "mariadb-admin" ,"ping", "-h", "localhost", "--password=my_secure_password_123" ]
```

```properties
# inpoll-backend/src/main/resources/application.properties
spring.datasource.password=my_secure_password_123
```

⚠️ **Important**: Make sure both passwords match, otherwise the backend won't be able to connect to the database.

## Project Structure

```
ics-wtp-inpoll/
├── inpoll-backend/           
│   ├── src/main/java/oth/ics/wtp/inpollbackend/
│   │   ├── advice/
│   │   ├── config/
│   │   ├── controllers/
│   │   ├── dtos/   
│   │   ├── entities/
│   │   ├── exceptions/      
│   │   ├── repositories/
│   │   ├── services/  
│   │   └── InpollBackendApplication.java
│   ├── pom.xml
│   └── Dockerfile
├── inpoll-frontend/          
│   ├── src/
│   │   ├── api/         
│   │   ├── components/
│   │   ├── context/  
│   │   ├── pages/       
│   │   └── routes/      
│   ├── vite.config.js
│   ├── App.jsx
│   ├── main.jsx
│   ├── package.json
│   └── Dockerfile
├── db-data/           
├── docker-compose.yml
└── README.md
```

## Generative AI Usage

OpenAI ChatGPT was used as a learning and assistance tool during development.

It was primarily used for:
- Explaining Spring Boot, React, Docker, and HTTP concepts
- Debugging configuration issues
- Clarifying framework documentation
- Reviewing code and discussing implementation approaches

All final code was understood, adapted, and integrated by the author. AI-generated code was used only where appropriate and remained within the project guidelines.

---

**Course**: Web Technology Project (WTP)  
**Program**: International Computer Science (ICS)  
**Institution**: OTH Regensburg  
**Semester**: Summer Semester 2026

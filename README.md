# SNRMS Backend (Spring Boot + PostgreSQL)

This is the backend for the Somalia Natural Resources Management System.
It was built by reading your React frontend's code (pages + services) and
matching its exact API expectations — so no frontend changes were needed
except two small integration wires (see bottom of this file).

## 1. Prerequisites

- Java 17+ (`java -version`)
- Maven (or use the wrapper if you add one) — `mvn -version`
- PostgreSQL running locally, with a database created:

```sql
CREATE DATABASE snrms_db;
```

## 2. Configure the database connection

Open `src/main/resources/application.yml` and update these three lines if
your Postgres username/password differ from the defaults:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/snrms_db
    username: postgres
    password: postgres
```

## 3. Run the backend

```bash
cd backend
mvn spring-boot:run
```

On first run you'll see this in the console:

```
No users found - created a default admin account:
  username: admin
  password: Admin@123
```

Hibernate will also auto-create all the tables for you (`ddl-auto: update`).

The API is now live at `http://localhost:8080/api`.

## 4. Run the frontend

Two tiny changes were made to your frontend so it talks to this backend
instead of the placeholder ASP.NET one:

- `vite.config.js` — the dev proxy now points `/api` at `http://localhost:8080`
  instead of `https://localhost:7183`.
- `src/services/api.js` — added an axios interceptor that attaches
  `Authorization: Bearer <token>` to every request, reading the token from
  `localStorage`.
- `src/context/AuthContext.jsx` — saves the token to `localStorage` on
  login, clears it on logout.

Nothing else changed. Run the frontend as usual:

```bash
npm install
npm run dev
```

Log in with `admin` / `Admin@123`.

## 5. How security works here

- `GET` requests on Categories/Resources/Projects/Reports are **public**
  (no login needed) — because your public pages (Home, PublicProjects,
  PublicResearch, PublicGISMap) call those same endpoints without a login.
- Everything else (`POST`/`PUT`/`DELETE`, and all of `/api/Users` except
  `/login` and `/register`) requires a valid JWT token.
- The token is a Bearer token, valid for 24 hours (`app.jwt.expiration-ms`
  in `application.yml`).

## 6. Project structure

```
src/main/java/com/snrms/backend/
  config/        SecurityConfig, DataSeeder
  security/      JwtUtil, JwtAuthFilter, CustomUserDetailsService
  entity/        User, Role, Category, Resource, Project, Report
  dto/           Request/response DTOs with validation annotations
  repository/    Spring Data JPA repositories
  service/       Business logic (CategoryService, ResourceService, etc.)
  mapper/        Entity <-> DTO conversion
  controller/    REST endpoints
  exception/     Custom exceptions + GlobalExceptionHandler
```

## 7. Testing it quickly with curl

```bash
# Log in
curl -X POST http://localhost:8080/api/Users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin","password":"Admin@123"}'

# Create a category (public GET works without a token; this POST needs one)
curl -X POST http://localhost:8080/api/Categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <paste token from login response here>" \
  -d '{"categoryName":"Minerals","description":"Mining and mineral resources"}'
```

## 8. One deliberate deviation from the original spec

The task list asked for a separate `Role` entity. Since Users.jsx only ever
sends/reads role as a plain string (`"Admin"` or `"Manager"`) with no role
management screen, a full Role table + join table would add complexity the
frontend has no use for yet. We used a validated String column instead, and
left a `Role` enum in `entity/Role.java` as a natural place to grow into a
real entity if you later add custom roles or per-role permissions.

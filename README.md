# 🔐 Authentication & RBAC Service

A standalone **Authentication & Authorization** microservice providing user management, JWT-based authentication, and Role-Based Access Control (RBAC).

---

## Features

- **User management**: register, login, logout, profile.
- **Authentication**: short-lived JWT Access Tokens + longer-lived UUID Refresh Tokens.
- **Token lifecycle**: token rotation and revocation (Redis-backed allowlist/blocklist).
- **RBAC**: define roles & permissions; API endpoints protected by role checks (e.g.,`viewer`, `admin`).
- **Security**: password hashing (argon2 / bcrypt), secure secret injection, rate limiting.

---

## 🏗️ Tech Stack 

* **Backend Framework:**
  * **Spring Boot (Java)**

* **Database:**
  * PostgreSQL 
  * Redis 

* **Authentication:**
  * JWT 

* **Containerization:**
  * Docker + Docker Compose

---

## Quick start (local)

- **Prerequisites**: Docker and Docker Compose.

1. Clone:
```bash
git clone https://github.com/eduardodsxavier/AuthRBAC.git
cd AuthRBAC
```

2. Start stack (Postgres + Redis + app):

```bash
mvn spring-boot:run
```

---

## Example API calls

Register (returns access + refresh token):

```bash
curl -v -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"P@ssw0rd"}'
```

Login (returns access + refresh token):

```bash
curl -v -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"P@ssw0rd"}'
```

Refresh (send refresh token in cookie returns access + refresh token):

```bash
curl -v -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  --cookie "refreshToken={token}"
```

Logout (revoke refresh token):

```bash
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  --cookie "refreshToken={token}"
```

---

## API (summary)

| **Method** | **Endpoint**                      | **Auth Required**   | **Description**                                                        |
| ---------- | --------------------------------- | ------------------- | ---------------------------------------------------------------------- |
| **POST**   | `/api/v1/auth/register`           | No                  | Register a new user.                                                   |
| **POST**   | `/api/v1/auth/login`              | No                  | Authenticate user and return access + refresh tokens.                  |
| **GET**    | `/actuator/health`                | No                  | Check server health status.                                            |
| **GET**    | `/api/v1/auth/refresh`            | Yes (Refresh Token) | Generate a new access token using a valid refresh token (from cookie). |
| **GET**    | `/api/v1/auth/logout`             | Yes                 | Revoke user refresh token (stored in Redis).                           |
| **GET**    | `/api/v1/users/me`                | Yes                 | Retrieve the currently authenticated user.                             |
| **PUT**    | `/api/v1/users/update`            | Yes                 | Update user profile information.                                       |
| **DELETE** | `/api/v1/users/delete/me`         | Yes                 | Delete the currently authenticated user.                               |
| **GET**    | `/api/v1/audit-logs/me`           | Yes                 | Get the audit logs for the current user.                               |
| **DELETE** | `/api/v1/users/delete/{username}` | Yes (`admin` only)  | Delete a specific user by username.                                    |
| **POST**   | `/api/v1/users/assign-role`       | Yes (`admin` only)  | Assign a role to a user.                                               |
| **GET**    | `/api/v1/audit-logs`              | Yes (`admin` only)  | Retrieve all audit logs.                                               |
| **GET**    | `/api/v1/roles`                   | Yes (`admin` only)  | List all available roles.                                              |


---

## Securit


* Passwords stored with bcrypt/argon2, never plain text.
* JWTs signed with HMAC256.
* Audit logs for suspicious activities.
* Use HttpOnly, Secure cookies (SameSite=strict) for refresh tokens if you support browsers.
* Implement token rotation and short access token lifetimes.

---

## Roadmap / TODOs

* [ ] Add comprehensive unit & integration tests
* [ ] Configure GitHub Actions (CI)
* [ ] Add logging/structured logs + correlation IDs
* [ ] Harden environment secret management
* [ ] Provide Postman collection / example OpenAPI client
* [ ] docker compose
* [ ] elasticsearch
* [ ] prometheus
* [ ] grafana
* [ ] swagger

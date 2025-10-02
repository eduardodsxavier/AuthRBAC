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
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"P@ssw0rd"}'
```

Login (returns access + refresh token):

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"P@ssw0rd"}'
```

Refresh (send refresh token in cookie returns access + refresh token):

```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  --cookie "refreshToken=<token>"
```

Logout (revoke refresh token):

```bash
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -H "Authorization: Bearer <access-token>"
```

---

## API (summary)

| Method |             Endpoint |    Auth required    | Notes                        |
| ------ | -------------------: | :-----------------: | ---------------------------- |
| POST   |     `/auth/register` |          No         | Create user                  |
| POST   |        `/auth/login` |          No         | Returns access + refresh     |
| POST   |      `/auth/refresh` | Yes (refresh token) | Requires valid refresh token in cookie |
| POST   |       `/auth/logout` |         Yes         | Revoke refresh token (Redis) |
| GET    |          `/users/me` |         Yes         | Access token required        |
| POST   | `/users/assign-role` |         Yes         | `admin` only                 |
| GET    |  `/admin/audit-logs` |         Yes         | `admin` only                 |

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

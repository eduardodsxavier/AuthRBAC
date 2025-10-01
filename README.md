# 🔐 Authentication & RBAC Service

A standalone **Authentication & Authorization** microservice providing user management, JWT-based authentication, and Role-Based Access Control (RBAC). Designed to be reused by other microservices.

---

## Features

- **User management**: register, login, logout, profile.
- **Authentication**: short-lived JWT Access Tokens + longer-lived UUID Refresh Tokens.
- **Token lifecycle**: token rotation and revocation (Redis-backed allowlist/denylist).
- **RBAC**: define roles & permissions; API endpoints protected by role checks (e.g.,`viewer`, `admin`).
- **Security**: password hashing (argon2 / bcrypt), secure secret injection, rate limiting.
- **API-first**: Swagger/OpenAPI docs and example requests.
- **Observability**: Prometheus metrics; Grafana dashboards.
- **DevOps**: Dockerized, CI via GitHub Actions, DB migrations with Flyway/Liquibase.

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

* **DevOps:**
  * GitHub Actions 
  * Prometheus + Grafana 

* **Documentation:**
  * Swagger / OpenAPI 3

---

## Quick start (local)

- **Prerequisites**: Docker and Docker Compose.

1. Clone:
```bash
git clone https://github.com/eduardodsxavier/AuthRBAC.git
cd AuthRBAC
````

2. Copy env example:

```bash
cp .env.example .env
# edit .env if needed
```

3. Start stack (Postgres + Redis + app):

```bash
docker-compose up --build
```

4. Open:

* API: `http://localhost:8080`
* Swagger: `http://localhost:8080/swagger-ui.html` (or `/swagger-ui/index.html` depending on Springdoc version)
* Metrics: `http://localhost:8080/metrics`

---

## Example env variables (`.env.example`)

```
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/authdb
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=changeme
SPRING_REDIS_HOST=redis
JWT_SECRET=replace_with_strong_secret
JWT_ACCESS_EXP_MIN=15
JWT_REFRESH_EXP_DAYS=30
```

---

## Example API calls

Register:

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"P@ssw0rd"}'
```

Login (returns access + refresh token):

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"P@ssw0rd"}'
```

Refresh (example: send refresh token in cookie or Authorization header depending on implementation):

```bash
curl -X POST http://localhost:8080/auth/refresh \
  -H "Content-Type: application/json" \
  --cookie "refreshToken=<token>"
```

Logout (revoke refresh token):

```bash
curl -X POST http://localhost:8080/auth/logout \
  -H "Authorization: Bearer <access-token>"
```

---

## API (summary)

| Method |             Endpoint |    Auth required    | Notes                        |
| ------ | -------------------: | :-----------------: | ---------------------------- |
| GET    |            `/health` |          No         | Health check                 |
| POST   |     `/auth/register` |          No         | Create user                  |
| POST   |        `/auth/login` |          No         | Returns access + refresh     |
| POST   |      `/auth/refresh` | Yes (refresh token) | Requires valid refresh token in cookie |
| POST   |       `/auth/logout` |         Yes         | Revoke refresh token (Redis) |
| GET    |          `/users/me` |         Yes         | Access token required        |
| POST   | `/users/assign-role` |         Yes         | `admin` only                 |
| GET    |  `/admin/audit-logs` |         Yes         | `admin` only                 |

---

## Security recommendations

* Use **Argon2** for password hashing if available.
* Store only refresh token identifiers (UUIDs) server-side (in Redis) rather than the full token.
* Use HttpOnly, Secure cookies (SameSite=strict) for refresh tokens if you support browsers.
* Implement token rotation and short access token lifetimes.
* Rate-limit `/auth/login` and `/auth/refresh`. Track failed attempts per IP/user.
* Store secrets in your cloud secret manager or CI secrets (never in repository).

---

## Testing & CI

* Unit tests: JUnit + Mockito
* Integration tests: Testcontainers for Postgres & Redis
* CI: GitHub Actions — run tests, lint, and build image on PRs

---

## Observability

* Metrics exposed at `/metrics` for Prometheus.
* Track: `auth_login_attempts_total{result="success|failure"}`, `auth_token_refresh_count`, `rbac_role_assignment_total`, request latency histograms.

---

## Roadmap / TODOs

* [ ] Add comprehensive unit & integration tests
* [ ] Configure GitHub Actions (CI)
* [ ] Add logging/structured logs + correlation IDs
* [ ] Harden environment secret management
* [ ] Provide Postman collection / example OpenAPI client


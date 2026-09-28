# Enterprise Digital Banking Platform

A backend for a digital banking system, built with Java and Spring Boot. I built it phase by phase to understand how a real banking backend fits together: users, accounts, money movement, security, caching and event-driven processing.

It is a learning project, but I tried to structure it the way a production service would be structured: layered architecture, DTOs, centralized exception handling, proper transactions, and tests.

## Tech Stack

| Area | Used |
|---|---|
| Language / Framework | Java, Spring Boot, Maven |
| Database | MySQL, Spring Data JPA / Hibernate |
| Security | Spring Security, JWT (access + refresh tokens), BCrypt |
| Caching | Redis |
| Messaging | Apache Kafka |
| Testing | JUnit, Mockito |
| API testing | Postman |

## What it does

**Users and security**
- Register and login with JWT authentication and refresh tokens
- Role-based access (User / Admin)
- Passwords stored with BCrypt

**Accounts**
- Savings and current accounts, with generated account numbers
- Open / close account, account details, balance inquiry
- One user can own multiple accounts

**Transactions**
- Deposit, withdraw and transfer between accounts
- Runs inside `@Transactional`, so a failed transfer rolls back cleanly
- Negative balance is blocked
- Transaction history and mini statement

**Beneficiaries and cards**
- Add / remove / verify beneficiaries
- Debit and virtual cards, block / unblock, change PIN

**APIs**
- Pagination, sorting, filtering and search on users, accounts and transactions

**Notifications and audit**
- Mock email and SMS notifications
- Login history, failed login logs, transaction logs, audit logs, user activity logs

**Performance and async processing**
- Redis caching for user profile, account and balance
- Kafka producers and consumers for transaction events, notifications, audit, email and logging

## Project Structure

```text
src/main/java/com/prajeet/bankingsystem
├── auth          authentication flow
├── config        app configuration (security, redis, kafka)
├── controller    REST endpoints
├── service       business logic
├── repository    JPA repositories
├── entity        database models
├── dto           request / response objects
├── mapper        entity <-> DTO conversion
├── enums         account types, statuses, roles
├── event         Kafka event models
├── consumer      Kafka consumers
├── exception     custom exceptions + global handler
├── jwt           token generation and validation
├── security      security filter chain
├── validation    custom validators
└── util          helpers (e.g. account number generator)
```

## Roadmap and Status

| Phase | Topic | Status |
|---|---|---|
| 1 | Foundation and user management (CRUD, DTO, validation, exception handling) | Done |
| 2 | Authentication and authorization (JWT, roles) | Done |
| 3 | Banking domain (accounts, entity relationships) | Done |
| 4 | Transactions (deposit, withdraw, transfer, history) | Done |
| 5 | Beneficiaries and cards | Done |
| 6 | Production APIs (pagination, sorting, filtering, search) | Done |
| 7 | Notifications and audit logging | Done |
| 8 | Redis caching | Done |
| 9 | Kafka event-driven processing | Done |
| 10 | Testing (unit, service, controller, repository, integration) | Done |
| 11 | Docker and Docker Compose | Planned |

## Running it locally

**You need:** JDK 17+ (or whatever version `pom.xml` says), Maven (or use the included `mvnw`), MySQL, Redis and Kafka running locally.

1. Clone the repo
   ```bash
   git clone https://github.com/prajeetdubey/enterprise-digital-banking-platform.git
   cd enterprise-digital-banking-platform
   ```

2. Create a MySQL database
   ```sql
   CREATE DATABASE banking_system;
   ```

3. Open `src/main/resources/application.properties` and set your own MySQL username and password. Check the Redis and Kafka host/port there too.

4. Start the app
   ```bash
   ./mvnw spring-boot:run
   ```
   On Windows: `mvnw.cmd spring-boot:run`

5. The API runs on `http://localhost:8080`. There is a Postman collection in the `postman` folder you can import to try the endpoints.

## Running tests

```bash
./mvnw test
```

## What's next

- Dockerize the app with MySQL, Redis and Kafka through Docker Compose, so the whole thing starts with one command
- Swagger / OpenAPI docs
- CI pipeline with GitHub Actions

## Author

**Prajeet Dubey**
GitHub: [@prajeetdubey](https://github.com/prajeetdubey)

# 🍃 SentryPay Bank Backend

SentryPay Backend is a secure, modern REST API engineered using Spring Boot 4.x and Java 17. It serves as the transaction processor and authentication engine for the SentryPay ecosystem, powering the native Jetpack Compose Android client application.

---

## 🏗️ Architecture & Directory Blueprint

The repository utilizes a **Feature-by-Domain Layered Architecture**. Instead of clustering code by technology layers globally (all controllers in one folder), components are isolated by their core business domain (`user`, `transaction`, etc.). This guarantees rapid refactoring and straightforward modularization down the road.

```text
sentrypay-backend/
├── src/main/java/com/sentrypay/backend/
│   ├── config/                     # Global infrastructure & Security (JWT, WebMVC)
│   ├── common/                     # Cross-cutting exceptions & API payload wrappers
│   │
│   └── domain/                     # Isolated Business Domains
│       └── user/                   # User Management Domain Space
│           ├── controller/         # REST Controllers exposing endpoints
│           ├── service/            # Core business logic processing engine
│           ├── repository/         # Spring Data JPA database interfaces
│           ├── entity/             # Relational Database Mapping Tables
│           └── dto/                # Network Data Transfer Objects
│
└── src/main/resources/
    ├── application.properties      # Core configuration properties
    └── db/migration/               # Schema version tracking (Future expansion)
```

---

## 🛠️ Tech Stack & Tooling

- **Core Framework:** Spring Boot 4.1.0
- **Language:** Java 17 (Eclipse Adoptium OpenJDK)
- **Build Pipeline & Toolchain:** Gradle (Groovy DSL)
- **Persistence & Mapping:** Spring Data JPA / Hibernate ORM
- **Databases:**
  - **In-Memory Environment:** H2 Database Engine (fast prototyping)
  - **Production Pipeline:** PostgreSQL
- **Security Layer:** Spring Security (default context protection enabled)
- **Boilerplate Optimization:** Project Lombok (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`)

---

## 🚀 Getting Started (Local Development)

### Prerequisites

Ensure your local environment matches the baseline tool configuration:

1. **Java Development Kit (JDK):** Version 17 or higher installed on your machine PATH.
2. **IDE:** Visual Studio Code (with the *Extension Pack for Java*) or *IntelliJ IDEA*.

### ⚡ Running the Server Locally

Because VS Code caches project bindings independently of Gradle tasks, it is always recommended to use the included Gradle Wrapper (`gradlew`) to cleanly build and boot the application engine.

Open your local terminal workspace and execute:

```bash
# For Linux or Mac terminal shells
./gradlew clean bootRun

# For Windows PowerShell / Command Prompt
./gradlew.bat clean bootRun
```

The terminal console will stream the active initialization telemetry log. The engine is fully operational once you see the validation completion line:

```
Tomcat started on port 8080 (http) with context path '/'
```

---

## 🔒 Security Configuration Notes

For rapid prototyping and early-stage REST layer builds, SentryPay includes `spring-boot-starter-security`.

- Upon startup, check your console stream logs for a line reading:
  `Using generated security password: [UUID-STRING-HERE]`
- When executing network checks against endpoints directly via a web browser (`http://localhost:8080`), log in using username `user` and provide the printed UUID string as the passkey.

---

## 📌 Baseline API Schema Integration Matrix

### Core Entities & DTO Mappings

To prevent sensitive operational parameters (internal generated DB IDs, raw timestamps, or cryptographic credential chains) from bleeding directly onto network pipelines, SentryPay relies on distinct DTO constraints:

1. **`UserEntity`:** Handles internal persistent tables mapped to database blocks.
2. **`UserRegistrationDto`:** Isolated wrapper holding inbound request inputs (`username`, `email`, `password`) arriving from the Jetpack Compose app interface.

---

## 🤝 Project Alignment

This repository operates strictly under a **Polyrepo Configuration** scheme. Frontend user-interface updates or mobile dependencies deployed to the Play Store via the Android repository live completely decoupled from backend deployments here, ensuring structural changes inside backend routines never unintentionally interrupt user interfaces.
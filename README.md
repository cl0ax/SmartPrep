# SmartPrep

**An adaptive coding-interview prep platform that meets each user at their skill level.**

SmartPrep helps users practice coding and interview-style questions tailored to
their proficiency. It executes and validates submitted solutions, scores them,
tracks skill over time, and adapts problem difficulty so practice stays
challenging but never overwhelming - the way a good interviewer would push you.

Built as a full-stack capstone project with a React frontend, a Spring Boot
backend, and a cloud-hosted database.

---

## Features

- **Proficiency scoring engine** - computes a skill level (1-100) from an intake
  questionnaire with experience-level multipliers, then maps that score to
  Easy / Medium / Hard problem tiers.
- **Adaptive difficulty** - problems scale up or down as the user answers, so the
  difficulty tracks their actual ability instead of a fixed path.
- **Server-side code execution** - user-submitted Java is compiled and run on the
  backend via the JDK `JavaCompiler` API, then checked against test cases.
- **Test-case validation & runtime measurement** - solutions are verified for
  correctness and timed for efficiency.
- **AI interview practice** - an integrated chatbot (Google Gemini) evaluates
  answers against a custom rubric to give structured, consistent feedback.
- **Secure authentication** - all endpoints protected with Spring Security,
  password hashing, and unique-constraint enforcement.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React |
| Backend | Java 17, Spring Boot, Spring Security |
| Database | MySQL (AWS RDS in production) |
| Code Execution | JDK `JavaCompiler` API |
| AI | Google Gemini API |
| Build | Maven (backend), npm (frontend) |

---

## Architecture

SmartPrep follows a client-server design with a clear data flow end to end:

```
React frontend  ⇄  Spring Boot REST API  ⇄  MySQL / AWS RDS
                          │
                          ├── JavaCompiler  → compile & run user code, validate test cases
                          └── Gemini API     → AI evaluation against a custom rubric
```

The backend owns all execution and scoring logic, so the client never trusts
user-submitted code directly - submissions are compiled, sandboxed, and validated
server-side before results return to the UI.

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Node.js & npm
- MySQL (or an AWS RDS instance)
- An IDE such as IntelliJ IDEA (optional)

### Backend

1. Configure your database connection and API keys in
   `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://<host>:3306/smartprep
   spring.datasource.username=<user>
   spring.datasource.password=<password>
   gemini.api.key=<your-gemini-key>
   ```
2. From the project root, build and run:
   ```bash
   mvn spring-boot:run
   ```

### Frontend

```bash
cd frontend
npm install
npm start
```

The frontend runs on `http://localhost:3000` and the backend API on
`http://localhost:8080` by default.

---

## What I Learned

SmartPrep was my first end-to-end full-stack build, and the hardest parts were
the ones that mattered most: safely executing untrusted user code on the server,
designing a scoring engine that actually *felt* adaptive, and controlling an AI
model by injecting our own rubric into the prompt instead of trusting raw output.
It taught me how the pieces of a real application - auth, data flow, execution,
and a third-party API - fit together, and how much of good engineering is
decisions made before you write the first line of code.

---

## Team

Group capstone project.
- **Joey** (Joseph Barron)
- **Abraham**
- **Ibrahim**

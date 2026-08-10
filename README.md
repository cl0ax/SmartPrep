# SmartPrep

An adaptive coding interview prep platform. React front end, Spring Boot back
end, MySQL, with server side execution of submitted Java and AI generated
interview practice.

![SmartPrep: sign up, proficiency assessment, dashboard, running a solution, AI interview practice](docs/demo.gif)

The recording is a real local run: signing up, the four question proficiency
assessment, the dashboard where those answers become live proficiency scores,
opening a problem, running a solution against its test case, and the AI
interview practice screen.

---

## About this fork

This is a fork of [josephbarron-dev/SmartPrep](https://github.com/josephbarron-dev/SmartPrep),
a three person capstone project. Joseph Barron created and owns the original.

**What I contributed to the original repository**, visible in its commit
history:

- **The AI feedback subsystem.** `ChatbotService` and `ChatbotServiceImpl`, the
  `ChatbotController`, the request and response DTOs, and the `ChatbotPage`
  React component and its styling. This is the "AI Practice" screen in the
  recording: pick a category and difficulty, get a generated interview question,
  submit an answer, get evaluated.
- **The JPA domain model.** The `Problem`, `Submission`, `Proficiency`,
  `Category`, `User` and `TestCase` entities, plus the `ProblemDifficulty` and
  `SolutionRating` enums. These are the tables in `db/schema.sql`.

Roughly 1,400 lines across those two areas. The rest of the application is my
teammates' work.

**What this fork adds on top of the original:**

- `src/main/resources/db/schema.sql` and `db/seed.sql`, so the project can
  actually be started from a clean clone (see below)
- this README and the demo recording

---

## Running it locally

The original was developed against AWS RDS and the schema was never committed,
so a clean clone could not start: `spring.jpa.hibernate.ddl-auto` is `none` and
there were no migrations. That is what `db/schema.sql` fixes.

### Requirements

- JDK 17 or newer. A full JDK, not a JRE, because submitted solutions are
  compiled at runtime through `javax.tools.JavaCompiler`.
- MySQL 8.0 or newer
- Node.js and npm
- A Google Gemini API key, for the AI practice screen only

### 1. Database

```bash
mysql -u root -e "CREATE DATABASE smartprep;"
mysql -u root smartprep < src/main/resources/db/schema.sql
mysql -u root smartprep < src/main/resources/db/seed.sql
```

The seed gives you three categories and a few problems with test cases, which is
the minimum the dashboard and the coding view need. Users and proficiency rows
are created by the signup flow, so do not seed those.

### 2. Backend

```bash
DB_URL=jdbc:mysql://localhost:3306/smartprep \
DB_USERNAME=youruser \
DB_PASSWORD=yourpassword \
GEMINI_API_KEY=yourkey \
./mvnw spring-boot:run
```

Serves on port 8080.

### 3. Frontend

```bash
cd frontend
npm install
npm start
```

Serves on port 3000.

**Both ports matter.** The frontend calls `http://localhost:8080` directly, and
the controllers are annotated `@CrossOrigin(origins = "http://localhost:3000")`.
Run either on a different port and requests are blocked by CORS until you change
both sides.

---

## How it fits together

```
  React (3000)  ──HTTP──►  Spring Boot (8080)  ──JPA──►  MySQL
                                   │
                                   ├─► javax.tools.JavaCompiler   (run a solution)
                                   └─► Gemini API                 (AI practice)
```

- `POST /api/v1/users` and `/login` handle accounts
- `GET /api/v1/proficiencies/{userId}/{categoryId}` backs the dashboard bars
- `GET /api/v1/problem/{userId}/{categoryId}` selects a problem for that user
- `POST /api/v1/solution/run` compiles and runs a solution against the sample
  test case; `POST /api/v1/solution` submits it
- `POST /api/v1/chatbot/evaluate` scores a written interview answer

The proficiency assessment at signup seeds the initial per category scores,
which is why two different sets of answers produce different dashboard
percentages.

---

## Known limitations

- **Submitted Java is compiled and executed on the server with no sandbox.**
  This is fine on localhost. It is not safe to expose publicly as is: pasted
  code can read environment variables, touch the filesystem and open network
  connections. Hosting this would require a locked down container per run.
- The AI practice screen needs `GEMINI_API_KEY`. Without it the rest of the app
  works and only that screen fails.
- `db/schema.sql` is generated from the current JPA entities rather than
  recovered from the original RDS instance, which was never committed. It is
  consistent with the code, and loading schema then seed into an empty database
  was verified to produce a working app.

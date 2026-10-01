<h1 align="center">SmartPrep</h1>

<p align="center">
  Adaptive coding interview prep: a proficiency score per topic, Java run against real test cases on the server, and AI feedback on interview answers.
</p>

<p align="center">
  <a href="#features">Features</a> ·
  <a href="#architecture">Architecture</a> ·
  <a href="#running-it-locally">Running it locally</a> ·
  <a href="#notes">Notes</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/language-Java%2017-orange" alt="Java 17">
  <img src="https://img.shields.io/badge/framework-Spring%20Boot%203.5-6db33f" alt="Spring Boot 3.5">
  <img src="https://img.shields.io/badge/frontend-React%2019-61dafb" alt="React 19">
  <img src="https://img.shields.io/badge/database-MySQL%208-4479a1" alt="MySQL 8">
  <img src="https://img.shields.io/badge/AI-Google%20Gemini-8e75b2" alt="Google Gemini">
  <img src="https://img.shields.io/badge/team-3%20person%20capstone-lightgrey" alt="Three person capstone team">
</p>

<p align="center">
  <img src="docs/demo.gif" width="720" alt="SmartPrep end to end: signing up, the onboarding questions, the dashboard, solving a problem, the submission history and the AI interview">
</p>

Every recording on this page is a real local run against the real backend. Nothing is mocked.

## Features

<table>
  <tr>
    <td width="50%" valign="top">
      <b>Sign up and sign in</b><br><br>
      <img src="docs/auth.gif" width="100%" alt="A new account signing up"><br>
      Client side validation on signup, and a failed login says what went wrong instead of failing silently. Passwords are hashed before they are stored.
    </td>
    <td width="50%" valign="top">
      <b>Proficiency assessment</b><br><br>
      <img src="docs/assessment.gif" width="100%" alt="The onboarding assessment questions"><br>
      A new account answers four short questions, and the answers seed a starting proficiency for each topic. Two accounts that answer differently start in different places.
    </td>
  </tr>
  <tr>
    <td width="50%" valign="top">
      <b>Dashboard</b><br><br>
      <img src="docs/dashboard.gif" width="100%" alt="The dashboard with a progress bar per topic"><br>
      A live progress bar for each topic. Pick a topic to get a problem matched to you, or let <b>Random Problem</b> choose.
    </td>
    <td width="50%" valign="top">
      <b>Solving a problem</b><br><br>
      <img src="docs/solve.gif" width="100%" alt="Typing a Java solution in the editor and running it"><br>
      A Monaco editor with Java highlighting. <b>Run</b> compiles your code on the server with <code>javax.tools.JavaCompiler</code> and checks it against the sample test case; <b>Submit</b> records the attempt and updates your proficiency.
    </td>
  </tr>
  <tr>
    <td width="50%" valign="top">
      <b>Submission history</b><br><br>
      <img src="docs/history.gif" width="100%" alt="A graded submission in the history, still there after signing back in"><br>
      Every graded solution is saved. <b>View Previous Submissions</b> lists them newest first with the problem, the grade, the time and the code, and they are still there after signing back in.
    </td>
    <td width="50%" valign="top">
      <b>AI interview practice</b><br><br>
      <img src="docs/ai-interview.gif" width="100%" alt="A medium Two Pointers question answered and scored 99 out of 100"><br>
      Pick a topic and a difficulty, write an answer to an interview question, and Google Gemini returns a score with written feedback.
    </td>
  </tr>
</table>

## Architecture

```
  React 19 (3000)  ──HTTP──►  Spring Boot 3.5 (8080)  ──JPA / Hibernate──►  MySQL 8
      Monaco                        │
                                    ├─► javax.tools.JavaCompiler   run a solution
                                    └─► Google Gemini              grade an answer
```

| Endpoint | Purpose |
|---|---|
| `POST /api/v1/users` | create an account |
| `POST /api/v1/users/login` | authenticate |
| `GET /api/v1/proficiencies/{userId}/{categoryId}` | dashboard progress bars |
| `GET /api/v1/problem/{userId}/{categoryId}` | select a problem for this user |
| `POST /api/v1/solution/run` | compile and run against the sample test case |
| `POST /api/v1/solution` | submit, record, update proficiency |
| `GET /api/v1/solution/submissions?userId=` | submission history |
| `POST /api/v1/chatbot/evaluate` | grade a written interview answer |

Six tables: `Users`, `Categories`, `Problems`, `Test_Cases`, `Submissions`,
`Proficiencies`. Full DDL in [`src/main/resources/db/schema.sql`](src/main/resources/db/schema.sql).

---

## Running it locally

### Requirements

- **A full JDK 17+**, not a JRE. Submitted solutions are compiled at runtime.
- MySQL 8.0+
- Node.js and npm
- A Google Gemini API key for the AI practice screen. `GEMINI_API_KEY` has to be set
  for the backend to start; any value works if you skip that screen.

### 1. Database

```bash
mysql -u root -e "CREATE DATABASE smartprep;"
mysql -u root smartprep < src/main/resources/db/schema.sql
mysql -u root smartprep < src/main/resources/db/seed.sql
```

The seed provides three categories and eight problems across all three
difficulties, with their test cases. Users and proficiency rows are created by
the signup flow, so they are not seeded.

For an existing database, run `ALTER TABLE Submissions MODIFY answer TEXT;`.

### 2. Backend

```bash
DB_URL=jdbc:mysql://localhost:3306/smartprep \
DB_USERNAME=youruser \
DB_PASSWORD=yourpassword \
GEMINI_API_KEY=yourkey \
./mvnw spring-boot:run
```

Port 8080.

### 3. Frontend

```bash
cd frontend
npm install
npm start
```

Port 3000.

**Both ports matter.** The front end calls `http://localhost:8080` directly and
the controllers are annotated `@CrossOrigin(origins = "http://localhost:3000")`.
Change one and you must change the other, or the browser blocks every request.

---

## Notes

SmartPrep started as a three person capstone project, and this repository continues
from the team's final version. I wrote the questions for the onboarding assessment, and a
teammate built the screen that asks them. I built the AI feedback subsystem end to end:
`ChatbotService`, `ChatbotServiceImpl`, `ChatbotController`, the request and response
DTOs, the `ChatbotPage` React component and its styling, and the proficiency adjustment
applied after each evaluation. The rest of the original app is my teammates' work.

After the class I added the database schema and seed so it runs from a clean clone,
`TEXT` columns for problem content (starter code used to get cut at 255 characters and
squashed onto one line), and the coding submission history.

It runs locally; there is no hosted instance. There is no logout button yet, which is
why the history recording reloads the page to get back to the sign-in screen. The backend
reads `GEMINI_API_KEY` at startup, so it has to be set even if you never open the AI
practice screen. A placeholder value is enough for everything else; only that screen
needs a real key.

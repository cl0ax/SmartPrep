import { useState } from "react";
import Login from "./Login";
import "./App.css";
import Questionnaire from "./Questionnaire";
import { INTAKE_QUESTIONS } from "./Questions";
import ProblemPage from "./ProblemPage";
import ProblemSelection from "./ProblemSelection";
import ChatbotPage from "./ChatbotPage";
import { UserProvider, useUser } from "./UserContext";
import ResultsPage from "./ResultsPage";
import SubmissionHistory from "./SubmissionHistory";

function AppContent() {
  const { user } = useUser();
  const [screen, setScreen] = useState("login");
  const [selectedTopic, setSelectedTopic] = useState("");
  const [selectedProblem, setSelectedProblem] = useState(null);
  const [problemResults, setProblemResults] = useState(null);

  return (
    <>
      {screen === "login" && (
        <Login
          goToQuestionnaire={() => {
            setScreen("questionnaire");
          }}
          goToProblemSelection={() => {
            setScreen("problemSelection");
          }}
        />
      )}

      {screen === "questionnaire" && (
        <Questionnaire
          questions={INTAKE_QUESTIONS}
          goBack={() => setScreen("login")}
          onSubmit={(payload) => {
            setScreen("problemSelection");
          }}
        />
      )}

      {screen === "results" && (
        <ResultsPage
          result={problemResults}
          goBack={() => setScreen("problemSelection")}
        />
      )}

      {screen === "submissions" && (
        <SubmissionHistory goBack={() => setScreen("problemSelection")} />
      )}

      {screen === "problemSelection" && (
        <ProblemSelection
          goBack={() => setScreen("questionnaire")}
          goToProblemPage={(problemDto, topic) => {
            setSelectedProblem(problemDto);
            setSelectedTopic(topic || "");
            setScreen("ProblemPage");
          }}
          goToChatbot={() => setScreen("chatbot")}
          goToSubmissions={() => setScreen("submissions")}
        />
      )}

      {screen === "ProblemPage" && (
        <ProblemPage
          topic={selectedTopic}
          problem={selectedProblem}
          userId={user?.userId}
          onSubmit={(resultData) => {
            setProblemResults(resultData || null);
            setScreen("results");
          }}
          goBack={() => setScreen("problemSelection")}
        />
      )}

      {screen === "chatbot" && (
        <ChatbotPage goBack={() => setScreen("problemSelection")} />
      )}
    </>
  );
}

function App() {
  return (
    <UserProvider>
      <AppContent />
    </UserProvider>
  );
}

export default App;

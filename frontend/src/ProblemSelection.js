import "./App.css";
import "./ProblemSelection.css";
import { useEffect, useState } from "react";
import { useUser } from "./UserContext";

export default function ProblemSelection({ goToProblemPage, goBack, goToChatbot, goToSubmissions }) {
  const { user } = useUser();
  const [isLoadingProblem, setIsLoadingProblem] = useState(false);
  const [problemError, setProblemError] = useState("");
  const [proficiencies, setProficiencies] = useState({});

  useEffect(() => {
    const userId = user.userId;

    if (!userId) {
      return;
    }

    async function fetchProficiencies() {
      try {
        const profMap = {};

        for (const categoryId of [1, 2, 3]) {
          const res = await fetch(`http://localhost:8080/api/v1/proficiencies/${userId}/${categoryId}`, {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
          });

          if (!res.ok) {
            throw new Error("Failed to fetch proficiencies");
          }

          const data = await res.json();
          profMap[categoryId] = data.proficiency ?? data;
        }

        setProficiencies(profMap);
      } catch (error) {
        console.error("Failed to load proficiencies:", error);
      }
    }

    fetchProficiencies();
  }, [user]);

  const topics = [
    { key: "arrays", label: "Arrays & Strings", progress: proficiencies[1] ?? 0, categoryId: 1 },
    { key: "twoPointers", label: "Two Pointers", progress: proficiencies[2] ?? 0, categoryId: 2 },
    { key: "hashMaps", label: "Hash Maps", progress: proficiencies[3] ?? 0, categoryId: 3 },
  ];

  async function handleTopicSelect(topic) {
    const userId = user?.userId;

    if (!userId) {
      setProblemError("No user is loaded yet.");
      return;
    }

    setIsLoadingProblem(true);
    setProblemError("");

    try {
      const res = await fetch(`http://localhost:8080/api/v1/problem/${userId}/${topic.categoryId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!res.ok) {
        throw new Error("Failed to fetch problem");
      }

      const problemDto = await res.json();
      console.log("Loaded problem:", problemDto);
      goToProblemPage(problemDto, topic.label);
    } catch (error) {
      console.error("Error fetching problem:", error);
      setProblemError("Could not load a problem for this category.");
    } finally {
      setIsLoadingProblem(false);
    }
  }

  return (
    <main className="selection-shell">
      <div className="selection-card">
        <div className="selection-topbar">
          <h1>SmartPrep</h1>
          <div className="selection-user">@{user?.username || "username"}</div>
        </div>

        <div className="selection-layout dashboard-layout">
          <section className="problem-panel">
            <div className="panel-label">Choose a coding topic</div>

            <div className="topic-scroll-list">
              {topics.map((topic) => (
                <button
                  key={topic.key}
                  type="button"
                  className="topic-card"
                  onClick={() => handleTopicSelect(topic)}
                >
                  <div className="topic-title">{topic.label}</div>

                  <div className="topic-progress-row">
                    <span className="topic-progress-label">{topic.progress}%</span>
                    <div className="topic-progress-track">
                      <div
                        className="topic-progress-fill"
                        style={{ width: `${topic.progress}%` }}
                      ></div>
                    </div>
                  </div>
                </button>
              ))}
            </div>

            <button
              type="button"
              className="random-problem-btn"
              onClick={() => {
                const randomTopic = topics[Math.floor(Math.random() * topics.length)];
                handleTopicSelect(randomTopic);
              }}
            >
              Random Problem
            </button>

            {isLoadingProblem && (
              <div className="selection-status">Loading problem...</div>
            )}

            {problemError && (
              <div className="selection-status error-text">{problemError}</div>
            )}
          </section>

          <section className="practice-panel">
            <button
              type="button"
              className="feature-card interview-card"
              onClick={goToChatbot}
            >
              <span className="feature-eyebrow">AI Practice</span>
              <span className="feature-title">Interview Question Practice</span>
              <span className="feature-copy">
                Practice technical interview questions with AI feedback.
              </span>
            </button>

            <button
              type="button"
              className="feature-card submissions-card"
              onClick={goToSubmissions}
            >
              <span className="feature-eyebrow">History</span>
              <span className="feature-title">View Previous Submissions</span>
              <span className="feature-copy">
                Review past answers, grades, and submission dates.
              </span>
            </button>

          </section>
        </div>
      </div>
    </main>
  );
}

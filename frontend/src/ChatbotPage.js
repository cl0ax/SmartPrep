import { useState } from "react";
import "./ChatbotPage.css";
import { useUser } from "./UserContext";

// Question bank: category label → difficulty → question text
const QUESTION_BANK = {
    "Arrays & Strings": {
        Easy:   "What is an array and how would you find duplicate values in one?",
        Medium: "Explain the sliding window technique and when you would use it.",
        Hard:   "How would you find the longest substring without repeating characters? Walk through your approach.",
    },
    "Two Pointers": {
        Easy:   "What is the two-pointer technique and when does it apply?",
        Medium: "How would you use two pointers to check if a string is a valid palindrome?",
        Hard:   "How does the two-pointer approach improve brute force for problems on sorted arrays? Give an example.",
    },
    "Hash Maps": {
        Easy:   "What is a HashMap, how does it work internally, and what is its average time complexity for get and put?",
        Medium: "How would you use a HashMap to solve the Two Sum problem?",
        Hard:   "Explain hash collisions and how Java's HashMap resolves them. What happens when load factor is exceeded?",
    },
};

// Maps category label to categoryId used by the backend
const CATEGORY_ID_MAP = {
    "Arrays & Strings": 1,
    "Two Pointers":     2,
    "Hash Maps":        3,
};

const DIFFICULTIES = ["Easy", "Medium", "Hard"];

export default function ChatbotPage({ goBack }) {
    const { user } = useUser();

    const [category, setCategory]     = useState("Arrays & Strings");
    const [difficulty, setDifficulty] = useState("Easy");
    const [answer, setAnswer]         = useState("");
    const [result, setResult]         = useState(null); // { score, rating, feedback }
    const [loading, setLoading]       = useState(false);
    const [error, setError]           = useState(null);

    const currentQuestion = QUESTION_BANK[category]?.[difficulty] ?? "No question available.";

    const handleSubmit = async () => {
        if (!answer.trim()) return;
        setLoading(true);
        setError(null);
        setResult(null);

        try {
            const res = await fetch("http://localhost:8080/api/v1/chatbot/evaluate", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    userId:     user?.userId,
                    categoryId: CATEGORY_ID_MAP[category] ?? 1,
                    question:   currentQuestion,
                    difficulty,
                    answer,
                }),
            });

            if (!res.ok) throw new Error(`Server error: ${res.status}`);
            const data = await res.json();
            setResult(data);
        } catch (err) {
            setError("Something went wrong. Make sure the backend is running.");
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleNextQuestion = () => {
        if (!result) return;

        // Adjust difficulty based on rating (CB4 logic)
        if (result.rating === "GREEN" && difficulty !== "Hard") {
            setDifficulty(DIFFICULTIES[DIFFICULTIES.indexOf(difficulty) + 1]);
        } else if (result.rating === "RED" && difficulty !== "Easy") {
            setDifficulty(DIFFICULTIES[DIFFICULTIES.indexOf(difficulty) - 1]);
        }
        // YELLOW: stay the same

        setAnswer("");
        setResult(null);
        setError(null);
    };

    const ratingClass = result
        ? { GREEN: "rating-green", YELLOW: "rating-yellow", RED: "rating-red" }[result.rating]
        : "";

    return (
        <main className="chatbot-shell">
            <div className="chatbot-card">

                {/* Top bar */}
                <div className="chatbot-topbar">
                    <h1>SmartPrep</h1>
                    <span className="chatbot-badge">AI Interview</span>
                </div>

                {/* Category + Difficulty selectors */}
                <div className="chatbot-selectors">
                    <div className="selector-group">
                        <label>Category</label>
                        <div className="selector-pills">
                            {Object.keys(QUESTION_BANK).map((cat) => (
                                <button
                                    key={cat}
                                    type="button"
                                    className={`pill ${category === cat ? "pill-active" : ""}`}
                                    onClick={() => { setCategory(cat); setResult(null); setAnswer(""); }}
                                >
                                    {cat}
                                </button>
                            ))}
                        </div>
                    </div>

                    <div className="selector-group">
                        <label>Difficulty</label>
                        <div className="selector-pills">
                            {DIFFICULTIES.map((d) => (
                                <button
                                    key={d}
                                    type="button"
                                    className={`pill pill-diff-${d.toLowerCase()} ${difficulty === d ? "pill-active" : ""}`}
                                    onClick={() => { setDifficulty(d); setResult(null); setAnswer(""); }}
                                >
                                    {d}
                                </button>
                            ))}
                        </div>
                    </div>
                </div>

                {/* Question */}
                <div className="chatbot-question">
                    <span className="question-label">Question</span>
                    <p>{currentQuestion}</p>
                </div>

                {/* Answer area - hidden after result */}
                {!result && (
                    <>
            <textarea
                className="chatbot-textarea"
                placeholder="Type your answer here..."
                value={answer}
                onChange={(e) => setAnswer(e.target.value)}
                rows={7}
            />
                        <button
                            type="button"
                            className="chatbot-submit-btn"
                            onClick={handleSubmit}
                            disabled={loading || !answer.trim()}
                        >
                            {loading ? "Evaluating..." : "Submit Answer"}
                        </button>
                    </>
                )}

                {/* Error */}
                {error && <p className="chatbot-error">{error}</p>}

                {/* Result card */}
                {result && (
                    <div className={`result-card ${ratingClass}`}>
                        <div className="result-top">
                            <div className="result-score">{result.score}<span>/100</span></div>
                            <div className={`result-rating-badge ${ratingClass}`}>{result.rating}</div>
                        </div>

                        <p className="result-feedback">{result.feedback}</p>

                        <div className="result-hint">
                            {result.rating === "GREEN"  && difficulty !== "Hard"  && "Nice work - difficulty going up!"}
                            {result.rating === "RED"    && difficulty !== "Easy"  && "Keep practicing - difficulty going down."}
                            {result.rating === "YELLOW" && "Good effort - same difficulty next round."}
                            {result.rating === "GREEN"  && difficulty === "Hard"  && "Outstanding - you're at max difficulty!"}
                            {result.rating === "RED"    && difficulty === "Easy"  && "Keep at it - review the fundamentals."}
                        </div>

                        <div className="result-actions">
                            <button type="button" className="chatbot-submit-btn" onClick={handleNextQuestion}>
                                Next Question
                            </button>
                            <button type="button" className="chatbot-back-btn" onClick={goBack}>
                                Back to Menu
                            </button>
                        </div>
                    </div>
                )}

                {/* Back button (before submit) */}
                {!result && (
                    <button type="button" className="chatbot-back-btn" onClick={goBack}>
                        Back
                    </button>
                )}

            </div>
        </main>
    );
}
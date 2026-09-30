import { useEffect, useState } from "react";
import { useUser } from "./UserContext";
import "./SubmissionHistory.css";

export default function SubmissionHistory({ goBack }) {
  const { user } = useUser();
  const [submissions, setSubmissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;
    fetch(`http://localhost:8080/api/v1/solution/submissions?userId=${encodeURIComponent(user?.userId || "")}`)
      .then((response) => {
        if (!response.ok) throw new Error("Could not load submissions.");
        return response.json();
      })
      .then((data) => { if (active) setSubmissions(data); })
      .catch(() => { if (active) setError("Could not load submissions. Please try again."); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, [user?.userId]);

  return (
    <main className="history-shell">
      <section className="history-card">
        <p className="history-eyebrow">History</p>
        <h1>Previous Submissions</h1>
        <p className="history-intro">Your coding attempts, newest first.</p>

        {loading && <p className="history-state">Loading submissions...</p>}
        {!loading && error && <p className="history-state history-error" role="alert">{error}</p>}
        {!loading && !error && submissions.length === 0 && (
          <p className="history-state">No coding submissions yet.</p>
        )}

        {!loading && !error && submissions.length > 0 && (
          <div className="history-list">
            {submissions.map((submission) => {
              const grade = String(submission.rating || "").toLowerCase();
              return (
                <article className="history-entry" key={submission.submissionId}>
                  <div className="history-entry-top">
                    <h2>{submission.problemTitle}</h2>
                    <span className={`history-grade ${grade}`}>{submission.rating}</span>
                  </div>
                  <time className="history-date" dateTime={submission.submittedAt}>
                    {new Date(submission.submittedAt).toLocaleString()}
                  </time>
                  <details className="history-code">
                    <summary>View submitted code</summary>
                    <pre><code>{submission.answer}</code></pre>
                  </details>
                </article>
              );
            })}
          </div>
        )}

        <button className="history-back" type="button" onClick={goBack}>Back to Problems</button>
      </section>
    </main>
  );
}

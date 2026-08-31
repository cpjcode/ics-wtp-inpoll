import {useNavigate} from "react-router";
import {useEffect, useState} from "react";
import * as InPollApi from "../api/InPollApi.js";

export default function ResultsPage() {
    const [polls, setPolls] = useState([]);
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        InPollApi.getMyPolls()
            .then(result => {
                setPolls(result.filter(p => p.pollStatus === "FINISHED"));
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    if (loading) {
        return (
            <div className="container mt-3">
                <p>Loading finished polls...</p>
            </div>
        );
    }

    return (
        <div className="container mt-3">

            <h2>Finished Polls</h2>

            {polls.map(p => (
                <div key={p.pollId} className="card mb-2">
                    <div className="card-body">

                        <h5>{p.title}</h5>

                        <p>Status: FINISHED</p>

                        <button
                            className="btn btn-primary me-2"
                            onClick={() => navigate(`${p.pollId}`)}
                        >
                            Aggregate Results
                        </button>

                        <button
                            className="btn btn-secondary"
                            onClick={() => navigate(`${p.pollId}/answers`)}
                        >
                            View Answers
                        </button>

                    </div>
                </div>
            ))}

            {polls.length === 0 && (
                <p><em>No finished polls available.</em></p>
            )}

        </div>
    );
}
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import * as InPollApi from "../api/InPollApi.js";

export default function PollDetailPage() {
    const {id} = useParams();

    const [poll, setPoll] = useState(null);

    useEffect(() => {
        InPollApi.getPoll(id).then((result) => {
            setPoll(result);
        });
    }, [id]);

    if (!poll) {
        return (
            <div className="container mt-3">
                <p>Loading poll...</p>
            </div>
        );
    }

    return (
        <div className="container mt-3">

            <h2>Poll: {poll.title}</h2>
            <p>
                <strong>Description:</strong> {poll.description}
            </p>

            <p>
                <strong>Due date:</strong>{" "}
                {new Date(poll.dueDate).toLocaleString("de-DE", {
                    day: "2-digit",
                    month: "2-digit",
                    year: "numeric",
                    hour: "2-digit",
                    minute: "2-digit"
                })}
            </p>

            <p>
                <strong>Status:</strong> {poll.pollStatus}
            </p>

            <hr/>

            <h3>Questions</h3>

            {poll.questions && poll.questions.length > 0 ? (
                poll.questions.map((q, index) => (
                    <div key={q.id} className="card mb-3">
                        <div className="card-body">

                            <p>
                                Question {index + 1}
                            </p>

                            <p className="card-text">
                                {q.question}
                            </p>

                        </div>
                    </div>
                ))
            ) : (
                <p><em>No questions available.</em></p>
            )}

        </div>
    );
}
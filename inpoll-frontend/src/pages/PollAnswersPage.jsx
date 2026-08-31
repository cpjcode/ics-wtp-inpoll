import { useEffect, useState } from "react";
import { useParams } from "react-router";
import * as InPollApi from "../api/InPollApi.js";

export default function PollAnswersPage() {

    const { id } = useParams();

    const [data, setData] = useState([]);
    const [questionMap, setQuestionMap] = useState({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        Promise.all([
            InPollApi.getAnswers(id),
            InPollApi.getPoll(id)
        ])
            .then(([answersResult, pollResult]) => {

                setData(answersResult || []);

                const map = {};

                pollResult.questions.forEach((q, index) => {
                    map[q.id] = `Q${index + 1}: ${q.question}`;
                });

                setQuestionMap(map);

            })
            .finally(() => {
                setLoading(false);
            });

    }, [id]);

    if (loading) {
        return (
            <div className="container mt-3">
                <p>Loading answers...</p>
            </div>
        );
    }

    return (
        <div className="container mt-3">

            <h2>Individual Answers</h2>

            {data.length > 0 ? (
                <>
                    <div className="card mb-3">
                        <div className="card-body">

                            <h5>Questions</h5>

                            <ul className="mb-0">
                                {Object.values(questionMap).map(question => (
                                    <li key={question}>
                                        {question}
                                    </li>
                                ))}
                            </ul>

                        </div>
                    </div>

                    {data.map(participation => (
                        <div key={participation.username} className="card mb-3">
                            <div className="card-body">

                                <h5>Username: {participation.username}</h5>

                                <ul className="mb-0">
                                    {participation.answers.map((answer, index) => (
                                        <li key={answer.id}>
                                            <strong>A{index + 1}: </strong>
                                            {answer.text === "true"
                                                ? "Yes"
                                                : answer.text === "false"
                                                    ? "No"
                                                    : answer.text}
                                        </li>
                                    ))}
                                </ul>

                            </div>
                        </div>
                    ))}
                </>
            ) : (
                <p>No answers available.</p>
            )}

        </div>
    );
}
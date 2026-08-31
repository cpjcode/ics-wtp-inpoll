import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router";
import * as InPollApi from "../api/InPollApi.js";
import AnswerInput from "../components/AnswerInput.jsx";

export default function ParticipatePage() {

    const {id} = useParams();
    const navigate = useNavigate();

    const [poll, setPoll] = useState({
        questions: []
    });
    const [error, setError] = useState(null);

    const [answers, setAnswers] = useState({});

    useEffect(() => {
        InPollApi.getPoll(id).then(result => {
            setPoll(result);
        });
    }, [id]);

    function updateAnswer(questionId, value) {
        setAnswers(prev => ({
            ...prev,
            [questionId]: value
        }));
    }

    function validateAnswers() {
        for (const q of poll.questions) {
            const a = answers[q.id];

            if (a === undefined || a === null || a === "") {
                return "All questions must be answered";
            }
        }

        return null;
    }

    async function submitAnswers() {

        const validationError = validateAnswers();

        if (validationError) {
            setError(validationError);
            return;
        }

        setError(null);

        const payload = poll.questions.map(q => ({
            questionId: q.id,
            text: answers[q.id] ?? ""
        }));

        await InPollApi.submitParticipation(id, payload);

        navigate("/invitations");
    }

    return (
        <div className="container mt-3">

            <h2>{poll.title}</h2>
            <p>{poll.description}</p>

            {poll.questions.map(q => (
                <AnswerInput
                    key={q.id}
                    question={q}
                    answer={answers[q.id] ?? ""}
                    updateAnswer={updateAnswer}
                />
            ))}

            {error && (
                <div className="alert alert-danger">
                    {error}
                </div>
            )}

            <button
                className="btn btn-primary w-100"
                onClick={submitAnswers}
            >
                Submit Answers
            </button>

        </div>
    );
}
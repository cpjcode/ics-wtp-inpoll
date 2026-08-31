import {useState} from "react";
import {useNavigate} from "react-router";
import * as InPollApi from "../api/InPollApi.js";
import QuestionEditor from "../components/QuestionEditor.jsx";

export default function CreatePollPage() {

    const navigate = useNavigate();
    const [error, setError] = useState(null);

    const [poll, setPoll] = useState({
        title: "",
        description: "",
        dueDate: "",
        questions: []
    });

    async function createPoll() {
        setError(null);
        const selectedDate = new Date(poll.dueDate);
        const now = new Date();

        if (!poll.title || poll.title.trim() === "") {
            setError("Poll title cannot be empty");
            return;
        }

        if (poll.questions.length === 0) {
            setError("You must add at least one question");
            return;
        }

        if (isNaN(selectedDate.getTime())) {
            setError("Invalid date format");
            return;
        }

        if (selectedDate < now) {
            setError("Due date cannot be in the past");
            return;
        }

        for (const q of poll.questions) {
            if (!q.question || q.question.trim() === "") {
                setError("Question text cannot be empty");
                return;
            }
        }

        const newPoll = {
            ...poll,
            dueDate: selectedDate.toISOString()
        };

        await InPollApi.createPoll(newPoll);

        navigate("/polls");
    }

    function updateField(field, value) {
        setPoll({
            ...poll,
            [field]: value
        });
    }

    function addQuestion() {
        setPoll({
            ...poll,
            questions: [
                ...poll.questions,
                {
                    question: "",
                    type: "TEXT"
                }
            ]
        });
    }

    function updateQuestion(index, field, value) {
        const updatedQuestions = [...poll.questions];

        updatedQuestions[index] = {
            ...updatedQuestions[index],
            [field]: value
        };

        setPoll({
            ...poll,
            questions: updatedQuestions
        });
    }

    function removeQuestion(index) {
        setPoll({
            ...poll,
            questions: poll.questions.filter((_, i) => i !== index)
        });
    }

    return (
        <div className="container mt-3">

            <h2>Create Poll</h2>

            {/* title */}
            <input
                className="form-control mb-2"
                placeholder="Title"
                value={poll.title}
                onChange={e => updateField("title", e.target.value)}
            />

            {/* description */}
            <textarea
                className="form-control mb-2"
                placeholder="Description"
                value={poll.description}
                onChange={e => updateField("description", e.target.value)}
            />

            {/* dueDate */}
            <input
                type="datetime-local"
                className="form-control mb-3"
                value={poll.dueDate}
                onChange={e => updateField("dueDate", e.target.value)}
            />

            {/* questions */}
            {poll.questions.map((q, index) => (
                <QuestionEditor
                    key={index}
                    question={q}
                    index={index}
                    removeQuestion={removeQuestion}
                    updateQuestion={updateQuestion}
                />
            ))}

            <button className="btn btn-secondary mb-3" onClick={addQuestion}>
                Add Question
            </button>

            <button className="btn btn-primary w-100" onClick={createPoll}>
                Create Poll
            </button>

            {error && (
                <div className="alert alert-danger">
                    {error}
                </div>
            )}

        </div>
    );

}
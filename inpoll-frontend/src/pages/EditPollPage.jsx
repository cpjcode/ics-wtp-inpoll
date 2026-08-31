import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router";
import * as InPollApi from "../api/InPollApi.js";

export default function EditPollPage() {

    const {id} = useParams();
    const navigate = useNavigate();

    const [error, setError] = useState(null);

    const [poll, setPoll] = useState({
        title: "",
        description: "",
        dueDate: ""
    });

    useEffect(() => {
        InPollApi.getPoll(id).then(result => {
            setPoll({
                title: result.title,
                description: result.description,
                dueDate: result.dueDate
                ? new Date(result.dueDate).toISOString().slice(0, 16)
                : ""
            });

        });
    }, [id]);

    function updateField(field, value) {
        setPoll({
            ...poll,
            [field]: value
        });
    }

    async function saveChanges() {
        setError(null);

        const selectedDate = new Date(poll.dueDate);
        const now = new Date();

        if (!poll.title || poll.title.trim() === "") {
            setError("Poll title cannot be empty");
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

        const updatedPoll = {
            ...poll,
            dueDate: selectedDate.toISOString()
        };

        await InPollApi.updatePoll(id, updatedPoll);
        navigate("/polls");
    }

    return (
        <div className="container mt-3">

            <h2>Edit Poll</h2>

            {error && (
                <div className="alert alert-danger">
                    {error}
                </div>
            )}

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

            <button
                className="btn btn-primary w-100"
                onClick={saveChanges}
            >
                Save Changes
            </button>

        </div>
    );
}
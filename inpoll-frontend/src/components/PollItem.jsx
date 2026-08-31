import {useAuthContext} from "../context/AuthContextProvider.jsx";
import {useState} from "react";

export default function PollItem({poll, onDelete, onFinish, onView, onEdit, onInvite}) {
    const { auth } = useAuthContext();
    const [inviteOpen, setInviteOpen] = useState(false);
    const [inviteName, setInviteName] = useState("");

    const isCreator = auth.name === poll.username;

    return (
        <div className="card mb-3">
            <div className="card-body">

                <h5 className="card-title">
                    {poll.title}
                </h5>

                <p>
                    Status: <strong>{poll.pollStatus}</strong>
                </p>

                <div className="row g-2">

                    <div className="col">
                        <button
                            className="btn btn-outline-secondary w-100"
                            onClick={() => onView(poll.pollId)}
                        >
                            View
                        </button>
                    </div>

                    {isCreator && poll.pollStatus === "ACTIVE" && (
                        <>
                            <div className="col">
                                <button className="btn btn-warning w-100"
                                        onClick={() => onEdit(poll.pollId)}>
                                    Edit
                                </button>
                            </div>

                            <div className="col">
                                <button className="btn btn-warning w-100"
                                        onClick={() => onFinish(poll.pollId)}>
                                    Finish
                                </button>

                                {inviteOpen && (
                                    <div className="mt-2 d-flex gap-2">
                                        <input
                                            className="form-control"
                                            placeholder="Username"
                                            value={inviteName}
                                            onChange={(e) => setInviteName(e.target.value)}
                                        />

                                        <button
                                            className="btn btn-success"
                                            onClick={() => {
                                                onInvite(poll.pollId, inviteName);

                                                setInviteName("");
                                            }}
                                        >
                                            Send
                                        </button>
                                    </div>
                                )}
                            </div>

                            <div className="col">
                                <button
                                    className="btn btn-info w-100"
                                    onClick={() => {
                                        setInviteOpen(!inviteOpen);
                                        setInviteName("");
                                    }}>
                                    Invite
                                </button>
                            </div>

                            <div className="col">
                                <button className="btn btn-danger w-100"
                                        onClick={() => onDelete(poll.pollId)}>
                                    Delete
                                </button>
                            </div>
                        </>
                    )}

                </div>
            </div>
        </div>
    );
}
import {useEffect, useState} from "react";
import * as InPollApi from "../api/InPollApi.js";
import {useNavigate} from "react-router";

export default function InvitationsPage() {

    const [polls, setPolls] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        InPollApi.getPendingPolls()
            .then(result => {
                setPolls(result);
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    function openPoll(id) {
        navigate("poll/" + id);
    }

    async function acceptInvite(id) {
        await InPollApi.participateInPoll(id);
        navigate("/participate/" + id);
    }

    if (loading) {
        return (
            <div className="container mt-3">
                <p>Loading invitations...</p>
            </div>
        );
    }

    return (
        <div className="container mt-3">

            <h2>Invitations</h2>

            {polls.map(p => (
                <div key={p.pollId} className="card mb-2">
                    <div className="card-body">

                        <h5>{p.title}</h5>

                        <p>Status: {p.pollStatus}</p>

                        <div className="d-flex gap-2">

                            <button
                                className="btn btn-outline-secondary"
                                onClick={() => openPoll(p.pollId)}
                            >
                                View
                            </button>

                            <button
                                className="btn btn-success"
                                onClick={() => acceptInvite(p.pollId)}
                            >
                                Participate
                            </button>

                        </div>

                    </div>
                </div>
            ))}

            {polls.length === 0 && (
                <p><em>No pending invitations.</em></p>
            )}

        </div>
    );
}
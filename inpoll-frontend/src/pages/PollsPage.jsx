import {useAuthContext} from "../context/AuthContextProvider.jsx";
import {useEffect, useState} from "react";
import * as InPollApi from "../api/InPollApi.js";
import PollItem from "../components/PollItem.jsx";
import {useNavigate} from "react-router";

export default function PollsPage() {
    const {auth} = useAuthContext();
    const navigate = useNavigate();

    const [polls, setPolls] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    useEffect(() => {
        InPollApi.getMyPolls().then(result => {
            setPolls(result.filter(p => p.pollStatus === "ACTIVE"));
        }).finally(() => {
            setLoading(false);
        });
    }, [auth]);

    function viewPoll(id) {
        navigate("poll/" + id);
    }

    function editPoll(id) {
        navigate("poll/" + id + "/edit");
    }

    async function inviteUser(pollId, username) {
        setError(null);
        setSuccess(null);

        if (!username || username.trim() === "") {
            setError("Username cannot be empty");
            return;
        }

        if (username === auth.name) {
            setError("You cannot invite yourself");
            return;
        }

        await InPollApi.inviteToPoll(pollId, username);
        setSuccess("The invitation was sent.");

        setTimeout(() => {
            setSuccess(null);
        }, 2000);

    }

    async function finishPoll(id) {
        await InPollApi.finishPoll(id);
        setPolls(prev =>
            prev.filter(p => p.pollId !== id));
    }

    async function deletePoll(id) {
        await InPollApi.deletePoll(id);
        setPolls(prev => prev.filter(p => p.pollId !== id));
    }

    if (loading) {
        return <div className="container mt-3">Loading polls...</div>;
    }

    return (
        <div className="row mt-2">

            {polls.map(p =>
                <PollItem
                    key={p.pollId}
                    poll={p}
                    onDelete={deletePoll}
                    onFinish={finishPoll}
                    onView={viewPoll}
                    onEdit={editPoll}
                    onInvite={inviteUser}
                />
            )}

            {error && (
                <div className="alert alert-danger">
                    {error}
                </div>
            )}

            {success && (
                <div className="alert alert-success">
                    {success}
                </div>
            )}

            {polls.length === 0 && (
                <p><em>No available polls found.</em></p>
            )}

        </div>
    );

}
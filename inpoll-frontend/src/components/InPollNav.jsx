import {useAuthContext} from "../context/AuthContextProvider.jsx";
import {NavLink, useNavigate} from "react-router";

export default function InPollNav() {
    const {auth, setAuth} = useAuthContext();
    const navigate = useNavigate();

    function logout() {
        setAuth({
            name: "",
            loggedIn: false
        });

        navigate("/login");
    }

    return (
        <nav className="navbar navbar-dark navbar-expand-sm pt-2 pb-2">
            <span className="navbar-brand">
                <strong>InPoll</strong>
            </span>

            <div id="mainnav" className="navbar-collapse collapse">
                <div className="navbar-nav ms-auto text-end">

                    {auth.loggedIn && (
                        <>
                            <NavLink className="nav-link" to="polls">
                                Polls
                            </NavLink>

                            <NavLink className="nav-link" to="results">
                                Results
                            </NavLink>

                            <NavLink className="nav-link" to="invitations">
                                Invitations
                            </NavLink>

                            <NavLink className="nav-link" to="create">
                                Create Poll
                            </NavLink>

                            <button
                                className="nav-link btn btn-link text-white"
                                onClick={logout}
                            >
                                Logout
                            </button>
                        </>
                    )}

                    {auth.loggedIn && (
                        <p><em> Logged in as {auth.name}</em></p>
                    )}

                </div>
            </div>
        </nav>
    );
}
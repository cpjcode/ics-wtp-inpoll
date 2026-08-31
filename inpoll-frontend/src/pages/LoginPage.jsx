import {useAuthContext} from "../context/AuthContextProvider.jsx";
import {useEffect, useRef, useState} from "react";
import * as InPollApi from "../api/InPollApi.js";
import {useNavigate} from "react-router";

export default function LoginPage() {
    const {auth, setAuth} = useAuthContext();
    const [createAccount, setCreateAccount] = useState(false);
    const [error, setError] = useState(null);

    const navigate = useNavigate();

    const username = useRef(undefined);
    const password = useRef(undefined);

    useEffect(() => {
        if (auth.loggedIn) {
            navigate("/polls");
        }
    }, [auth.loggedIn, navigate]);

    async function logIn() {
        if (!username.current.value || !password.current.value) {
            setError("Username and password cannot be empty");
            return;
        }

        const credentials = {
            name: username.current.value,
            password: password.current.value
        };

        await InPollApi.logIn(credentials);

        setAuth({
            name: credentials.name,
            password: credentials.password,
            loggedIn: true
        });
    }

    async function register() {
        if (!username.current.value || !password.current.value) {
            setError("Username and password cannot be empty");
            return;
        }

        const newUser = {username: username.current.value, password: password.current.value};

        await InPollApi.registerUser(newUser);

        setAuth({
            name: newUser.username,
            password: newUser.password,
            loggedIn: true
        });
    }


        return <>
            <p><em>Currently not logged in.</em></p>

            <div className="form-check mb-sm-3">

                <input
                    id="new-account"
                    className="form-check-input"
                    type="checkbox"
                    checked={createAccount}
                    onChange={e => setCreateAccount(e.target.checked)}
                />

                <label
                    className="form-check-label"
                    htmlFor="new-account"
                >
                    I want to create a new account.
                </label>

            </div>

            <div className="row mb-sm-3">

                <label
                    className="col-sm-3 col-form-label text-end"
                    htmlFor="username"
                >
                    Username:
                </label>

                <div className="col-sm-9">
                    <input
                        id="username"
                        className="form-control"
                        placeholder="Your username"
                        ref={username}
                    />
                </div>

            </div>

            <div className="row mb-sm-3">

                <label
                    className="col-sm-3 col-form-label text-end"
                    htmlFor="password"
                >
                    Password:
                </label>

                <div className="col-sm-9">
                    <input
                        type="password"
                        id="password"
                        className="form-control"
                        placeholder="***"
                        ref={password}
                    />
                </div>

            </div>

            {
                createAccount
                    ? (
                        <button
                            className="btn btn-primary col-12"
                            onClick={register}
                        >
                            Register
                        </button>
                    )
                    : (
                        <button
                            className="btn btn-primary col-12"
                            onClick={logIn}
                        >
                            Log in
                        </button>
                    )
            }

            {error && (
                <div className="alert alert-danger">
                    {error}
                </div>
            )}

        </>;

}
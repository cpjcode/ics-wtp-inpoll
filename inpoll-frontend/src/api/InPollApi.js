import {currentAuth} from "../context/AuthContextProvider.jsx";

const ROOT = window.location.protocol + '//' + window.location.hostname + ":8080";

let errorHandler = null;

export function registerErrorHandler(handler) {
    errorHandler = handler;
}

function json() {
    return {"Content-Type": "application/json"}
}

function authorized() {
    return authorizedAs(currentAuth());
}

function authorizedAs(auth) {
    return {"Authorization": "Basic " + btoa(auth.name + ":" + auth.password)};
}

function authorizedJson() {
    return {...authorized(), ...json()};
}

async function apiFetch(url, options) {
    try {
        const response = await fetch(url, options);

        if (!response.ok) {
            await extractError(response);
        }

        const contentType = response.headers.get("content-type");

        if (contentType && contentType.includes("application/json")) {
            return await response.json();
        }

        return null;
    } catch (e) {
        console.error(e);

        if (errorHandler) {
            throw errorHandler(e.message);
        }

        throw e;
    }
}

async function extractError(response) {
    let message = response.status + " " + response.message;

    try {
        const serverError = await response.json().catch(() => null);
        message = serverError?.message || (response.status + " Error");
    } catch { //
    }

    throw new Error(message);
}

export async function registerUser(userDto) {
    return apiFetch(ROOT + "/users",
        {method: "POST", headers: json(), body: JSON.stringify(userDto)});
}

export async function logIn(credentials) {
    return apiFetch(ROOT + "/users/current", {
        headers: authorizedAs(credentials)
    });
}

export async function getMyPolls() {
    return apiFetch(ROOT + "/polls", {
        headers: authorized()
    });
}

export async function createPoll(dto) {
    return apiFetch(ROOT + "/polls", {
        method: "POST",
        headers: authorizedJson(),
        body: JSON.stringify(dto)
    });
}

export async function updatePoll(id, dto) {
    return apiFetch(ROOT + "/polls/" + id, {
        method: "PUT",
        headers: authorizedJson(),
        body: JSON.stringify(dto)
    });
}

export async function inviteToPoll(id, username) {
    return apiFetch(
        ROOT + "/polls/" + id + "/invite?username=" + username,
        {
            method: "POST",
            headers: authorized(),
            body: JSON.stringify({ username })
        }
    );
}

export async function finishPoll(id) {
    return apiFetch(ROOT + "/polls/" + id + "/finish", {
        method: "POST",
        headers: authorized()
    });
}

export async function getPoll(id) {
    return apiFetch(ROOT + "/polls/" + id, {
        headers: authorized()
    });
}

export async function getPendingPolls() {
    return apiFetch(ROOT + "/polls/pending", {
        headers: authorized()
    });
}

export async function getAnswers(id) {
    return apiFetch(ROOT + "/polls/" + id + "/answers", {
        headers: authorized()
    });
}

export async function getAggregate(id) {
    return apiFetch(ROOT + "/polls/" + id + "/aggregate", {
        headers: authorized()
    });
}

export async function deletePoll(id) {
    return apiFetch(ROOT + "/polls/" + id, {
        method: "DELETE",
        headers: authorized()
    });
}

export async function participateInPoll(pollId) {
    return apiFetch(ROOT + "/polls/" + pollId + "/participate", {
        method: "POST",
        headers: authorized()
    });
}

export async function submitParticipation(pollId, answers) {
    return apiFetch(ROOT + "/polls/" + pollId + "/submit", {
        method: "POST",
        headers: authorizedJson(),
        body: JSON.stringify(answers)
    });
}

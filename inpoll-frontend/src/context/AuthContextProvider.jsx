import {createContext, useContext, useState} from "react";

const AuthContext = createContext();

// eslint-disable-next-line react-refresh/only-export-components
export const loggedOut = {name: null, password: null, loggedIn: false}

let current = loggedOut;

// eslint-disable-next-line react-refresh/only-export-components
export function currentAuth() {
    return current;
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuthContext() {
    return useContext(AuthContext);
}

export default function AuthContextProvider({children}) {
    const [auth, internalSetAuth] = useState(loggedOut);

    function setAuth(newAuth) {
        current = newAuth;
        internalSetAuth(newAuth);
    }

    return <AuthContext value={{auth, setAuth}}>
        {children}
    </AuthContext>
}
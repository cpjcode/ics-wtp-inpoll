import {createContext, useContext, useEffect, useRef, useState} from "react";
import {registerErrorHandler} from "../api/InPollApi.js";

const ErrorContext = createContext({visible: false, errorMessage: ""});

// eslint-disable-next-line react-refresh/only-export-components
export function useErrorContext() {
    return useContext(ErrorContext);
}

export default function ErrorContextProvider({children}) {
    const [visible, setVisible] = useState(false);
    const [errorMessage, setErrorMessage] = useState(undefined);

    const timeoutRef = useRef(null);

    function showErrorMessage(message) {
        setErrorMessage(message);
        setVisible(true);

        if (timeoutRef.current) clearTimeout(timeoutRef.current);

        timeoutRef.current = setTimeout(() => {
            setVisible(false);
        }, 8000);
    }

    useEffect(() => {
        registerErrorHandler(showErrorMessage);
    }, []);

    return (
        <ErrorContext.Provider value={{visible, errorMessage}}>
            {children}
        </ErrorContext.Provider>
    );
}
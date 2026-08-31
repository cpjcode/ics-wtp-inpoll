import {useErrorContext} from "../context/ErrorContextProvider.jsx";

export default function ErrorToastContainer() {
    const {visible, errorMessage} = useErrorContext();

    if (visible) {
        return (
            <div className="toast-container position-absolute bottom-0 end-0 mb-4 me-4">
                <div className="toast show" role="alert">
                    <div className="toast-body bg-danger bg-opacity-25">
                        {errorMessage}
                    </div>
                </div>
            </div>
        );
    }

    return <></>;
}
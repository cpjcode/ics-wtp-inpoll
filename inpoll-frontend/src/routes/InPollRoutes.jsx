import {Route, Routes} from "react-router";
import LoginPage from "../pages/LoginPage.jsx";
import CreatePollPage from "../pages/CreatePollPage.jsx";
import PollDetailPage from "../pages/PollDetailPage.jsx";
import ParticipatePage from "../pages/ParticipatePage.jsx";
import AggregateResultsPage from "../pages/AggregateResultsPage.jsx";
import {useAuthContext} from "../context/AuthContextProvider.jsx";
import EditPollPage from "../pages/EditPollPage.jsx";
import PollsPage from "../pages/PollsPage.jsx";
import ResultsPage from "../pages/ResultsPage.jsx";
import InvitationsPage from "../pages/InvitationsPage.jsx";
import PollAnswersPage from "../pages/PollAnswersPage.jsx";

export default function InPollRoutes() {
    const { auth } = useAuthContext();

    if (!auth.loggedIn) {
        return <LoginPage/>;
    }

    return (
        <Routes>
            <Route path={"polls"} element={<PollsPage/>}/>
            <Route path={"create"} element={<CreatePollPage/>}/>
            <Route path={"polls/poll/:id"} element={<PollDetailPage/>}/>
            <Route path={"invitations/poll/:id"} element={<PollDetailPage/>}/>
            <Route path={"polls/poll/:id/edit"} element={<EditPollPage/>}/>
            <Route path={"results"} element={<ResultsPage/>}/>
            <Route path={"participate/:id"} element={<ParticipatePage/>}/>
            <Route path={"results/:id"} element={<AggregateResultsPage/>}/>
            <Route path={"results/:id/answers"} element={<PollAnswersPage/>} />
            <Route path={"invitations"} element={<InvitationsPage/>}/>
            <Route path={"login"} element={<LoginPage/>}/>
        </Routes>
    );
}
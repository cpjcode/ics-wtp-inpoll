import InPollRoutes from "./routes/InPollRoutes.jsx";
import InPollNav from "./components/InPollNav.jsx";
import ErrorToastContainer from "./components/ErrorToastContainer.jsx";

export default function App() {

  return (
      <>
        <div className="bg-primary">
          <header className="container pt-3 pb-4">
            <InPollNav/>
          </header>
        </div>

        <div className="bg-white">
            <main className="pt-3 pb-4">
                <InPollRoutes/>
                <ErrorToastContainer/>
            </main>
        </div>

          <div className="border-top">
              <footer className="container pt-2">
                  <p className="text-muted small">
                      Summer 2026, Web Technology Project (ICS), OTH Regensburg
                      <br/>
                      Created with Spring boot, React, Bootstrap, MariaDB, and Docker. <br/>
                      Project repository: <a className="text-muted" href="https://github.com/cpjcode/ics-wtp-inpoll">InPoll GitHub</a>
                  </p>
              </footer>
          </div>
      </>
  );
}
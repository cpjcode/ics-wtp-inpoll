import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import AuthContextProvider from "./context/AuthContextProvider.jsx";
import ErrorContextProvider from "./context/ErrorContextProvider.jsx";
import {BrowserRouter} from "react-router";

createRoot(document.getElementById('root')).render(
  <StrictMode>
      <AuthContextProvider>
          <ErrorContextProvider>
              <BrowserRouter>
                  <App/>
              </BrowserRouter>
          </ErrorContextProvider>
      </AuthContextProvider>
  </StrictMode>);

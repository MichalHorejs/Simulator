import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import App from "./App.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import HomePage from "./pages/HomePage.tsx";
import {AuthProvider} from "./context/LoginContext.tsx";
import RegisterPage from "./pages/RegisterPage.tsx";
import AboutPage from "./pages/AboutPage.tsx";
import SimulatorPage from "./pages/SimulatorPage.tsx";
import LeaderboardsPage from "./pages/LeaderboardsPage.tsx";
import LeaderboardsDetailPage from "./pages/LeaderboardsDetailPage.tsx";

const router = createBrowserRouter([
    {
        path: '/',
        element: <App/>,
        children: [
            {
                path: '/',
                element: <HomePage/>,
            },
            {
                path: '/about',
                element: <AboutPage/>
            },
            {
                path: '/simulator',
                element: <SimulatorPage/>
            },
            {
                path: '/leaderboards',
                element: <LeaderboardsPage/>
            },
            {
                path: '/login',
                element: <LoginPage/>,
            },
            {
                path: '/register',
                element: <RegisterPage/>
            },
            {
                path: '/leaderboards/simulation/:simId',
                element: <LeaderboardsDetailPage/>
            }
        ]
    }
]);

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <AuthProvider>
            <RouterProvider router={router}/>
        </AuthProvider>
    </StrictMode>,
)

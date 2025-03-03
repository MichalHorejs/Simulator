import './App.css'
import NavBar from "./components/NavBar/NavBar.tsx";
import {Outlet} from "react-router-dom";

function App() {

    return (
        <div>
            <NavBar />
            <Outlet />
        </div>
    );
}

export default App

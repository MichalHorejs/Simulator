import './App.css'
import NavBar from "./components/navbar/NavBar.tsx";
import {Outlet} from "react-router-dom";
import 'leaflet/dist/leaflet.css'

function App() {

    return (
        <div>
            <NavBar />
            <Outlet />
        </div>
    );
}

export default App

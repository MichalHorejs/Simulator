import './App.css'
import {useEffect} from "react";
import {Env} from "./Env.ts";
import HomePage from "./pages/HomePage.tsx";

function App() {

    useEffect(() => {
        fetch(`${Env.API_BASE_URL}/ping`)
            .then((response) => response.text())
            .then((body) => console.log(body))
            .catch((error) => console.error(error));
    }, []);

    return (
        <>
            <HomePage />
        </>
    )
}

export default App

import Nav from 'react-bootstrap/Nav';
import 'bootstrap/dist/css/bootstrap.min.css';
import './NavBar.css'
import {Container, Navbar} from 'react-bootstrap';
import {useEffect} from "react";
import {Env} from "../../Env.ts";


function NavBar() {
    useEffect(() => {
        fetch(`${Env.API_BASE_URL}/login`)
            .then((response) => response.text())
            .then((body) => console.log(body))
            .catch((error) => console.error(error));
    }, []);

    return (
        <Navbar fixed="top" expand="lg" className="bg-body-tertiary" bg="dark" data-bs-theme="dark">
            <Container>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link href="#home" className="nav-link-spacing">Home</Nav.Link>
                        <Nav.Link href="#about" className="nav-link-spacing">About</Nav.Link>
                        <Nav.Link href="#simulator" className="nav-link-spacing">Simulator</Nav.Link>
                        <Nav.Link href="#leaderboards" className="nav-link-spacing">Leaderboards</Nav.Link>
                    </Nav>
                    <Nav>
                        <Nav.Link href="#signin">Sign In</Nav.Link>
                        <span className="navbar-text mx-2">/</span>
                        <Nav.Link href="#signup" className="rounded-border">Sign Up</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavBar;
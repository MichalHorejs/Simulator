import Nav from 'react-bootstrap/Nav';
import 'bootstrap/dist/css/bootstrap.min.css';
import './NavBar.css'
import {Container, Navbar} from 'react-bootstrap';
import {Link} from "react-router-dom";
import {useAuth} from "../../context/LoginContext.tsx";


function NavBar() {

    const { user, logout } = useAuth();

    return (
        <Navbar fixed="top" expand="lg" className="bg-body-tertiary" bg="dark" data-bs-theme="dark">
            <Container>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto first-nav">
                        <Nav.Link as={Link} to="/">Home</Nav.Link>
                        <Nav.Link as={Link} to="/about">About</Nav.Link>
                        <Nav.Link as={Link} to="/simulator">Simulator</Nav.Link>
                        <Nav.Link as={Link} to="/leaderboards" className="nav-link-spacing">Leaderboards</Nav.Link>
                    </Nav>
                    <Nav>
                        { user ? (
                            <>
                                <Nav.Item className="navbar-text mx-2">{user.toUpperCase()}</Nav.Item>
                                <span className="navbar-text mx-2">/</span>
                                <Nav.Link className="rounded-border" onClick={logout}>Logout</Nav.Link>
                            </>
                        ) : (
                            <>
                                <Nav.Link as={Link} to="/login">Sign In</Nav.Link>
                                <span className="navbar-text mx-2">/</span>
                                <Nav.Link as={Link} to="/register" className="rounded-border">Sign Up</Nav.Link>
                            </>
                        )}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavBar;
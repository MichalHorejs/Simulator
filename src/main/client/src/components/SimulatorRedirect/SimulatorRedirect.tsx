import {Button, Container} from "react-bootstrap";
import loginImage from "../../assets/exclamation_mark.png";
import './SimulatorRedirect.css'

function SimulatorRedirect() {

    return (
        <Container className="text-center mt-5 cont">
            <div className="image-text-container">
                <img src={loginImage} alt="Please log in" className="mb-4" />
                <h4>You need to log in or register to access this page!</h4>
            </div>
            <div className="d-flex justify-content-center gap-2">
                <Button href="/login" variant="outline-dark" size="lg">Log In</Button>
                <Button href="/register" variant="secondary" size="lg">Register</Button>
            </div>
        </Container>
    );
}

export default SimulatorRedirect;
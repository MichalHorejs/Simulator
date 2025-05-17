import {Button, Container} from "react-bootstrap";
import loginImage from "../../assets/exclamation_mark.png";
import './SimulatorRedirect.css'

function SimulatorRedirect() {

    return (
        <Container className="text-center mt-5 cont">
            <div className="image-text-container">
                <img src={loginImage} alt="Please log in" className="mb-4" />
                <h4>Pro povolení přístupu na tuto stránku se musíte nejprve přihlásit!</h4>
            </div>
            <div className="d-flex justify-content-center gap-2">
                <Button href="/login" variant="outline-dark" size="lg">Přihlášení</Button>
                <Button href="/register" variant="secondary" size="lg">Registrace</Button>
            </div>
        </Container>
    );
}

export default SimulatorRedirect;
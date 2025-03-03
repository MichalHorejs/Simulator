import 'bootstrap/dist/css/bootstrap.min.css';
import {Button, Col, Container, Form, Row} from 'react-bootstrap';
import '../index.css'
import {FormEvent, useState} from "react";
import {useAuth} from "../context/LoginContext.tsx";
import {useNavigate} from "react-router-dom";
// todo: handle expired tokens
function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const {login} = useAuth();
    const navigate = useNavigate();


    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        setError(null);
        try {
            await login(username, password);
            navigate('/');
        } catch (err) {
            setError("Wrong username or password");
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center" style={{height: '100vh'}}>
            <Container>
                <Row className="justify-content-md-center">
                    <Col md={6}>
                        <h2 className="text-center">Please log-in</h2><br/>
                        <Form onSubmit={handleSubmit}>
                            <Form.Group as={Row} controlId="formUsername" className="mb-3">
                                <Form.Label column sm={3}>Username</Form.Label>
                                <Col sm={9}>
                                    <Form.Control type="text"
                                                  placeholder="Enter username"
                                                  value={username}
                                                  onChange={(e) => setUsername(e.target.value)}
                                                  required/>
                                </Col>
                            </Form.Group>

                            <Form.Group as={Row} controlId="formPassword" className="mb-3">
                                <Form.Label column sm={3}>Password</Form.Label>
                                <Col sm={9}>
                                    <Form.Control
                                        type="password"
                                        placeholder="Enter password"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required/>
                                </Col>
                            </Form.Group>

                            {error && <p style={{color: 'red'}}>{error}</p>}

                            <Button variant="dark" type="submit" className="w-100">
                                Login
                            </Button>
                        </Form>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default LoginPage;
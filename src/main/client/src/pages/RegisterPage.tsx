import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Col, Container, Form, Row } from 'react-bootstrap';
import '../index.css';
import { FormEvent, useState } from "react";
import { useAuth } from "../context/LoginContext";
import { useNavigate } from "react-router-dom";
import { Env } from "../Env";

function RegisterPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const { authenticate } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        setError(null);

        if (password !== confirmPassword) {
            setError("Passwords do not match");
            return;
        }

        try {
            const response = await fetch(`${Env.API_BASE_URL}/auth/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Registration failed");
            }

            const data = await response.json();
            authenticate(data);
            navigate('/');
        } catch (err: any) {
            setError(err.message);
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
            <Container>
                <Row className="justify-content-md-center">
                    <Col md={6}>
                        <h2 className="text-center">Please register:</h2><br />
                        <Form onSubmit={handleSubmit}>
                            <Form.Group as={Row} controlId="formUsername" className="mb-3">
                                <Form.Label column sm={3}>Username</Form.Label>
                                <Col sm={9}>
                                    <Form.Control
                                        type="text"
                                        placeholder="Enter username"
                                        value={username}
                                        onChange={(e) => setUsername(e.target.value)}
                                        required
                                    />
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
                                        required
                                    />
                                </Col>
                            </Form.Group>

                            <Form.Group as={Row} controlId="formConfirmPassword" className="mb-3">
                                <Form.Label column sm={3}>Confirm Password</Form.Label>
                                <Col sm={9}>
                                    <Form.Control
                                        type="password"
                                        placeholder="Confirm password"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required
                                    />
                                </Col>
                            </Form.Group>

                            {error && <p style={{ color: 'red' }}>{error}</p>}

                            <Button variant="dark" type="submit" className="w-100">
                                Register
                            </Button>
                        </Form>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default RegisterPage;

package com.gina.simulator;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimulationApiTests extends AbstractIntegrationTest{

    private String simulationId;

    @Test
    @Order(1)
    public void testStartSimulation() {
        String payload = """
            {
                "difficulty": "EASY",
                "person": {
                    "username": "%s"
                }
            }
            """.formatted(registeredUsername);

        HttpEntity<String> request = new HttpEntity<>(payload, getAuthHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
                getRootUrl("simulation/start"),
                HttpMethod.POST,
                request,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        simulationId = response.getBody().get("id").toString();
    }

    @Test
    @Order(2)
    public void testFinishSimulation() {
        assertThat(simulationId).isNotNull();
        String payload = """
            {
                "id": "%s"
            }
            """.formatted(simulationId);

        HttpEntity<String> request = new HttpEntity<>(payload, getAuthHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
                getRootUrl("simulation/finish"),
                HttpMethod.POST,
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(3)
    public void testGetSimulationDetails() {
        assertThat(simulationId).isNotNull();
        HttpEntity<Void> request = new HttpEntity<>(getAuthHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                getRootUrl("simulation/" + simulationId + "/detail"),
                HttpMethod.GET,
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}

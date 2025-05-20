package com.gina.simulator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected String jwtToken;
    protected String registeredUsername;

    protected String getRootUrl(String path) {
        return "http://localhost:" + port + "/api/" + path;
    }

    @BeforeAll
    void registerAndStoreToken() {
        registeredUsername = "test_" + UUID.randomUUID();

        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("username", registeredUsername);
        registerRequest.put("password", "pass");

        String registerUrl = getRootUrl("auth/register");
        ResponseEntity<Map> registerResponse = restTemplate.postForEntity(registerUrl, registerRequest, Map.class);

        Assertions.assertEquals(HttpStatus.OK, registerResponse.getStatusCode(), "Registrace selhala");
        assert registerResponse.getBody() != null;
        jwtToken = (String) registerResponse.getBody().get("access_token");
        Assertions.assertNotNull(jwtToken, "JWT token nesmí být null");
    }

    protected HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);
        return headers;
    }
}

package com.gina.simulator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LeaderboardsApiTests extends AbstractIntegrationTest {

    @Test
    public void testGetLeaderboards() {
        String url = getRootUrl("leaderboards?difficulty=EASY&page=0&limit=10");
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(null),
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    /**
     * Anonymous user cannot access admin only endpoint.
     */
    @Test
    public void testdeleteLeaderBoardNotLogged() {
        String url = getRootUrl("leaderboards/12345");

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    /**
     * User cannot access admin only endpoint.
     */
    @Test
    public void testdeleteLeaderBoardLogged(){
        String url = getRootUrl("leaderboards/12345");

        HttpEntity<String> request = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}

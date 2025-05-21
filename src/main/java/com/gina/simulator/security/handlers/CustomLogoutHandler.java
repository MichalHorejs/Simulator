package com.gina.simulator.security.handlers;

import com.gina.simulator.token.Token;
import com.gina.simulator.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * Class responsible for deleting user from Spring context.
 */
@RequiredArgsConstructor
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);

        Token activeToken = tokenRepository.findByAccessToken(token)
                .orElse(null);

        if (activeToken != null) {
            activeToken.setLoggedOut(true);
            tokenRepository.save(activeToken);
        }
    }
}

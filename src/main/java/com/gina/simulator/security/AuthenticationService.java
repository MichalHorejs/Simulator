package com.gina.simulator.security;

import com.gina.simulator.exception.BadRequestException;
import com.gina.simulator.person.Person;
import com.gina.simulator.person.PersonRepository;
import com.gina.simulator.role.Role;
import com.gina.simulator.security.models.AuthenticationResponse;
import com.gina.simulator.token.Token;
import com.gina.simulator.token.TokenRepository;
import com.gina.simulator.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PersonRepository personRepository;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(Person request) {


        if (personRepository.existsByUsername(request.getUsername())){
            throw new BadRequestException("User '%s' already exist !".formatted(request.getUsername()));
        }

        Person person = new Person();
        person.setUsername(request.getUsername());
        person.setPassword(passwordEncoder.encode(request.getPassword()));
        person.setRole(Role.USER);

        personRepository.save(person);

        String accessToken = tokenService.generateAccessToken(person);
        String refreshToken = tokenService.generateRefreshToken(person);

        savePersonToken(accessToken, refreshToken, person);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse authenticate(Person request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Person person = personRepository.getPersonByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Person not found"));

        String accessToken = tokenService.generateAccessToken(person);
        String refreshToken = tokenService.generateRefreshToken(person);

        revokeAllTokens(person);
        savePersonToken(accessToken, refreshToken, person);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public ResponseEntity<?> refreshToken(
            HttpServletRequest request
    ){
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        String username = tokenService.extractUsername(token);

        Person person = personRepository.getPersonByUsername(username)
                .orElseThrow(() -> new BadRequestException("Person not found"));

        if (tokenService.isValidRefreshToken(token, person)){
            String accessToken = tokenService.generateAccessToken(person);
            String refreshToken = tokenService.generateRefreshToken(person);

            revokeAllTokens(person);
            savePersonToken(accessToken, refreshToken, person);

            return new ResponseEntity<>(new AuthenticationResponse(accessToken, refreshToken), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private void savePersonToken(String accessToken, String refreshToken, Person person) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setPerson(person);
        tokenRepository.save(token);
    }

    private void revokeAllTokens(Person person) {
        List<Token> validTokenList = tokenRepository.findAllAccessTokensByUser(person.getUsername());

        if(!validTokenList.isEmpty()){
            validTokenList.forEach(t -> t.setLoggedOut(true));
        }

        tokenRepository.saveAll(validTokenList);
    }
}

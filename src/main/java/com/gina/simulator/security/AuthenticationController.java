package com.gina.simulator.security;

import com.gina.simulator.person.Person;
import com.gina.simulator.security.models.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login (@RequestBody Person person){
        return ResponseEntity.ok(authService.authenticate(person));
    }

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody Person person){
        return ResponseEntity.ok(authService.register(person));
    }

    @PostMapping("refresh_token")
    public ResponseEntity<?> refreshToken (
            HttpServletRequest request
    ){
        return authService.refreshToken(request);
    }

}

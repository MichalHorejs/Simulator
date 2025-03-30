package com.gina.simulator.token;

import com.gina.simulator.person.Person;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Person person;

    private String accessToken;

    private String refreshToken;

    private boolean loggedOut;

}

package com.gina.simulator.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findByAccessToken(String token);

//    @Query("SELECT t FROM Token t WHERE t.refreshToken = :token")
    Optional<Token> findByRefreshToken(String token);

    @Query("SELECT t FROM Token t WHERE t.person.username = :username AND t.loggedOut = false")
    List<Token> findAllAccessTokensByUser(@Param("username") String username);
}

package com.gina.simulator.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {
    boolean existsByUsername(String username);
    Optional<Person> getPersonByUsername(String username);

    Optional<Person> findByUsername(String username);
}

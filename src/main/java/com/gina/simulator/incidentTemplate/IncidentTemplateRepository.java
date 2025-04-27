package com.gina.simulator.incidentTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IncidentTemplateRepository extends JpaRepository<IncidentTemplate, UUID> {

    @Query(value = "SELECT * FROM incident_template ORDER BY random() LIMIT 1", nativeQuery = true)
    Optional<IncidentTemplate> findRandomTemplate();
}

package com.gina.simulator.incidentTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IncidentTemplateRepository extends JpaRepository<IncidentTemplate, UUID> {

    @Query(value = "SELECT * FROM incident_template ORDER BY random() LIMIT 1", nativeQuery = true)
    Optional<IncidentTemplate> findRandomTemplate();

    @Query("SELECT DISTINCT it.address.district FROM IncidentTemplate it")
    List<String> findDistinctDistricts();

    @Query("SELECT DISTINCT it.address.municipality FROM IncidentTemplate it WHERE it.address.district = :districtName")
    List<String> findDistinctMunicipalities(@Param("districtName") String districtName);
}

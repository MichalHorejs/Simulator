package com.gina.simulator.simulation;

import com.gina.simulator.enums.VehicleType;
import com.gina.simulator.exception.EntityNotFoundException;
import com.gina.simulator.incident.Incident;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.leaderboard.LeaderboardService;
import com.gina.simulator.person.Person;
import com.gina.simulator.person.PersonRepository;
import com.gina.simulator.utils.Utils;
import com.gina.simulator.vehicle.Vehicle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final PersonRepository personRepository;
    private final SimulationRepository simulationRepository;
    private final LeaderboardService leaderboardService;

    @Transactional
    public Simulation startSimulation(Simulation simulation) {
        Person person = personRepository.findByUsername(simulation.getPerson().getUsername())
                .orElseThrow(() -> new EntityNotFoundException(Person.class, simulation.getPerson().getId()));

        simulation.setStartTime(LocalDateTime.now());
        simulation.setPerson(person);
        return simulationRepository.save(simulation);
    }

    @Transactional
    public Simulation finish(Simulation s) {
        Simulation simulation = simulationRepository.findById(s.getId())
                .orElseThrow(() -> new EntityNotFoundException(Simulation.class, s.getId()));
        simulation.setEndTime(LocalDateTime.now());

        leaderboardService.save(
                simulation.getPerson().getUsername(),
                simulation.getId(),
                computeRating(simulation),
                simulation.getDifficulty()
        );

        return simulationRepository.save(simulation);
    }

    private int computeRating(Simulation simulation){
        List<Incident> incidents = simulation.getIncidents();
        if (incidents.isEmpty()) {
            return 0;
        }

        double totalScore = 0.0;
        double maxScore = 0.0;

        for (Incident incident : incidents) {
            maxScore += 1800;

            if (incident.getCallPickedUpTime() == null || incident.getEndTime() == null) {
                continue;
            }

            IncidentTemplate template = incident.getIncidentTemplate();

            totalScore += incident.getCategory() == template.getCategory() ? 200 : 0;
            totalScore += incident.getSubcategory() == template.getSubcategory() ? 200 : 0;
            totalScore += incident.getUrgency() == template.getUrgency() ? 200 : 0;
            totalScore += Objects.equals(incident.getAddress().getDistrict(), template.getAddress().getDistrict()) ? 200 : 0;
            totalScore += Objects.equals(incident.getAddress().getMunicipality(), template.getAddress().getMunicipality()) ? 200 : 0;
            totalScore += computeLocationScore(incident, template);
            totalScore += computeVehicleScore(incident, template);
            totalScore += computeTimeScore(incident.getStartTime(), incident.getCallPickedUpTime());
            totalScore += computeTimeScore(incident.getCallPickedUpTime(), incident.getEndTime());

        }

        return (int) Math.round((totalScore / maxScore) * 5000);
    }

    private double computeTimeScore(LocalDateTime startTime, LocalDateTime endTime) {
        long seconds = Duration.between(startTime, endTime).getSeconds();

        if (seconds <= 60) {
            return 200;
        } else if (seconds >= 180) {
            return 0;
        } else {
            return 200 * (180 - seconds) / 120.0;
        }
    }

    private double computeVehicleScore(Incident incident, IncidentTemplate template){
        Set<VehicleType> selectedVehicleTypes = incident.getVehicleTypes();
        Set<VehicleType> correctVehicleTypes = template.getVehicles().stream()
                .map(Vehicle::getType)
                .collect(Collectors.toSet());

        Set<VehicleType> intersection = new HashSet<>(selectedVehicleTypes);
        intersection.retainAll(correctVehicleTypes);

        int correctMatches = intersection.size();
        double finalScore = getFinalScore(correctVehicleTypes, selectedVehicleTypes, correctMatches);

        return Math.max(0, finalScore);
    }

    private double getFinalScore(Set<VehicleType> correctVehicleTypes, Set<VehicleType> selectedVehicleTypes, int correctMatches) {
        int totalCorrect = correctVehicleTypes.size();
        int totalSelected = selectedVehicleTypes.size();
        int incorrectSelections = totalSelected - correctMatches;

        int maxAllowed = totalCorrect * 2;

        double penaltyRatio = (double) incorrectSelections / (maxAllowed - totalCorrect);
        penaltyRatio = Math.min(1.0, penaltyRatio);

        double baseScore = ((double) correctMatches / totalCorrect) * 200;
        return baseScore * (1.0 - penaltyRatio);
    }

    private double computeLocationScore(Incident incident, IncidentTemplate template) {

        double selectedLat = Double.parseDouble(incident.getAddress().getLatitude());
        double selectedLon = Double.parseDouble(incident.getAddress().getLongitude());
        double correctLat = Double.parseDouble(template.getAddress().getLatitude());
        double correctLon = Double.parseDouble(template.getAddress().getLongitude());
        double distanceMeters = Utils.calculateDistanceMeters(correctLat, correctLon, selectedLat, selectedLon);

        if (distanceMeters <= 30) {
            return 200;
        } else if (distanceMeters >= 125) {
            return 0;
        } else {
            return (125 - distanceMeters) / (125 - 30);
        }
    }
}

package com.gina.simulator.aiContext;

import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.integration.Osm.features.Building;
import com.gina.simulator.integration.Osm.features.Highway;
import com.gina.simulator.integration.Osm.features.NearbyFeatures;
import com.gina.simulator.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AiContextGenerator {

    public String generateContext(NearbyFeatures nearbyFeatures, IncidentTemplate incidentTemplate) {
        List<Building> buildings = nearbyFeatures.getBuildings();
        List<Highway> highways = nearbyFeatures.getHighways();

        StringBuilder sb = new StringBuilder();
        sb.append("Jsi civilista Karel Novák 43 let, který právě volá na hasičskou tísňovou linku.\n");

        // Vlastnosti
        sb.append("Jsi průměrný civilista, který je chytrý, ale trochu stresuješ.\n");

        // Omezeni
        sb.append("Neříkej přesné vzdálenosti. Vzdálenosti jsou uvedeny v metrech. Chovej se jako klasický občan");
        sb.append("volající na tisňovou linku. Neuvedené informace si domysli. Odpovídej v 1-3 kratších větách. ");
        sb.append("Anglické názvy pocházejí z Open Street Maps. Nemluv anglicky a překládej.");
        sb.append("Odpovídej pouze na dotázané informace. Podle popisu co se stalo si vyber místo incidentu.\n"); // todo: jak resit lokalitu

        // Informace o incidentu
        sb.append("Detail incidentu které nesmíš říct. Kategorie: ").append(incidentTemplate.getCategory());
        sb.append(" podkategorie: ").append(incidentTemplate.getSubcategory());
        sb.append(". Tyhle musíš. okres: ").append(incidentTemplate.getAddress().getDistrict());
        sb.append(" vesnice/město: ").append(incidentTemplate.getAddress().getMunicipality());
        sb.append(" co se stalo: ").append(incidentTemplate.getSpecification()).append("\n");

        // Okoli
        sb.append("Tvé okolí vypadá následovně:\n");
        sb.append(summarizeBuildings(buildings)).append("\n");
        sb.append(summarizeHighways(highways)).append("\n");

        log.debug(sb.toString());
        return sb.toString();
    }

    private String summarizeBuildings(List<Building> buildings) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < buildings.size(); i++) {
            sb.append("Budova ").append(i + 1).append(": ").append(buildings.get(i));
        }

        return sb.toString();
    }

    private String summarizeHighways(List<Highway> highways) {
        StringBuilder sb = new StringBuilder("Vozovky: ");

        Map<String, Long> typeCounts = highways.stream()
                .collect(Collectors.groupingBy(Highway::getType, Collectors.counting()));

        Map<String, Long> surfaceCounts = highways.stream()
                .filter(h -> Utils.isEmpty(h.getSurface()))
                .collect(Collectors.groupingBy(Highway::getSurface, Collectors.counting()));

        Map<String, Long> trackTypeCounts = highways.stream()
                .filter(h -> Utils.isEmpty(h.getTrackType()))
                .collect(Collectors.groupingBy(Highway::getTrackType, Collectors.counting()));

        sb.append(summarizeHighwayAttributes(typeCounts, "typy silnic"));
        sb.append(summarizeHighwayAttributes(surfaceCounts, "povrchy cest"));
        sb.append(summarizeHighwayAttributes(trackTypeCounts, "typy cest (track)"));

        return sb.toString();
    }

    private String summarizeHighwayAttributes(Map<String, Long> map, String prefix) {
        if (map.isEmpty()) return "není známo " + prefix;

        return prefix + " v okolí jsou: " +
                map.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(2) 
                        .map(e -> e.getKey() + " (" + e.getValue() + "×)")
                        .collect(Collectors.joining(", "));
    }
}

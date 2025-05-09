package com.gina.simulator.aiContext;

import com.gina.simulator.features.*;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
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
        StringBuilder sb = new StringBuilder();
        sb.append("Jsi civilista Karel Novák 43 let, který právě volá na hasičskou tísňovou linku.\n");

        // Vlastnosti
        sb.append("Jsi průměrný civilista, který je chytrý, ale trochu stresuješ.\n");

        // Omezeni
        sb.append("Neříkej přesné vzdálenosti. Vzdálenosti jsou uvedeny v metrech. Chovej se jako klasický občan ");
        sb.append("volající na tisňovou linku. Neuvedené informace si domysli. Odpovídej v 1-3 kratších větách. ");
        sb.append("Anglické názvy pocházejí z Open Street Maps. Nemluv anglicky a překládej.");
        sb.append("Odpovídej pouze na dotázané informace. Podle popisu co se stalo si vyber místo incidentu.\n"); // todo: jak resit lokalitu

        // Informace o incidentu
        sb.append("Detail incidentu které nesmíš říct. Kategorie: ").append(incidentTemplate.getCategory());
        sb.append(", podkategorie: ").append(incidentTemplate.getSubcategory());
        sb.append(". Tyhle musíš: okres: ").append(incidentTemplate.getAddress().getDistrict());
        sb.append(", vesnice/město: ").append(incidentTemplate.getAddress().getMunicipality());
        sb.append(", co se stalo: ").append(incidentTemplate.getSpecification()).append("\n");

        // Okoli
        sb.append("Tvé okolí vypadá následovně:\n");
        sb.append(summarizeBuildings(nearbyFeatures.getBuildings())).append("\n");
        sb.append(summarizeHighways(nearbyFeatures.getHighways())).append("\n");
        sb.append(summarizeLanduses(nearbyFeatures.getLanduses())).append("\n");
        sb.append(summarizeNaturals(nearbyFeatures.getNaturals())).append("\n");
        sb.append(summarizeLeisures(nearbyFeatures.getLeisures())).append("\n");
        sb.append(summarizeAmenities(nearbyFeatures.getAmenities())).append("\n");

        log.debug(sb.toString());
        return sb.toString();
    }

    private String summarizeBuildings(List<Building> buildings) {
        if (buildings.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("Budovy: ");

        for (int i = 0; i < buildings.size(); i++) {
            sb.append("Budova ").append(i + 1).append(": ").append(buildings.get(i));
        }

        return sb.toString();
    }

    private String summarizeLeisures(List<Leisure> leisures) {
        if (leisures.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("Místa na trávení volného času: ");

        for (int i = 0; i < leisures.size(); i++) {
            sb.append("Místo ").append(i + 1).append(": ").append(leisures.get(i));
        }

        return sb.toString();
    }

    private String summarizeAmenities(List<Amenity> amenities) {
        if (amenities.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("Významné zařízení pro obyvatele: ");

        for (int i = 0; i < amenities.size(); i++) {
            sb.append("Zařízení ").append(i + 1).append(": ").append(amenities.get(i));
        }

        return sb.toString();
    }

    private String summarizeHighways(List<Highway> highways) {
        if (highways.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("Vozovky: ");

        Map<String, Long> typeCounts = highways.stream()
                .collect(Collectors.groupingBy(Highway::getType, Collectors.counting()));

        Map<String, Long> surfaceCounts = highways.stream()
                .filter(h -> Utils.isEmpty(h.getSurface()))
                .collect(Collectors.groupingBy(Highway::getSurface, Collectors.counting()));

        Map<String, Long> trackTypeCounts = highways.stream()
                .filter(h -> Utils.isEmpty(h.getTrackType()))
                .collect(Collectors.groupingBy(Highway::getTrackType, Collectors.counting()));

        sb.append(summarizeAttribute(typeCounts, "typy silnic"));
        sb.append(summarizeAttribute(surfaceCounts, "povrchy cest"));
        sb.append(summarizeAttribute(trackTypeCounts, "typy cest (track)"));

        return sb.toString();
    }

    private String summarizeLanduses(List<Landuse> landuses) {
        if (landuses.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("Využití místa na kterém stojíš lidmi: ");

        Map<String, Long> typeCounts = landuses.stream()
                .collect(Collectors.groupingBy(Landuse::getType, Collectors.counting()));

        sb.append(summarizeAttribute(typeCounts, "způsob využití"));
        return sb.toString();
    }

    private String summarizeNaturals(List<Natural> naturals) {
        if (naturals.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("Popis přírody místa na kterém stojíš: ");

        Map<String, Long> typeCounts = naturals.stream()
                .collect(Collectors.groupingBy(Natural::getType, Collectors.counting()));

        sb.append(summarizeAttribute(typeCounts, "typy"));
        return sb.toString();
    }

    private String summarizeAttribute(Map<String, Long> map, String prefix) {
        if (map.isEmpty()) return "není známo " + prefix;

        return prefix + ": " +
                map.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(2) 
                        .map(e -> e.getKey() + " (" + e.getValue() + "×)")
                        .collect(Collectors.joining(", "));
    }
}

package com.gina.simulator.aiContext;

import com.gina.simulator.enums.Difficulty;
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

    /**
     * Generates human readeable string, that is sent to LLM in prompt as context.
     * @param nearbyFeatures object to parse data from
     * @param incidentTemplate template of incident
     * @return himan readable string
     */
    public String generateContext(NearbyFeatures nearbyFeatures, IncidentTemplate incidentTemplate, Difficulty difficulty) {
        StringBuilder sb = new StringBuilder();
        sb.append(generateCharacter(difficulty));

        // Omezeni
        sb.append("Své jméno si vymysli. Neříkej přesné vzdálenosti. Vzdálenosti jsou uvedeny v metrech. Chovej se jako klasický občan ");
        sb.append("volající na tisňovou linku. Neuvedené informace si domysli nebo je neznáš. Odpovídej v 1-2 kratších větách. ");
        sb.append("Anglické názvy pocházejí z Open Street Maps. Nemluv anglicky a překládej do češtiny.");
        sb.append("Odpovídej pouze na dotázané informace. Pokud je níže uvedena budova \n");
        sb.append(", incident se stal v nejbližší, pokud to nerozporuje s popisem incidentu níže.");

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

    private String generateCharacter(Difficulty difficulty) {
        return switch (difficulty) {
            case EASIEST -> """
                    Je ti 43 let. Máš rozsáhlé životní zkušenosti, klidnou povahu a výborně zvládáš stresové situace.
                    Máš širší znalosti první pomoci a dokážeš věcně a srozumitelně popsat každý detail incidentu.
                    """;
            case EASY -> """
                    Je ti 29 let. Jsi relativně klidná, mírně zkušená v krizových situacích.
                    Znáš základní postupy první pomoci a snažíš se být nápomocná, i když jsi trochu nervózní.
                    """;
            case MEDIUM -> """
                    Je ti 35 let. Jsi středně zkušený občan, cítíš určitou míru stresu a nejistoty.
                    Máš základní přehled o první pomoci, ale v návalu emocí můžeš zapomenout některé detaily.
                    """;
            case HARD -> """
                    Je ti 24 let. Jsi mladá a vystrašená, cítíš se velmi nejistě.
                    Nemáš zkušenosti s první pomocí, často se rozptýlíš okolními podněty.
                    Na detailnější otázky dokážeš odpovídat občas trochu nekonkrétně.
                    """;
            case HARDEST -> """
                    Je ti 18 let. Jsi velmi vystrašený, v panice nevíš, co se děje.
                    Máš nulové zkušenosti s první pomocí, vnímáš jen to nejbližší ohrožení.
                    Pokud budeš dotázán mimo bezprostřední popis incidentu, nereaguj nebo zeptej se, proč je to důležité.
                    Na otázku odpovídáš zmateně a je obtížnější z tebe informace dostat.
                    """;
        };
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

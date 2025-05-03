package com.gina.simulator.integration.Osm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gina.simulator.integration.features.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@NoArgsConstructor
@Component
public class OsmParser {

    public static final Map<String, String> RUIAN_TYPE_DESCRIPTIONS = Map.ofEntries(
            Map.entry("1", "průmyslový objekt"),
            Map.entry("2", "zemědělská usedlost"),
            Map.entry("3", "objekt k bydlení"),
            Map.entry("4", "objekt lesního hospodářství"),
            Map.entry("5", "objekt občanské vybavenosti"),
            Map.entry("6", "bytový dům"),
            Map.entry("7", "rodinný dům"),
            Map.entry("8", "stavba pro rodinnou rekreaci"),
            Map.entry("9", "stavba pro shromažďování většího počtu osob"),
            Map.entry("10", "stavba pro obchod"),
            Map.entry("11", "stavba ubytovacího zařízení"),
            Map.entry("12", "stavba pro výrobu a skladování"),
            Map.entry("13", "zemědělská stavba"),
            Map.entry("14", "stavba pro administrativu"),
            Map.entry("15", "stavba občanského vybavení"),
            Map.entry("16", "stavba technického vybavení"),
            Map.entry("17", "stavba pro dopravu"),
            Map.entry("18", "garáž"),
            Map.entry("19", "jiná stavba"),
            Map.entry("20", "víceúčelová stavba"),
            Map.entry("21", "skleník"),
            Map.entry("22", "přehrada"),
            Map.entry("23", "hráz přehrazující vodní tok nebo údolí"),
            Map.entry("24", "hráz k ochraně nemovitostí před zaplavením při povodni"),
            Map.entry("25", "hráz ohrazující umělou vodní nádrž"),
            Map.entry("26", "jez"),
            Map.entry("27", "stavba k plavebním účelům"),
            Map.entry("28", "vodní elektrárna"),
            Map.entry("29", "stavba odkaliště")
    );

    public static final Map<String, String> TRACK_TYPE_DESCRIPTIONS = Map.ofEntries(
            Map.entry("grade1", "pevný povrch"),
            Map.entry("grade2", "většinou pevný povrch"),
            Map.entry("grade3", "mix pevných a měkkých povrchů"),
            Map.entry("grade4", "většinou měkky povrch"),
            Map.entry("grade5", "měkky povrch")
    );

    public Node parseNode(JsonNode el){
        String lon = el.get("lon").asText();
        String lat = el.get("lat").asText();
        return new Node(lon, lat);
    }

    public Building parseBuilding(JsonNode tags, JsonNode nodes){
        Building building = new Building();
        building.setType(resolveBuildingType(tags.path("building:ruian:type").asText("není známo")));
        building.setLevels(tags.path("building:levels").asText("není známo"));
        building.setFlats(tags.path("building:flats").asText("není známo"));
        building.setHouseNumber(tags.path("ref:ruian:building").asText("není známo"));

        Optional.ofNullable(nodes)
                .filter(JsonNode::isArray)
                .ifPresent(array -> array.forEach(n -> building.getNodeIds().add(n.asText())));

        return building;
    }

    public Natural parseNatural(JsonNode tags, JsonNode nodes){
        Natural natural = new Natural();
        natural.setType(tags.path("natural").asText("není známo"));

        Optional.ofNullable(nodes)
                .filter(JsonNode::isArray)
                .ifPresent(array -> array.forEach(n -> natural.getNodeIds().add(n.asText())));

        return natural;
    }

    public Landuse parseLanduse(JsonNode tags, JsonNode nodes){
        Landuse landuse = new Landuse();
        landuse.setType(tags.path("landuse").asText("není známo"));

        Optional.ofNullable(nodes)
                .filter(JsonNode::isArray)
                .ifPresent(array -> array.forEach(n -> landuse.getNodeIds().add(n.asText())));

        return landuse;
    }

    public Highway parseHighway(JsonNode tags, JsonNode nodes){
        Highway highway = new Highway();
        highway.setType(tags.path("highway").asText("není známo"));
        highway.setSmoothness(tags.path("smoothness").asText("není známo"));
        highway.setSurface(tags.path("surface").asText("není známo"));
        highway.setTrackType(resolveTrackType(tags.path("tracktype").asText("není známo")));

        Optional.ofNullable(nodes)
                .filter(JsonNode::isArray)
                .ifPresent(array -> array.forEach(n -> highway.getNodeIds().add(n.asText())));

        return highway;
    }

    public NearbyFeatures parsePrivateObjectsFromOSM(String osmResponse) {
        ObjectMapper mapper = new ObjectMapper();
        NearbyFeatures features = new NearbyFeatures();

        try {
            JsonNode root = mapper.readTree(osmResponse);
            JsonNode elements = root.get("elements");

            if (elements == null || !elements.isArray()) {
                return null;
            }


            for (JsonNode el : elements) {
                String type = el.path("type").asText();
                JsonNode tags = el.path("tags");
                JsonNode nodes = el.path("nodes");

                switch (type){
                    case "node":
                        String id = el.path("id").asText("není známo");
                        features.getNodeMap().put(id, parseNode(el));
                        break;
                    case "way": // todo: waterway, leisure, amenity, shop, public_transport, tourism
                        if (tags == null || nodes == null) continue;

                        if (tags.get("building") != null){
                            features.getBuildings().add(parseBuilding(tags, nodes));
                        } else if (tags.get("natural") != null) {
                            features.getNaturals().add(parseNatural(tags, nodes));
                        } else if (tags.get("landuse") != null) {
                            features.getLanduses().add(parseLanduse(tags, nodes));
                        } else if (tags.get("highway") != null) {
                            features.getHighways().add(parseHighway(tags, nodes));
                        }
                        break;

                    default:
                }
            }


        } catch (Exception e) {
            log.warn("Chyba při generování promptu.\n {}", e.getMessage());
        }
        features.computeCoords();
        return features;
    }

    private String resolveBuildingType(String typeNumber){
        return OsmParser.RUIAN_TYPE_DESCRIPTIONS.getOrDefault(typeNumber, "Není známo");
    }

    private String resolveTrackType(String typeNumber){
        return OsmParser.TRACK_TYPE_DESCRIPTIONS.getOrDefault(typeNumber, "Není známo");
    }
}

package com.gina.simulator.integration.Osm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gina.simulator.features.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * Class responsible for parsing OSM JSON response.
 */
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
        building.setType(resolveBuildingType(tags.path("building:ruian:type").asText("")));
        building.setLevels(tags.path("building:levels").asText(""));
        building.setFlats(tags.path("building:flats").asText(""));
        building.setHouseNumber(tags.path("ref:ruian:building").asText(""));

        Optional.ofNullable(nodes)
                .filter(JsonNode::isArray)
                .ifPresent(array -> array.forEach(n -> building.getNodeIds().add(n.asText())));

        return building;
    }

    public Natural parseNatural(JsonNode tags){
        Natural natural = new Natural();
        natural.setType(tags.path("natural").asText(""));

        return natural;
    }

    public Landuse parseLanduse(JsonNode tags){
        Landuse landuse = new Landuse();
        landuse.setType(tags.path("landuse").asText(""));

        return landuse;
    }

    public Highway parseHighway(JsonNode tags){
        Highway highway = new Highway();
        highway.setType(tags.get("highway").asText());
        highway.setSurface(tags.path("surface").asText(""));
        highway.setTrackType(resolveTrackType(tags.path("tracktype").asText("")));

        return highway;
    }

    public Leisure parseLeisure(JsonNode tags, JsonNode nodes){
        Leisure leisure = new Leisure();
        leisure.setType(tags.get("leisure").asText(""));
        leisure.setSport(tags.path("sport").asText(""));

        Optional.ofNullable(nodes)
                .filter(JsonNode::isArray)
                .ifPresent(array -> array.forEach(n -> leisure.getNodeIds().add(n.asText())));

        return leisure;
    }

    private Amenity parseAmenity(JsonNode tags, JsonNode nodes) {
        Amenity amenity = new Amenity();
        amenity.setType(tags.get("amenity").asText(""));
        amenity.setName(tags.path("name").asText(""));
        amenity.setBuilding(tags.path("building").asText(""));
        amenity.setBuildingType(resolveBuildingType(tags.path("building:ruian:type").asText("")));
        amenity.setShelter_type(tags.path("shelter_type").asText(""));
        amenity.setBench(tags.path("bench").asBoolean());

        Optional.ofNullable(nodes)
                .filter(JsonNode::isArray)
                .ifPresent(array -> array.forEach(n -> amenity.getNodeIds().add(n.asText())));

        return amenity;
    }

    /**
     * Function responsible for parsing JSON response from OSM.
     * @param osmResponse JSON response
     * @param lat incident latitude
     * @param lon incident longtitude
     * @return object containing information about incident surrounding
     */
    public NearbyFeatures parsePrivateObjectsFromOSM(String osmResponse, double lat, double lon) {
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
                        String id = el.path("id").asText("");
                        features.getNodeMap().put(id, parseNode(el));
                        break;
                    case "way": // todo: waterway, leisure, amenity, shop, public_transport, tourism
                        if (tags == null || nodes == null) continue;

                        if (tags.get("building") != null && tags.get("amenity") == null) {
                            features.getBuildings().add(parseBuilding(tags, nodes));
                        } else if (tags.get("natural") != null) {
                            features.getNaturals().add(parseNatural(tags));
                        } else if (tags.get("landuse") != null) {
                            features.getLanduses().add(parseLanduse(tags));
                        } else if (tags.get("highway") != null) {
                            features.getHighways().add(parseHighway(tags));
                        } else if (tags.get("leisure") != null) {
                            features.getLeisures().add(parseLeisure(tags, nodes));
                        } else if (tags.get("amenity") != null) {
                            features.getAmenities().add(parseAmenity(tags, nodes));
                        }
                        break;

                    default:
                }
            }


        } catch (Exception e) {
            log.warn("Chyba při generování promptu.\n {}", e.getMessage());
        }
        features.computeCoords(lat, lon);
        return features;
    }

    private String resolveBuildingType(String typeNumber){
        return OsmParser.RUIAN_TYPE_DESCRIPTIONS.getOrDefault(typeNumber, "");
    }

    private String resolveTrackType(String typeNumber){
        return OsmParser.TRACK_TYPE_DESCRIPTIONS.getOrDefault(typeNumber, "");
    }
}

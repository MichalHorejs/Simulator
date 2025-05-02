package com.gina.simulator.integration.Osm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.integration.features.NearbyFeatures;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class OsmService {

    @Value("${openstreetmap.url}")
    String url;

    @Value("${openstreetmap.radius:200}")
    String radius;

    RestTemplate restTemplate;

    public OsmService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public NearbyFeatures generateNearbyFeatures(IncidentTemplate incidentTemplate) {
        double latitude = Double.parseDouble(incidentTemplate.getAddress().getLatitude());
        double longitude = Double.parseDouble(incidentTemplate.getAddress().getLongitude());

        String nearbyFeatures = obtainNearbyObjectsFromOSM(incidentTemplate);
        return parsePrivateObjectsFromOSM(nearbyFeatures, latitude, longitude);
    }

    private NearbyFeatures parsePrivateObjectsFromOSM(String osmResponse, double incidentLat, double incidentLon) {
        StringBuilder sb = new StringBuilder("Jako civilista vidíš následující okolní objekty:\n");
        ObjectMapper mapper = new ObjectMapper();
        OsmParser parser = new OsmParser();
        NearbyFeatures features = new NearbyFeatures();

        try {
            JsonNode root = mapper.readTree(osmResponse);
            JsonNode elements = root.get("elements");

            if (elements == null || !elements.isArray()) {
                return null;
            }


            for (JsonNode el : elements) {
                String type = el.path("type").asText();

                switch (type){
                    case "node":
                        String id = el.path("id").asText("není známo");
                        features.getNodeMap().put(id, parser.parseNode(el));
                        break;
                    case "way":
                        JsonNode tags = el.path("tags");
                        JsonNode nodes = el.path("nodes");
                        if (tags == null || nodes == null) continue;

                        if (tags.get("building") != null){
                            features.getBuildings().add(parser.parseBuilding(tags, nodes));
                        } else if (tags.get("natural") != null) {
                            features.getNaturals().add(parser.parseNatural(tags, nodes));
                        }
                        break;

                    default:
                }
            }


        } catch (Exception e) {
            log.warn("Chyba při generování promptu.");
        }

        return features;
    }

    private String obtainNearbyObjectsFromOSM(IncidentTemplate incidentTemplate){
        String latitude = incidentTemplate.getAddress().getLatitude();
        String longitude = incidentTemplate.getAddress().getLongitude();

        String overpassQuery = buildQuery(latitude, longitude);

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("data", overpassQuery)
                .build()
                .toUri();

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        return response.getBody();
    }

    private String buildQuery(String latitude, String longitude) {
        String[] features = {
                "building", "natural", "landuse", "waterway",
                "highway", "leisure", "amenity", "shop",
                "public_transport", "tourism"
        };

        String nodeTemplate = "node(around:%s,%s,%s)[%s]";
        String wayTemplate = "way(around:%s,%s,%s)[%s]";
        String relationTemplate = "relation(around:%s,%s,%s)[%s]";

        String clauses = Arrays.stream(features)
                .flatMap(f -> Stream.of(
                        String.format(nodeTemplate, radius, latitude, longitude, f),
                        String.format(wayTemplate, radius, latitude, longitude, f),
                        String.format(relationTemplate, radius, latitude, longitude, f)
                ))
                .collect(Collectors.joining(";\n", "", ";"));

        return String.format(
                "[out:json];\n(\n%s\n);\nout body;\n>;\nout skel qt;",
                clauses
        );
    }
}

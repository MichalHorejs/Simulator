package com.gina.simulator.integration.Osm;

import com.gina.simulator.features.NearbyFeatures;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.integration.ruian.RuianService;
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

/**
 * Service responsible for communication with Open Street Maps API.
 */
@Service
@Slf4j
public class OsmService {

    @Value("${openstreetmap.url}")
    String url;

    @Value("${openstreetmap.radius:200}")
    String radius;

    RestTemplate restTemplate;

    private final OsmParser osmParser;
    private final RuianService ruianService;

    public OsmService(RestTemplateBuilder builder, OsmParser osmParser, RuianService ruianService) {
        this.restTemplate = builder.build();
        this.osmParser = osmParser;
        this.ruianService = ruianService;
    }

    /**
     * Generates incident surrouding info stored in objects.
     * @param incidentTemplate template of incident
     * @return object containing information about incident surrounding
     */
    public NearbyFeatures generateNearbyFeatures(IncidentTemplate incidentTemplate) {
        double latitude = Double.parseDouble(incidentTemplate.getAddress().getLatitude());
        double longitude = Double.parseDouble(incidentTemplate.getAddress().getLongitude());

        String osmResponse = obtainNearbyObjectsFromOSM(incidentTemplate);
        NearbyFeatures nf = osmParser.parsePrivateObjectsFromOSM(osmResponse, latitude, longitude);
        return ruianService.obtainAdditionalInformation(nf);
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

    /**
     * Generates qurey that OSM understands and which asks for wanted info.
     * @param latitude latitude of incident
     * @param longitude longtitude of incident
     * @return OSM JSON response
     */
    private String buildQuery(String latitude, String longitude) {
        StringBuilder query = new StringBuilder("[out:json];\n");
        String[] features = {
                "building", "waterway",
                "highway", "leisure", "amenity", "shop",
                "public_transport"
        };

        String wayTemplate = "way(around:%s,%s,%s)[%s]";
        String endTemplate = "out body;\n>;\nout skel qt;\n";

        String clauses = Arrays.stream(features)
                .flatMap(f -> Stream.of(
                        String.format(wayTemplate, radius, latitude, longitude, f)
                ))
                .collect(Collectors.joining(";\n", "", ";"));

        query.append("\nnode(around:%s,%s,%s);\n(\n%s\n);".formatted(radius, latitude, longitude, clauses)).append(endTemplate);
        query.append("is_in(%s,%s);\narea._[landuse];\n".formatted(latitude, longitude)).append(endTemplate);
        query.append("is_in(%s,%s);\narea._[natural];\n".formatted(latitude, longitude)).append(endTemplate);

        return query.toString();
    }
}

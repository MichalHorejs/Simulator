package com.gina.simulator.integration.ruian;

import com.gina.simulator.features.Building;
import com.gina.simulator.features.NearbyFeatures;
import com.gina.simulator.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Class responsible for communication with RUIAN
 */
@Service
@Slf4j
public class RuianService {

    @Value("${ruian.api.url}")
    String url;

    RestTemplate restTemplate;

    public RuianService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * Finds house number for given building.
     * @param nearbyFeatures info about incident surrounding
     * @return object with additional info
     */
    public NearbyFeatures obtainAdditionalInformation(NearbyFeatures nearbyFeatures) {
        List<Building> buildings = nearbyFeatures.getBuildings();

        buildings.forEach(building -> {
            if (!Utils.isEmpty(building.getHouseNumber())){
                String parsedHouseNumber = obtainHouseNumber(building.getHouseNumber());

                if (!Utils.isEmpty(parsedHouseNumber)) {
                    building.setHouseNumber(parsedHouseNumber);
                } else {
                    building.setHouseNumber("");
                }
            }
        });

        return nearbyFeatures;
    }

    private String obtainHouseNumber(String ruianId) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url+"/"+ruianId, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Document doc = Jsoup.parse(response.getBody());
                Elements tables = doc.select("table.table-info");

                for (Element table : tables) {
                    Elements labelCells = table.select("th");
                    for (Element label : labelCells) {
                        if (label.text().trim().contains("Čísla popisná nebo evidenční")) {

                            Elements rows = table.select("tr");
                            for (Element row : rows) {
                                String th = row.select("th").text().trim();
                                String td = row.select("td").text().trim();

                                if (th.contains("Čísla popisná nebo evidenční")) {
                                    log.info("→ Číslo popisné/evidenční: {}", td);
                                    return td;
                                }
                            }
                        }
                    }
                }
            }
        } catch (RestClientException e) {
            log.error("Chyba při načítání dat z RÚIAN pro ID {}", ruianId, e);
            log.error(e.getMessage());
        }
        return "";
    }
}

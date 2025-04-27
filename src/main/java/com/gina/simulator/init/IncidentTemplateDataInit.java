package com.gina.simulator.init;

import com.gina.simulator.address.Address;
import com.gina.simulator.enums.Category;
import com.gina.simulator.enums.Subcategory;
import com.gina.simulator.enums.Urgency;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.incidentTemplate.IncidentTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class IncidentTemplateDataInit {

    private final IncidentTemplateRepository repository;
    private final ApplicationContext applicationContext;

    @Value("${data.init.incidentTemplate:false}")
    private boolean loadData;

    @Value("classpath:data/incidents-new.csv")
    private Resource incidentsCsv;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (loadData) {
            if (repository.count() == 0) {
                IncidentTemplateDataInit proxy = applicationContext.getBean(IncidentTemplateDataInit.class);
                proxy.saveIncidentTemplates();
            }
            log.info("Incident Template data initialized");
        }
    }

    @Transactional
    public void saveIncidentTemplates() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(incidentsCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.readLine();

            br.lines().forEach(line -> {
                try {
                    String[] parts = line.split(";");
                    if (parts.length >= 16 && isValidRow(parts)) {
                        IncidentTemplate template = new IncidentTemplate();
                        template.setTitle(parts[1].trim());
                        template.setSpecification(parts[2].trim());
                        template.setUrgency(mapUrgency(parts[3].trim()));
                        template.setCategory(Category.fromDisplayName(parts[4].trim()));
                        template.setSubcategory(Subcategory.fromDisplayName(parts[5].trim()));

                        Address address = new Address();
                        address.setLatitude(parts[8].trim());
                        address.setLongitude(parts[9].trim());
                        address.setDistrict(parts[12].trim());
                        address.setMunicipality(parts[13].trim());
                        template.setAddress(address);

                        repository.save(template);
                    }
                } catch (Exception e) {
                    log.warn("Skipping line: {}", line);
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException("Error loading CSV file", ex);
        }
    }

    private boolean isValidRow(String[] parts) {
        for (String part : parts) {
            if (part == null || part.trim().isEmpty() || part.trim().equalsIgnoreCase("unknown")) {
                return false;
            }
        }
        return true;
    }

    private Urgency mapUrgency(String value) {
        if ("1".equals(value)) {
            return Urgency.LOW;
        } else if ("2".equals(value)) {
            return Urgency.MEDIUM;
        }
        return Urgency.HIGH;
    }
}

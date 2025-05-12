package com.gina.simulator.init;

import com.gina.simulator.address.Address;
import com.gina.simulator.enums.Category;
import com.gina.simulator.enums.Subcategory;
import com.gina.simulator.enums.Urgency;
import com.gina.simulator.enums.VehicleType;
import com.gina.simulator.incidentTemplate.IncidentTemplate;
import com.gina.simulator.incidentTemplate.IncidentTemplateRepository;
import com.gina.simulator.vehicle.Vehicle;
import com.gina.simulator.vehicle.VehicleRepository;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class IncidentTemplateDataInit {

    private final IncidentTemplateRepository incidentTemplateRepository;
    private final VehicleRepository vehicleRepository;
    private final ApplicationContext applicationContext;

    @Value("${data.init.incidentTemplate:false}")
    private boolean loadData;

    @Value("${data.init.incidentTemplate.file}")
    private Resource incidentsCsv;

    @Value("${data.init.vehicles.file}")
    private Resource vehiclesCsv;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (loadData) {
            if (incidentTemplateRepository.count() == 0) {
                IncidentTemplateDataInit proxy = applicationContext.getBean(IncidentTemplateDataInit.class);
                proxy.saveVehicles();
                proxy.saveIncidentTemplates();
            }
            log.info("Vehicle and Incident Template data initialized");
        }
    }

    @Transactional
    public void saveVehicles(){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                vehiclesCsv.getInputStream(), StandardCharsets.UTF_8))) {

            br.lines().skip(1).forEach(line -> {
                try {
                    String[] parts = line.split(";");
                    if (parts.length >= 4) {
                        Vehicle vehicle = new Vehicle();
                        vehicle.setId(Integer.parseInt(parts[0].trim()));
                        vehicle.setName(parts[1].trim());
                        vehicle.setType(parseVehicleType(parts[3].trim()));
                        vehicleRepository.save(vehicle);
                    }
                } catch (Exception e) {
                    log.error("Chyba při zpracování řádku: " + line, e);
                }
            });
        } catch (Exception e) {
            log.error("Nelze načíst soubor s auty", e);
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
                    if (parts.length >= 9 && isValidRow(parts)) {
                        IncidentTemplate template = new IncidentTemplate();
                        template.setSpecification(parts[0].trim());
                        template.setUrgency(mapUrgency(parts[1].trim()));
                        template.setCategory(Category.fromDisplayName(parts[2].trim()));
                        template.setSubcategory(Subcategory.fromDisplayName(parts[3].trim()));

                        Address address = new Address();
                        address.setLatitude(parts[4].trim());
                        address.setLongitude(parts[5].trim());
                        address.setDistrict(parts[6].trim());
                        address.setMunicipality(parts[7].trim());
                        template.setAddress(address);

                        String vehicleIds = parts[8].trim();
                        List<Vehicle> vehicles = new ArrayList<>();
                        if (!vehicleIds.isEmpty()) {
                            String[] ids = vehicleIds.split(",");
                            for (String id : ids) {
                                try {
                                    int vehicleId = Integer.parseInt(id.trim());
                                    vehicleRepository.findById(vehicleId).ifPresent(vehicles::add);
                                } catch (NumberFormatException ex) {
                                    log.error("Neplatné ID vozidla: {}", id, ex);
                                }
                            }
                        }
                        template.setVehicles(vehicles);

                        incidentTemplateRepository.save(template);
                    }
                } catch (Exception e) {
                    log.warn("Skipping line: {}", line);
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException("Error loading CSV file", ex);
        }
    }

    private VehicleType parseVehicleType(String rawValue) {
        String code = rawValue.contains(" - ") ? rawValue.split(" - ")[0].trim() : rawValue.trim();
        for (VehicleType type : VehicleType.values()) {
            String typeCode = type.getDisplayName().contains(" - ")
                    ? type.getDisplayName().split(" - ")[0].trim()
                    : type.getDisplayName().trim();
            if (typeCode.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Neznámý typ vozidla: " + rawValue);
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

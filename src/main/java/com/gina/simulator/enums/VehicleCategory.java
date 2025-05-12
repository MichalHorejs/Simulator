package com.gina.simulator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VehicleCategory {
    VOZIDLA("Vozidla"),
    KONTEJNERY("Kontejnery"),
    PRIVESY("Přívěsy"),
    OSTATNI("Ostatní");

    private final String displayName;

    public static VehicleCategory fromDisplayName(String name) {
        for (VehicleCategory category : values()) {
            if (category.getDisplayName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Neznámá kategorie vozidel: " + name);
    }
}

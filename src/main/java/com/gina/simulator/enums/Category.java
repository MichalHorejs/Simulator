package com.gina.simulator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    UNIK_NEBEZPECNYCH_LATEK("Únik nebezpečných látek"),
    DOPRAVNI_NEHODA("Dopravní nehoda"),
    TECHNICKA_POMOC("Technická pomoc"),
    ZACHRANA_OSOB_A_ZVIRAT("Záchrana osob a zvířat"),
    PLANY_POPLACH("Planý poplach"),
    POZAR("Požár"),
    OSTATNI_MIMORADNA_UDALOST("Ostatní mimořádná událost"),
    JINE_ZATIM_NEURCENO("Jiné, zatím neurčeno"),
    FORMALNE_ZALOZENA_UDALOST("Formálně založená událost");

    private final String displayName;

    public static Category fromDisplayName(String name) {
        for (Category category : values()) {
            if (category.getDisplayName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Neznámá kategorie: " + name);
    }
}

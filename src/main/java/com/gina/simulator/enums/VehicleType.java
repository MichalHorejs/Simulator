package com.gina.simulator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VehicleType {
    A(VehicleCategory.VOZIDLA, "A - autobus"),
    AJ(VehicleCategory.VOZIDLA, "AJ - automobilový jeřáb"),
    ANK(VehicleCategory.VOZIDLA, "ANK - automobilový nosič kontejnerů"),
    AP(VehicleCategory.VOZIDLA, "AP - automobilová plošina"),
    AZ(VehicleCategory.VOZIDLA, "AZ - automobilový žebřík"),
    CAS(VehicleCategory.VOZIDLA, "CAS - cisternová automobilová stříkačka"),
    CT(VehicleCategory.VOZIDLA, "CT - terenní čtyřkolka"),
    DA(VehicleCategory.VOZIDLA, "DA - dopravní automobil"),
    KCE(VehicleCategory.KONTEJNERY, "KCE - čerpací"),
    KCH(VehicleCategory.KONTEJNERY, "KCH - chemický"),
    KEC(VehicleCategory.KONTEJNERY, "KEC - elektrocentrála"),
    KPLH(VehicleCategory.KONTEJNERY, "KPLH - plynový hasicí (CO2)"),
    KPP(VehicleCategory.KONTEJNERY, "KPP - první pomoci"),
    KR(VehicleCategory.KONTEJNERY, "KR - ropný"),
    KSK(VehicleCategory.KONTEJNERY, "KSK - skříňový"),
    KT(VehicleCategory.KONTEJNERY, "KT - technický"),
    KTY(VehicleCategory.KONTEJNERY, "KTY - týlový"),
    LOD(VehicleCategory.VOZIDLA, "LOD - člun, loď"),
    NA(VehicleCategory.VOZIDLA, "NA - nákladní automobil"),
    OA(VehicleCategory.VOZIDLA, "OA - osobní automobil"),
    PILA(VehicleCategory.OSTATNI, "PILA - motorová pila"),
    PL(VehicleCategory.PRIVESY, "PL - lodní"),
    PN(VehicleCategory.PRIVESY, "PN - nákladní"),
    POD(VehicleCategory.PRIVESY, "POD - odtahový"),
    PPLA(VehicleCategory.VOZIDLA, "PPLA - protiplynový automobil"),
    T(VehicleCategory.VOZIDLA, "T - traktor"),
    TA(VehicleCategory.VOZIDLA, "TA - technický automobil"),
    UA(VehicleCategory.VOZIDLA, "UA - užitkový automobil"),
    VA(VehicleCategory.VOZIDLA, "VA - vyšetřovací automobil"),
    VEA(VehicleCategory.VOZIDLA, "VEA - velitelský automobil"),
    VYA(VehicleCategory.VOZIDLA, "VYA - vyprošťovací automobil");

    private final VehicleCategory category;
    private final String displayName;

    public static VehicleType fromDisplayName(String name) {
        for (VehicleType type : values()) {
            if (type.getDisplayName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Neznámý typ vozidla: " + name);
    }
}
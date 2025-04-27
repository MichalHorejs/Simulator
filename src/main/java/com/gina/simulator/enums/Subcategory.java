package com.gina.simulator.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Subcategory {

    PROVOZNICH_KAPALIN(Category.UNIK_NEBEZPECNYCH_LATEK, "Provozních kapalin"),
    SE_ZRANENIM(Category.DOPRAVNI_NEHODA, "Se zraněním"),
    TRANSPORT_PACIENTA(Category.TECHNICKA_POMOC, "Transport pacienta"),
    CERPANI_VODY(Category.TECHNICKA_POMOC, "Čerpání vody"),
    UVOLNENI_KOMUNIKACE_ODTAZENI(Category.DOPRAVNI_NEHODA, "Uvolnění komunikace, odtažení"),
    AED(Category.ZACHRANA_OSOB_A_ZVIRAT, "AED"),
    PLANY_POPLACH(Category.PLANY_POPLACH, "Planý poplach"),
    OTEVRENI_UZAVRENYCH_PROSTOR(Category.TECHNICKA_POMOC, "Otevření uzavřených prostor"),
    ODSTRANENI_NEBEZPECNYCH_STAVU(Category.TECHNICKA_POMOC, "Odstranění nebezpečných stavů"),
    NA_DO_VODNI_PLOCHY(Category.UNIK_NEBEZPECNYCH_LATEK, "Na (do) vodní plochu(y)"),
    VYPROSTENI_OSOB(Category.DOPRAVNI_NEHODA, "Vyproštění osob"),
    SPOLUPRACE_SE_SLOZKAMI_IZS(Category.TECHNICKA_POMOC, "Spolupráce se složkami IZS"),
    UKLID_VOZOVKY(Category.DOPRAVNI_NEHODA, "Úklid vozovky"),
    ODPAD_OSTATNI(Category.POZAR, "Odpad, ostatní"),
    LIKVIDACE_OBTIZNEHO_HMYZU(Category.TECHNICKA_POMOC, "Likvidace obtížného hmyzu"),
    UZAVRENE_PROSTORY_VYTAH(Category.ZACHRANA_OSOB_A_ZVIRAT, "Uzavřené prostory, výtah"),
    DOPRAVNI_PROSTREDKY(Category.POZAR, "Dopravní prostředky"),
    MERENI_KONCENTRACI(Category.UNIK_NEBEZPECNYCH_LATEK, "Měření koncentrací"),
    MONITORING(Category.TECHNICKA_POMOC, "Monitoring"),
    ODSTRANENI_STROMU(Category.TECHNICKA_POMOC, "Odstranění stromu"),
    TRAFOSTANICE_ROZVODNY(Category.POZAR, "Trafostanice, rozvodny"),
    NIZKE_BUDOVY(Category.POZAR, "Nízké budovy"),
    DESTRUKCE_OBJEKTU(Category.TECHNICKA_POMOC, "Destrukce objektu"),
    VYSKOVE_BUDOVY(Category.POZAR, "Výškové budovy"),
    POPELNICE_KONTEJNER(Category.POZAR, "Popelnice, kontejner"),
    KOMIN(Category.POZAR, "Komín"),
    Z_HLOUBKY(Category.ZACHRANA_OSOB_A_ZVIRAT, "Z hloubky"),
    ZELEZNICNI(Category.DOPRAVNI_NEHODA, "Železniční"),
    JINE(Category.OSTATNI_MIMORADNA_UDALOST, "Jiné"),
    DO_OVZDUSI(Category.UNIK_NEBEZPECNYCH_LATEK, "Do ovzduší"),
    KULNY_PRISTRESKY_CHATKY(Category.POZAR, "Kůlny, přístřešky. chatky"),
    ODSTRANOVANI_PREKAZEK(Category.TECHNICKA_POMOC, "Odstraňování překážek"),
    NA_POZEMNI_KOMUNIKACI(Category.UNIK_NEBEZPECNYCH_LATEK, "Na pozemní komunikaci"),
    ZATIM_NEURCENO(Category.JINE_ZATIM_NEURCENO, "Zatím neurčeno"),
    ELEKTROINSTALACE(Category.POZAR, "Elektroinstalace"),
    Z_VYSKY(Category.ZACHRANA_OSOB_A_ZVIRAT, "Z výšky"),
    TECHNOLOGICKY_TEST(Category.JINE_ZATIM_NEURCENO, "Technologický test"),
    SKLADY(Category.POZAR, "Sklady"),
    POLNI_POROST_TRAVA(Category.POZAR, "Polní porost, tráva"),
    PRUMYSLOVE_OBJEKTY(Category.POZAR, "Průmyslové objekty"),
    LESNI_POROST(Category.POZAR, "Lesní porost"),
    ZEMEDELSKE_OBJEKTY(Category.POZAR, "Zemědělské objekty"),
    ODSTRANENI_SNEHU_LEDU(Category.TECHNICKA_POMOC, "Odstranění sněhu, ledu"),
    ZASYPANE_ZAVALENE(Category.ZACHRANA_OSOB_A_ZVIRAT, "Zasypané,zavalené"),
    Z_VODY(Category.ZACHRANA_OSOB_A_ZVIRAT, "Z vody"),
    SHROMAZDISTE_OSOB(Category.POZAR, "Shromaždiště osob"),
    OSTATNI_FORMALNE_ZALOZENA_UDALOST(Category.FORMALNE_ZALOZENA_UDALOST, "Ostatní formálně založená událost"),
    NAHRADA_NEFUNKCNIHO_ZARIZENI(Category.TECHNICKA_POMOC, "Náhrada nefunkčního zařízení"),
    DO_PUDY(Category.UNIK_NEBEZPECNYCH_LATEK, "Do půdy"),
    VYSKOVE_PRACE(Category.TECHNICKA_POMOC, "Výškové práce"),
    ODPAD(Category.POZAR, "Odpad"),
    KOLEJOVA_VOZIDLA(Category.POZAR, "Kolejová vozidla"),
    LETECKA(Category.DOPRAVNI_NEHODA, "Letecká");

    private final Category category;
    private final String displayName;

    public static Subcategory fromDisplayName(String name) {
        for (Subcategory subcategory : values()) {
            if (subcategory.getDisplayName().equalsIgnoreCase(name)) {
                return subcategory;
            }
        }
        throw new IllegalArgumentException("Unknown subcategory: " + name);
    }
}
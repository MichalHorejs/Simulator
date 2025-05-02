package com.gina.simulator.integration.Osm;

import com.fasterxml.jackson.databind.JsonNode;
import com.gina.simulator.integration.features.Building;
import com.gina.simulator.integration.features.Natural;
import com.gina.simulator.integration.features.Node;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
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

    public Node parseNode(JsonNode el){
        String lon = el.get("lon").asText();
        String lat = el.get("lat").asText();
        return new Node(lon, lat);
    }

    public Building parseBuilding(JsonNode tags, JsonNode nodes){
        Building building = new Building();
        building.setType(resolveBuildingType(tags.path("building:ruian:type").asText("není známo")));
        building.setLevels(tags.path("building:levels").asText("není známo"));
        building.setFlats(tags.path("building:flats").asText("není známo"));
        building.setHouseNumber(tags.path("ref:ruian:building").asText("není známo"));

        building.setNodeId(nodes.path(0).asText("není známo"));

        return building;
    }

    public Natural parseNatural(JsonNode tags, JsonNode nodes){
        Natural natural = new Natural();
        natural.setType(tags.path("natural").asText("není známo"));

        natural.setNodeId(nodes.path(0).asText("není známo"));

        return natural;
    }

    private String resolveBuildingType(String typeNumber){
        return OsmParser.RUIAN_TYPE_DESCRIPTIONS.getOrDefault(typeNumber, "Neznámý typ stavby");
    }
}

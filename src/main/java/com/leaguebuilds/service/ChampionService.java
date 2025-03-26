package com.leaguebuilds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leaguebuilds.controller.ChampionController;
import com.leaguebuilds.model.Champion;
import com.leaguebuilds.model.Item;
import com.leaguebuilds.utils.Utils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class ChampionService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final HashMap<String, Champion> champions = new HashMap<>();

    public ChampionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @PostConstruct
    public void loadChampions() {
        String url = "https://ddragon.leagueoflegends.com/cdn/15.6.1/data/en_US/champion.json";
        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode dataNode = root.path("data");
            dataNode.fields().forEachRemaining(entry -> {

                String id = entry.getKey();
                JsonNode championNode = entry.getValue();

                ((ObjectNode) championNode).put("id", id);
                try {
                    Champion champion = objectMapper.treeToValue(championNode, Champion.class);
                    champions.put(champion.getId(), champion);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

            });
        } catch (Exception e) {
            throw new RuntimeException("Error parsing item data: " + e.getMessage());
        }
    }

    public HashMap<String, Champion> getChampions() {
        return champions;
    }
}

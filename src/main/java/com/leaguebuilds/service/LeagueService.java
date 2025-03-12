package com.leaguebuilds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leaguebuilds.model.Item;
import com.leaguebuilds.utils.ItemUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LeagueService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String RIOT_API_URL = "https://ddragon.leagueoflegends.com/cdn/15.5.1/data/en_US/item.json";
    private HashMap<Integer, Item> items = new HashMap<>();

    public LeagueService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Obtiene un objeto Item específico por su ID desde la API de Riot Games.
     * @param id El identificador del ítem que se desea obtener.
     * @return El objeto Item correspondiente al ID proporcionado.
     * @throws RuntimeException Si ocurre un error al analizar los datos del ítem.
     */
    public Item getItemById(String id) {
        String response = restTemplate.getForObject(RIOT_API_URL, String.class);
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode itemNode = root.path("data").path(id);
            ((ObjectNode) itemNode).put("id", id);
            return objectMapper.treeToValue(itemNode, Item.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing item data: " + e.getMessage());
        }
    }

    /**
     * Carga los ítems desde la API de Riot Games y los almacena en una lista.
     * Este método se ejecuta automáticamente después de que el contenedor de Spring haya inicializado el bean.
     */
    @PostConstruct
    public void loadItems() {
        String response = restTemplate.getForObject(RIOT_API_URL, String.class);
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode dataNode = root.path("data");
            dataNode.fields().forEachRemaining(entry -> {
                String id = entry.getKey();
                JsonNode itemNode = entry.getValue();
                ((ObjectNode) itemNode).put("id", id);
                try {
                    Item item = objectMapper.treeToValue(itemNode, Item.class);
                    items.put(Integer.valueOf(item.getId()), item);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Error parsing item data: " + e.getMessage());
        }
    }

    public HashMap<Integer, Item> getItems() {
        return items;
    }
}

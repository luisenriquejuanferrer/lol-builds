package com.leaguebuilds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leaguebuilds.model.Item;
import com.leaguebuilds.utils.Utils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final HashMap<Integer, Item> items = new HashMap<>();

    public ItemService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Carga los ítems desde la API de Riot Games y los almacena en una lista.
     * Esta función se ejecuta automáticamente después de que el contenedor de Spring haya inicializado el bean.
     */
    @PostConstruct
    public void loadItems() {
        String response = restTemplate.getForObject(Utils.RIOT_API_ITEM_URL, String.class);
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode dataNode = root.path("data");
            dataNode.fields().forEachRemaining(entry -> {

                String id = entry.getKey();
                JsonNode itemNode = entry.getValue();

                if (itemNode.path("maps").path("11").asBoolean() && itemNode.path("gold").path("purchasable").asBoolean()) {
                    ((ObjectNode) itemNode).put("id", id);
                    int totalGold = itemNode.path("gold").path("total").asInt();
                    ((ObjectNode) itemNode).put("totalGold", totalGold);
                    try {
                        Item item = objectMapper.treeToValue(itemNode, Item.class);
                        items.put(Integer.valueOf(item.getId()), item);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }

            });
        } catch (Exception e) {
            throw new RuntimeException("Error parsing item data: " + e.getMessage());
        }
    }

    /**
     * Elimina los ítems duplicados en el HashMap `items` basándose en el nombre del ítem.
     * Esta función se ejecuta automáticamente después de que el contenedor de Spring haya inicializado el bean.
     *
     * Utiliza un `Set` para rastrear los nombres de los ítems que ya se han visto y filtra los duplicados.
     * Luego, limpia el HashMap original y lo vuelve a llenar con los ítems únicos.
     */
    @PostConstruct
    public void deleteDuplicatedItems() {
        Set<String> seenNames = new HashSet<>();

        Map<Integer, Item> filteredItems = items.entrySet()
                .stream()
                .filter(entry -> seenNames.add(entry.getValue().getName()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        items.clear();
        items.putAll(filteredItems);
    }

    public Item getItemById(String id) {
        return items.get(Integer.valueOf(id));
    }

    public HashMap<Integer, Item> getItems() {
        return items;
    }
}

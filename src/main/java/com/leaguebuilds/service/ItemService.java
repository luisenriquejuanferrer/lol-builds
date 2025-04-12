package com.leaguebuilds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.leaguebuilds.model.Item;
import com.leaguebuilds.utils.Utils;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Getter
    private final HashMap<Integer, Item> items = new HashMap<>();

    public ItemService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void loadItems() {
        String response = restTemplate.getForObject(Utils.getRiotApiItemURL(), String.class);
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
        deleteDuplicatedItems();
    }

    public void deleteDuplicatedItems() {
        Set<String> seenNames = new HashSet<>();

        Map<Integer, Item> filteredItems = items.entrySet()
                .stream()
                .filter(entry -> seenNames.add(entry.getValue().getName()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        items.clear();
        items.putAll(filteredItems);
        uploadItemsToDatabase();
    }

    public void uploadItemsToDatabase() {
        Firestore db = FirestoreClient.getFirestore();

        items.values().forEach(item -> {
            db.collection("lolbuilder")
                    .document(Utils.getRiotApiVersion())
                    .collection("items")
                    .document(item.getId())
                    .set(item);
        });
    }

    public List<Item> loadItemsFromDatabase() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        Iterable<DocumentReference> refs = db.collection("lolbuilder").listDocuments();
        List<String> versions = new ArrayList<>();

        for (DocumentReference ref : refs) {
            String id = ref.getId();
            if (id.matches("\\d+\\.\\d+\\.\\d+")) {
                versions.add(id);
            }
        }

        versions.sort(Comparator.reverseOrder());

        if (versions.isEmpty() || !Objects.equals(versions.getFirst(), Utils.getRiotApiVersion())) {
            loadItems();
        }

        ApiFuture<QuerySnapshot> future = db.collection("lolbuilder")
                .document(Utils.getRiotApiVersion())
                .collection("items")
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Item> items = new ArrayList<>();

        for (QueryDocumentSnapshot doc : documents) {
            items.add(doc.toObject(Item.class));
        }
        return items;
    }
}

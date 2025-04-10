package com.leaguebuilds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.leaguebuilds.model.Champion;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ChampionService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Getter
    private final HashMap<String, Champion> champions = new HashMap<>();

    public ChampionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }


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

    public void uploadChampionsToFirestore() {
        Firestore db = FirestoreClient.getFirestore();

        champions.values().forEach(champion -> {
            db.collection("lolbuilder")
                    .document("15.6.1")
                    .collection("champions")
                    .document(champion.getId())
                    .set(champion);
        });
    }

    @PostConstruct
    public List<Champion> loadChampionsFromFirestore() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = db.collection("lolbuilder")
                .document("15.6.1")
                .collection("champions")
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Champion> champions = new ArrayList<>();

        for (QueryDocumentSnapshot doc : documents) {
            champions.add(doc.toObject(Champion.class));
        }
        return champions;
    }
}

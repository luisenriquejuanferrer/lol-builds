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
import com.leaguebuilds.model.Champion;
import com.leaguebuilds.utils.Utils;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
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

    // @PostConstruct
    public void loadChampions() {
        String response = restTemplate.getForObject(Utils.getRIOT_API_CHAMPION_URL(), String.class);

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
        uploadChampionsToFirestore();
    }

    public void uploadChampionsToFirestore() {
        Firestore db = FirestoreClient.getFirestore();

        champions.values().forEach(champion -> {
            db.collection("lolbuilder")
                    .document(Utils.getRIOT_API_VERSION())
                    .collection("champions")
                    .document(champion.getId())
                    .set(champion);
        });
    }

    public List<Champion> getChampionsFromFirestore() throws ExecutionException, InterruptedException {
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

        if (versions.isEmpty() || !Objects.equals(versions.getFirst(), Utils.getRIOT_API_VERSION())) {
            loadChampions();
        }

        ApiFuture<QuerySnapshot> future = db.collection("lolbuilder")
                .document(Utils.getRIOT_API_VERSION())
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

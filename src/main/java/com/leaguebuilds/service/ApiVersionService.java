package com.leaguebuilds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaguebuilds.utils.Utils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ApiVersionService {
    private final RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    private String[] apiVersions = new String[]{};

    public ApiVersionService(RestTemplate restTemplate, ItemService itemService) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public String[] loadApiVersions() {
        try {
            String response = restTemplate.getForObject(Utils.RIOT_API_VERSION_URL, String.class);
            apiVersions = objectMapper.readValue(response, String[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return apiVersions;
    }

    public void setLatestApiVersion() {
        String latestApiVersion = apiVersions[0];

        if (!Objects.equals(latestApiVersion, Utils.getRiotApiVersion())) {
            Utils.setRiotApiVersion(latestApiVersion);
        }
    }

    @PostConstruct
    public void init() {
        loadApiVersions();
        setLatestApiVersion();
    }
}

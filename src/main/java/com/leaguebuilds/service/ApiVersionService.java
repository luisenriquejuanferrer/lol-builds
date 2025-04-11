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

    private String latestApiVersion;
    private String[] apiVersions = new String[]{};

    public ApiVersionService(RestTemplate restTemplate, ItemService itemService) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public String[] getApiVersions() {
        try {
            String response = restTemplate.getForObject(Utils.RIOT_API_VERSION_URL, String.class);
            apiVersions = objectMapper.readValue(response, String[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return apiVersions;
    }

    public void setLatestApiVersion() {
        getApiVersions();
        latestApiVersion = apiVersions[0];
    }

    @PostConstruct
    public void init() {
        setLatestApiVersion();
        if (!Objects.equals(latestApiVersion, Utils.getRIOT_API_VERSION())) {
            Utils.setRIOT_API_VERSION(latestApiVersion);
            System.out.println(Utils.getRIOT_API_VERSION());
        }
    }
}

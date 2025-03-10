package com.leaguebuilds.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LeagueService {

    private  final RestTemplate restTemplate;

    public LeagueService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}

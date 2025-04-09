package com.leaguebuilds.controller;

import com.leaguebuilds.model.Champion;
import com.leaguebuilds.service.ChampionService;
import com.leaguebuilds.utils.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(Utils.BASE_API_URL)
public class ChampionController {

    private ChampionService championService;

    public ChampionController(ChampionService championService) {
        this.championService = championService;
    }

    @GetMapping("/champions")
    public HashMap<String, Champion> getChampions() {
        return championService.getChampions();
    }

    @GetMapping("/champions/post")
    public void uploadChampionsToFirestore(){
        championService.uploadChampionsToFirestore();
    }
}

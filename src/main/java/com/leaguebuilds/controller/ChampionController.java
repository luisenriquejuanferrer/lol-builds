package com.leaguebuilds.controller;

import com.leaguebuilds.model.Champion;
import com.leaguebuilds.service.ChampionService;
import com.leaguebuilds.utils.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(Utils.BASE_API_URL)
public class ChampionController {

    private final ChampionService championService;

    public ChampionController(ChampionService championService) {
        this.championService = championService;
    }

    @GetMapping("/champions/cachedChampions")
    public HashMap<String, Champion> getCachedChampions() {
        return championService.getChampions();
    }

    @GetMapping("/champions/loadFromDatabase")
    public List<Champion> loadChampionsFromDatabase() throws ExecutionException, InterruptedException {
        return championService.loadChampionsFromDatabase();
    }

    @PostMapping("/champions/uploadToDatabase")
    public void uploadChampionsToDatabase() {
        championService.uploadChampionsToDatabase();
    }
}

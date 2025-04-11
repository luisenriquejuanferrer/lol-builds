package com.leaguebuilds.controller;

import com.leaguebuilds.model.Champion;
import com.leaguebuilds.service.ChampionService;
import com.leaguebuilds.utils.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(Utils.BASE_API_URL)
public class ChampionController {

    private ChampionService championService;

    public ChampionController(ChampionService championService) {
        this.championService = championService;
    }

//    @GetMapping("/champions")
//    public HashMap<String, Champion> getChampions() {
//        return championService.getChampions();
//    }

    @GetMapping("/champions")
    public List<Champion> getChampionsFromFirestore() throws ExecutionException, InterruptedException {
        return championService.getChampionsFromFirestore();
    }

    @PostMapping("/champions/uploadChampions")
    public void uploadChampionsToFirestore() {
        championService.uploadChampionsToFirestore();
    }
}

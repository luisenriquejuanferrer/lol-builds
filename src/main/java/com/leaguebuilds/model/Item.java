package com.leaguebuilds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leaguebuilds.utils.Utils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String id;
    private String name;
    private int totalGold;
    private List<String> tags = new ArrayList<>();

    //Esto habrá que cambiarlo para que coja la constant de RIOT_API_IMAGE_URL + id + ".png"
    public String getImage() {
        return Utils.RIOT_API_ITEM_IMAGE_URL + id + ".png";
    }
}
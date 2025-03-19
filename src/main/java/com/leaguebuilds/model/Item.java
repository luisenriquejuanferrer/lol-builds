package com.leaguebuilds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String id;
    private String name;
    private int totalGold;

    //Esto habrá que cambiarlo para que coja la constant de RIOT_API_IMAGE_URL + id + ".png"
    public String getImage() {
        return "https://ddragon.leagueoflegends.com/cdn/15.5.1/img/item/" + id + ".png";
    }
}
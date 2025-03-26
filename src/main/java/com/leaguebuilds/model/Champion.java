package com.leaguebuilds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Champion {
    private String id;

    public String getImage() {
        return "https://ddragon.leagueoflegends.com/cdn/15.6.1/img/champion/" + id + ".png";
    }

}

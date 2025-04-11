package com.leaguebuilds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leaguebuilds.utils.Utils;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Champion {
    private String id;

    public String getImage() {
        return Utils.RIOT_API_CHAMPION_IMAGE_URL + id + ".png";
    }

}

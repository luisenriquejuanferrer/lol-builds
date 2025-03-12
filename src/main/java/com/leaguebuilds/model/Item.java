package com.leaguebuilds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String id;
    @JsonPropertyOrder({"name", "plaintext"})
    private String name;
    //private String plaintext;
    //private String description;
}

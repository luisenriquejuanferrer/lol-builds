package com.leaguebuilds.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class Utils {
//    @Value("${RIOT_API_VERSION}")
//    private String RIOT_API_VERSION;
    private static final String RIOT_API_VERSION = "15.7.1";

    public static final String BASE_API_URL = "/api/lol";
    public static final String RIOT_API_BASE_URL = "https://ddragon.leagueoflegends.com/cdn";

    public static String RIOT_API_ITEM_URL = RIOT_API_BASE_URL+ "/" + RIOT_API_VERSION + "/data/en_US/item.json";
    public static String RIOT_API_CHAMPION_URL = RIOT_API_BASE_URL+ "/" + RIOT_API_VERSION + "/data/en_US/champion.json";

    public static String RIOT_API_ITEM_IMAGE_URL = RIOT_API_BASE_URL+ "/" + RIOT_API_VERSION + "/img/item/";
    public static String RIOT_API_CHAMPION_IMAGE_URL = RIOT_API_BASE_URL + "/" + RIOT_API_VERSION + "/img/champion/";
}


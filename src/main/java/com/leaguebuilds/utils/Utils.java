package com.leaguebuilds.utils;

public class Utils {
    private static String RIOT_API_VERSION;

    public static final String BASE_API_URL = "/lolbuilds";
    public static final String RIOT_API_BASE_URL = "https://ddragon.leagueoflegends.com";

    public static String RIOT_API_VERSION_URL = RIOT_API_BASE_URL + "/api/versions.json";

    public static String getRiotApiItemURL() {
        return RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/data/en_US/item.json";
    }

    public static String getRiotApiChampionURL() {
        return RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/data/en_US/champion.json";
    }

    public static String getRiotApiItemImageURL() {
        return RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/img/item/";
    }

    public static String getRiotApiChampionImageURL() {
        return RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/img/champion/";
    }

    public static String getRiotApiVersion() {
        return RIOT_API_VERSION;
    }

    public static void setRiotApiVersion(String version) {
        RIOT_API_VERSION = version;
    }
}



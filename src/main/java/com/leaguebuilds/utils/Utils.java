package com.leaguebuilds.utils;

public class Utils {
    private static String RIOT_API_VERSION;

    public static final String BASE_API_URL = "/api/lol";
    public static final String RIOT_API_BASE_URL = "https://ddragon.leagueoflegends.com";

    //public static String RIOT_API_ITEM_URL = RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/data/en_US/item.json";
    //public static String RIOT_API_CHAMPION_URL = RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/data/en_US/champion.json";
    public static String RIOT_API_VERSION_URL = RIOT_API_BASE_URL + "/api/versions.json";

    //public static String RIOT_API_ITEM_IMAGE_URL = RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/img/item/";
    //public static String RIOT_API_CHAMPION_IMAGE_URL = RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/img/champion/";

    public static String getRIOT_API_ITEM_URL() {
        return RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/data/en_US/item.json";
    }

    public static String getRIOT_API_CHAMPION_URL() {
        return RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/data/en_US/champion.json";
    }

    public static String getRIOT_API_ITEM_IMAGE_URL() {
        return RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/img/item/";
    }

    public static String getRIOT_API_CHAMPION_IMAGE_URL() {
        return RIOT_API_BASE_URL + "/cdn/" + RIOT_API_VERSION + "/img/champion/";
    }

    public static String getRIOT_API_VERSION() {
        return RIOT_API_VERSION;
    }

    public static void setRIOT_API_VERSION(String version) {
        RIOT_API_VERSION = version;
    }
}


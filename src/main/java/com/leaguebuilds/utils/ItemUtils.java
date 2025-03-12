package com.leaguebuilds.utils;

import com.leaguebuilds.model.Item;
import org.apache.commons.text.StringEscapeUtils;

import java.util.HashMap;
import java.util.TreeMap;

public class ItemUtils {

    // No funciona correctamente, habrá que revisar el código
    public static String formatDescription(String unformattedDescription) {
        if (unformattedDescription == null) {
            return "";
        }

        String decoded = StringEscapeUtils.unescapeJava(unformattedDescription);
        String cleaned = decoded.replaceAll("\\<.*?\\>", "");
        cleaned = cleaned.replaceAll("(\\d+)([A-Za-z])", "$1 $2");

        return cleaned.trim().replaceAll("\\s+", " ");
    }

    // Aquí habrá que ir iterando por cada Item y cambiando el id empezando por 1
    public static String formatId(String unformattedId) {
        return "";
    }

    // Quizas tengamos que ordenar los items por id
    public static HashMap<Integer, Item> sortedItems(HashMap<Integer, Item> items) {
        TreeMap<Integer, Item> sorted = new TreeMap<>(items);
        return new HashMap<>(sorted);
    }
}

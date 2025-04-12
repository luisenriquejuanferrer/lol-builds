package com.leaguebuilds.controller;

import com.leaguebuilds.model.Item;
import com.leaguebuilds.service.ItemService;
import com.leaguebuilds.utils.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(Utils.BASE_API_URL)
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items/cachedItems")
    public HashMap<Integer, Item> getCachedItems() {
        return itemService.getItems();
    }

    @GetMapping("/items/loadFromDatabase")
    public List<Item> loadItemsFromDatabase() throws ExecutionException, InterruptedException {
        return itemService.loadItemsFromDatabase();
    }

    @PostMapping("/items/uploadToDatabase")
    public void uploadItemsToDatabase() {
        itemService.uploadItemsToDatabase();
    }
}

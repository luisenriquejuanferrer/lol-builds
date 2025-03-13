package com.leaguebuilds.controller;

import com.leaguebuilds.model.Item;
import com.leaguebuilds.service.ItemService;
import com.leaguebuilds.utils.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(Utils.BASE_API_URL)
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping({"/item/{id}"})
    public Item getItemById(@PathVariable String id) {
        return itemService.getItemById(id);
    }

    @GetMapping("/items")
    public HashMap<Integer, Item> getItems() {
        return itemService.getItems();
    }
}

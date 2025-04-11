package com.leaguebuilds.controller;

import com.leaguebuilds.model.Item;
import com.leaguebuilds.service.ItemService;
import com.leaguebuilds.utils.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(Utils.BASE_API_URL)
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

//    @GetMapping("/items")
//    public HashMap<Integer, Item> getItems() {
//        return itemService.getItems();
//    }

    @GetMapping("/items")
    public List<Item> getItemsFromFirestore() throws ExecutionException, InterruptedException {
        return itemService.getItemsFromFirestore();
    }

    @PostMapping("/items/uploadItems")
    public void uploadItemsToFirestore() {
        itemService.uploadItemsToFirestore();
    }
}

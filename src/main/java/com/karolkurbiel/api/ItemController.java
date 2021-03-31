package com.karolkurbiel.api;

import com.karolkurbiel.model.item.Item;
import com.karolkurbiel.model.item.ItemCondition;
import com.karolkurbiel.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/item")
@RestController
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/add")
    public ResponseEntity<ItemRequest> addNewItem(@RequestParam("name") String name,@RequestParam("description") String description,@RequestParam("condition") ItemCondition itemCondition,@RequestParam("price") BigDecimal price,@RequestParam("location") String location) {
        Item newItem = itemService.addNewItem(name, description, itemCondition, price, location);

        System.out.println("\n" + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ISO_TIME) + " --- addNewItem(): " + newItem.getItemID());

        return ResponseEntity.ok(ItemRequest.fromItem(newItem));
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<ItemRequest> modifyItem(@PathVariable("id") UUID itemID, @RequestParam("name") String name,@RequestParam("description") String description,@RequestParam("condition") ItemCondition itemCondition,@RequestParam("price") BigDecimal price,@RequestParam("location") String location) {
        Item modifiedItem = itemService.modifyItem(itemID, name, description, itemCondition, price, location);

        System.out.println("\n" + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ISO_TIME) + " --- modifyItem(): " + itemID);

        return ResponseEntity.ok(ItemRequest.fromItem(modifiedItem));
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteItem(@PathVariable("id") UUID itemID) {
        boolean status = itemService.deleteItem(itemID);

        System.out.println("\n" + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ISO_TIME) + " --- deleteItem(): " + itemID);

        return status;
    }

    @PostMapping("/image/add/{id}")
    public ResponseEntity<ItemRequest> addImageUrlToItem(@PathVariable("id") UUID itemID, @RequestParam("imageUrl") String imageUrl) {
        Item processedItem = itemService.addImageUrlToItem(itemID, imageUrl);

        System.out.println("\n" + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ISO_TIME) + " --- addImageUrlToItem: " + itemID);

        return ResponseEntity.ok(ItemRequest.fromItem(processedItem));
    }

    @PutMapping("/image/modify/{id}")
    public ResponseEntity<ItemRequest> modifyImageUrl(@PathVariable("id") UUID itemID,@RequestParam("oldImageUrl") String oldImageUrl,@RequestParam("newImageUrl") String newImageUrl) {
        Item processedItem = itemService.modifyImageUrl(itemID, oldImageUrl, newImageUrl);

        System.out.println("\n" + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ISO_TIME) + " --- modifyImageUrl(): " + itemID);

        return ResponseEntity.ok(ItemRequest.fromItem(processedItem));
    }

    @DeleteMapping("/image/delete/{id}")
    public boolean deleteImageUrl(@PathVariable("id") UUID itemID,@RequestParam("oldImageUrl") String oldImageUrl) {
        boolean deleteFlag = itemService.deleteImageUrl(itemID, oldImageUrl);

        System.out.println("\n" + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ISO_TIME) + " --- deleteImageUrl(): " + itemID);

        return deleteFlag;
    }

    @GetMapping("/get/all")
    public ResponseEntity<MapRequest> getItemList() {
        Map<UUID, Item> itemDB = itemService.getItemList();

        System.out.println("\n" + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ISO_TIME) + " --- getItemList()");

        return ResponseEntity.ok(new MapRequest(itemDB));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ItemRequest> getCreatedItem(@PathVariable("id") UUID itemID) {
        Item requestedItem = itemService.getCreatedItem(itemID);

        System.out.println("\n" + LocalDate.now() + " " + LocalTime.now().format(DateTimeFormatter.ISO_TIME) + "--- getCreatedItem(): " + itemID);

        return ResponseEntity.ok(ItemRequest.fromItem(requestedItem));
    }

    //******************************************************************************************************************

    private static class MapRequest {
        private final Map<UUID, Item> itemDB;

        public MapRequest(Map<UUID, Item> itemDB) {
            this.itemDB = itemDB;
        }

        public Map<UUID, Item> getItemDB() {
            return itemDB;
        }
    }

    private static class ItemRequest {
        private final UUID itemID;
        private final String name;
        private final String description;
        private final ItemCondition itemCondition;
        private final BigDecimal price;
        private final String location;
        private final LocalDate postDate;
        private final List<String> offerImageUrlList;

        public ItemRequest(UUID itemID, String name, String description, ItemCondition itemCondition, BigDecimal price, String location, LocalDate postDate, List<String> offerImageUrlList) {
            this.itemID = itemID;
            this.name = name;
            this.description = description;
            this.itemCondition = itemCondition;
            this.price = price;
            this.location = location;
            this.postDate = postDate;
            this.offerImageUrlList = offerImageUrlList;
        }

        public static ItemController.ItemRequest fromItem(Item item) {
            return new ItemController.ItemRequest(item.getItemID(),
                    item.getName(),
                    item.getDescription(),
                    item.getItemCondition(),
                    item.getPrice(),
                    item.getLocation(),
                    item.getPostDate(),
                    item.getOfferImageUrlList());
        }

        public UUID getItemID() {
            return itemID;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public ItemCondition getItemCondition() {
            return itemCondition;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public String getLocation() {
            return location;
        }

        public LocalDate getPostDate() {
            return postDate;
        }

        public List<String> getOfferImageUrlList() {
            return offerImageUrlList;
        }
    }
}

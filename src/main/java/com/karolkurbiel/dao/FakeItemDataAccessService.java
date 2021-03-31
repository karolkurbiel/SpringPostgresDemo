package com.karolkurbiel.dao;

import com.karolkurbiel.exceptions.ItemAlreadyExistsException;
import com.karolkurbiel.exceptions.NonExistingItemIdException;
import com.karolkurbiel.model.item.Item;
import com.karolkurbiel.model.item.ItemCondition;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository("FakeItemService")
public class FakeItemDataAccessService implements ItemDao {

    private final Map<UUID, Item> storeItemsDB = new HashMap<>();

    @Override
    public Item addNewItem(String name, String description, ItemCondition itemCondition, BigDecimal price, String location) {
        Item newItem = new Item(name, description, itemCondition, price, location);
        if(storeItemsDB.containsKey(newItem.getItemID())) {
            throw new ItemAlreadyExistsException(newItem.getItemID());
        } else {
            storeItemsDB.put(newItem.getItemID(), newItem);
            return newItem;
        }
    }

    @Override
    public Item modifyItem(UUID itemID, String name, String description, ItemCondition itemCondition, BigDecimal price, String location) {
        if(storeItemsDB.containsKey(itemID)) {
            Item processedItem = storeItemsDB.get(itemID);
            processedItem.modifyProperties(name, description, itemCondition, price, location);
            return processedItem;
        } else {
            throw new NonExistingItemIdException(itemID);
        }
    }

    @Override
    public boolean deleteItem(UUID itemID) {
        Item removedItem = storeItemsDB.remove(itemID);
        if(removedItem == null) {
            throw new NonExistingItemIdException(itemID);
        } else {
            return true;
        }
    }

    @Override
    public Item addImageUrlToItem(UUID itemID, String imageUrl) {
        if(storeItemsDB.containsKey(itemID)) {
            storeItemsDB.get(itemID).addNewItemImage(imageUrl);
            return storeItemsDB.get(itemID);
        } else {
            throw new NonExistingItemIdException(itemID);
        }
    }

    @Override
    public Item modifyImageUrl(UUID itemID, String urlToModify, String newImageUrl) {
        if(storeItemsDB.containsKey(itemID)) {
            storeItemsDB.get(itemID).modifyItemImage(urlToModify, newImageUrl);
            return storeItemsDB.get(itemID);
        } else {
            throw new NonExistingItemIdException(itemID);
        }
    }

    @Override
    public boolean deleteImageUrl(UUID itemID, String imageUrl) {
        if(storeItemsDB.containsKey(itemID)) {
            storeItemsDB.get(itemID).deleteItemImage(imageUrl);
            return true;
        } else {
            throw new NonExistingItemIdException(itemID);
        }
    }

    @Override
    public Map<UUID, Item> getItemList() {
        Map<UUID, Item> returnedMap = new HashMap<>();
        for(Map.Entry<UUID, Item> entry : storeItemsDB.entrySet()) {
            Item processedItem = new Item(entry.getValue());
            returnedMap.put(processedItem.getItemID(), processedItem);
        }

        return returnedMap;
    }

    @Override
    public Item getCreatedItem(UUID itemID) {
        if(storeItemsDB.containsKey(itemID)) {
            return new Item(storeItemsDB.get(itemID));
        } else {
            throw new NonExistingItemIdException(itemID);
        }
    }
}

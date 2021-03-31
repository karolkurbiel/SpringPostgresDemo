package com.karolkurbiel.dao;

import com.karolkurbiel.model.item.Item;
import com.karolkurbiel.model.item.ItemCondition;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface ItemDao {

    Item addNewItem(String name, String description, ItemCondition itemCondition, BigDecimal price, String location);
    Item modifyItem(UUID itemID, String name, String description, ItemCondition itemCondition, BigDecimal price, String location);
    boolean deleteItem(UUID itemID);
    Item getCreatedItem(UUID itemID);

    Item addImageUrlToItem(UUID itemID, String imageUrl);
    Item modifyImageUrl(UUID itemID, String urlToModify, String newImageUrl);
    boolean deleteImageUrl(UUID itemID, String urlToDelete);

    Map<UUID, Item> getItemList();
}

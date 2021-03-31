package com.karolkurbiel.service;

import com.karolkurbiel.dao.ItemDao;
import com.karolkurbiel.model.item.Item;
import com.karolkurbiel.model.item.ItemCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class ItemService {
    private final ItemDao itemDao;

    @Autowired
    public ItemService(@Qualifier("ItemPostgresService") ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public Item addNewItem(String name, String description, ItemCondition itemCondition, BigDecimal price, String location) {
        return this.itemDao.addNewItem(name, description, itemCondition, price, location);
    }

    public Item modifyItem(UUID itemID, String name, String description, ItemCondition itemCondition, BigDecimal price, String location) {
        return this.itemDao.modifyItem(itemID, name, description, itemCondition, price, location);
    }

    public boolean deleteItem(UUID itemID) {
        return this.itemDao.deleteItem(itemID);
    }

    public Item addImageUrlToItem(UUID itemID, String imageUrl) {
        return this.itemDao.addImageUrlToItem(itemID, imageUrl);
    }

    public Item modifyImageUrl(UUID itemID, String oldImageUrl, String newImageUrl) {
        return this.itemDao.modifyImageUrl(itemID, oldImageUrl, newImageUrl);
    }

    public boolean deleteImageUrl(UUID itemID, String imageUrl) {
        return this.itemDao.deleteImageUrl(itemID, imageUrl);
    }

    public Map<UUID, Item> getItemList() {
        return this.itemDao.getItemList();
    }

    public Item getCreatedItem(UUID itemID) {
        return this.itemDao.getCreatedItem(itemID);
    }
}

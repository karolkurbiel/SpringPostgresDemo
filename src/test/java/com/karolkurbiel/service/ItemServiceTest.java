package com.karolkurbiel.service;

import com.karolkurbiel.dao.FakeItemDataAccessService;
import com.karolkurbiel.dao.ItemDao;
import com.karolkurbiel.model.item.Item;
import com.karolkurbiel.model.item.ItemCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ItemServiceTest {
    private ItemDao itemDao;

    @BeforeEach
    public void setUp() {
        itemDao = new FakeItemDataAccessService();
    }

    @Test
    public void addNewItem_correctlyGivenData_addedItemNameMatchItemNameFromDB() {
        String name = "Gumofilce";
        String description = "Elegant polish shoes.";
        ItemCondition itemCondition = ItemCondition.USED;
        BigDecimal price = BigDecimal.valueOf(150.00);
        String location = "Warsaw";

        itemDao.addNewItem(name, description, itemCondition, price, location);
        String expectedItemName = null;
        for(Map.Entry<UUID, Item> mapEntry : itemDao.getItemList().entrySet()) {
            expectedItemName = mapEntry.getValue().getName();
            break;
        }

        assertThat(name).isEqualTo(expectedItemName);
    }

    @Test
    public void getCreatedItem_gotDeepCopyOfItem_HashCodeIsDifferent() {
        Item newItem = itemDao.addNewItem("Axe", "Great for non eco people.", ItemCondition.USED, BigDecimal.valueOf(1599.60), "Fargo");

        Item checkedItem = itemDao.getCreatedItem(newItem.getItemID());
        assertThat(newItem.getItemID()).isEqualTo(checkedItem.getItemID());

        checkedItem.addNewItemImage("coolpics.com/doggie");
        assertThat(newItem).isNotEqualTo(checkedItem);
    }
}
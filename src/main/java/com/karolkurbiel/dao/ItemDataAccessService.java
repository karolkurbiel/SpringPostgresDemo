package com.karolkurbiel.dao;

import com.karolkurbiel.exceptions.NonExistingItemIdException;
import com.karolkurbiel.model.item.Item;
import com.karolkurbiel.model.item.ItemCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Primary
@Repository("ItemPostgresService")
public class ItemDataAccessService implements ItemDao {

    private final JdbcTemplate jdbcTemplate;
    private String sqlQuery;

    @Autowired
    public ItemDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Item addNewItem(String name, String description, ItemCondition itemCondition, BigDecimal price, String location) {

        Item newItem = new Item(name, description, itemCondition, price, location);

        sqlQuery = "INSERT INTO itemsDB VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sqlQuery,
                newItem.getItemID(),
                newItem.getDescription(),
                (newItem.getItemCondition().equals(ItemCondition.NEW) ? 0 : 1),
                newItem.getLocation(),
                newItem.getName(),
                newItem.getPostDate(),
                newItem.getPrice());
        return newItem;
    }

    @Override
    public Item modifyItem(UUID itemID, String name, String description, ItemCondition itemCondition, BigDecimal price, String location) {
        sqlQuery = "UPDATE itemsDB SET description = ?, item_condition = ?, location = ?, name = ?, price = ? WHERE itemid = ?";
        try {
            jdbcTemplate.update(sqlQuery, description, (itemCondition.equals(ItemCondition.NEW) ? 0 : 1), location, name, price, itemID);
        } catch(DataAccessException e) {
            throw new NonExistingItemIdException(itemID);
        }

        return getCreatedItem(itemID);
    }

    @Override
    public boolean deleteItem(UUID itemID) {
        sqlQuery = "DELETE FROM itemsDB WHERE itemid = ?";
        try {
            jdbcTemplate.update(sqlQuery, itemID);
            return true;
        } catch(DataAccessException e) {
            throw new NonExistingItemIdException(itemID);
        }
    }

    @Override
    public Item addImageUrlToItem(UUID itemID, String imageUrl) {
        sqlQuery = "INSERT INTO item_offer_image_url_list VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, itemID, imageUrl);
        return getCreatedItem(itemID);
    }

    @Override
    public Item modifyImageUrl(UUID itemID, String oldImageUrl, String newImageUrl) {
        sqlQuery = "UPDATE item_offer_image_url_list SET offer_image_url_list = ? WHERE (item_itemid = ? AND offer_image_url_list = ?)";
        try {
            jdbcTemplate.update(sqlQuery, newImageUrl, itemID, oldImageUrl);
        } catch(DataAccessException e) {
            throw new NonExistingItemIdException(itemID);
        }
        return getCreatedItem(itemID);
    }

    @Override
    public boolean deleteImageUrl(UUID itemID, String oldImageUrl) {
        sqlQuery = "DELETE FROM item_offer_image_url_list WHERE (item_itemid = ? AND offer_image_url_list = ?)";
        try {
            jdbcTemplate.update(sqlQuery, itemID, oldImageUrl);
            return true;
        } catch(DataAccessException e) {
            throw new NonExistingItemIdException(itemID);
        }
    }

    @Override
    public Item getCreatedItem(UUID itemID) {
        sqlQuery = "SELECT * FROM itemsDB WHERE itemid = ?";
        Item item;
        try {
            item = jdbcTemplate.queryForObject(sqlQuery, new Object[]{itemID}, mapItemFromDB());
        } catch (DataAccessException e) {
            throw new NonExistingItemIdException(itemID);
        }

        return item;
    }

    @Override
    public Map<UUID, Item> getItemList() {
        sqlQuery = "SELECT * FROM itemsDB";
        List<Item> objectList = jdbcTemplate.query(sqlQuery, mapItemFromDB());

        Map<UUID, Item> returnMap = new HashMap<>();

        for(Item item : objectList) {
            returnMap.put(item.getItemID(), item);
        }

        return returnMap;
    }

    //******************************************************************************************************************

    private RowMapper<Item> mapItemFromDB() {
        return ((resultSet, i) -> {
           UUID itemId = resultSet.getObject("itemid", java.util.UUID.class);
           String description = resultSet.getString("description");
           ItemCondition itemCondition = (resultSet.getInt("item_condition") == 0 ? ItemCondition.NEW : ItemCondition.USED);
           String location = resultSet.getString("location");
           String name = resultSet.getString("name");
           String stringPostDate = resultSet.getDate("post_date").toString();
           LocalDate postDate = LocalDate.parse(stringPostDate);
           BigDecimal price = resultSet.getBigDecimal("price");

           sqlQuery = "SELECT * FROM item_offer_image_url_list WHERE item_itemid = ?";
           List<String> imageUrlList = jdbcTemplate.query(sqlQuery, mapUrlFromDB(), itemId);

           return new Item(itemId, name, description, itemCondition, price, location, postDate, imageUrlList);
        });
    }

    private RowMapper<String> mapUrlFromDB() {
        return (resultSet, i) -> resultSet.getString("offer_image_url_list");
    }
}

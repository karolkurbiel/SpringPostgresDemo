package com.karolkurbiel.model.item;

import com.karolkurbiel.exceptions.FieldIsBlankException;
import com.karolkurbiel.exceptions.ImageUrlAlreadyAddedException;
import com.karolkurbiel.exceptions.NegativePriceException;
import com.karolkurbiel.exceptions.ReachedMaximumNumberOfImagesException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "ItemsDB")
public class Item {
    @Id
    @SequenceGenerator(
            name = "item_sequence",
            sequenceName = "item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "item_sequence"
    )
    private final UUID itemID;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(length = 500)
    private String description;
    @Column(nullable = false, length = 1)
    private ItemCondition itemCondition;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false, length = 50)
    private String location;
    private final LocalDate postDate;
    @ElementCollection()
    private final List<String> offerImageUrlList;

    public Item(Item item) {
        this.itemID = item.getItemID();
        this.name = item.getName();
        this.description = item.getDescription();
        this.itemCondition = item.getItemCondition();
        this.price = item.getPrice();
        this.location = item.getLocation();
        this.postDate = item.getPostDate();
        this.offerImageUrlList = item.getOfferImageUrlList();
    }

    public Item(String name, String description, ItemCondition itemCondition, BigDecimal price, String location) {
        this(UUID.randomUUID(), name, description, itemCondition, price, location, LocalDate.now(), new ArrayList<>());
    }

    public Item(UUID itemID, String name, String description, ItemCondition itemCondition, BigDecimal price, String location, LocalDate postDate, List<String> offerImageUrlList) {
        if(name.isBlank() || description.isBlank() || itemCondition == null || price == null || location.isBlank()) {
            throw new FieldIsBlankException();
        } else if(price.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativePriceException(price);
        }
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.itemCondition = itemCondition;
        this.price = price;
        this.location = location;
        this.postDate = postDate;
        this.offerImageUrlList = offerImageUrlList;
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
        List<String> returnedList = new ArrayList<>();

        for(String s : offerImageUrlList) {
            returnedList.add(s);
        }

        return returnedList;
    }

    public void addNewItemImage(String imageURL) {
        if(!offerImageUrlList.contains(imageURL) && offerImageUrlList.size() < 11) {
            offerImageUrlList.add(imageURL);
        } else if(offerImageUrlList.contains(imageURL)) {
            throw new ImageUrlAlreadyAddedException(imageURL);
        } else {
            throw new ReachedMaximumNumberOfImagesException();
        }
    }

    public void modifyItemImage(String oldImageUrl, String newImageUrl) {
        if(offerImageUrlList.contains(oldImageUrl)) {
            int index = offerImageUrlList.indexOf(oldImageUrl);
            modifyItemImage(index, newImageUrl);
        } else {
            throw new IllegalArgumentException("Modify image failed: cannot find url in image list!");
        }
    }

    public void modifyItemImage(int imageIndex, String newImageUrl) {
        if(!offerImageUrlList.contains(newImageUrl) && offerImageUrlList.size() > imageIndex) {
            offerImageUrlList.set(imageIndex, newImageUrl);
        } else if(offerImageUrlList.contains(newImageUrl)) {
            throw new ImageUrlAlreadyAddedException(newImageUrl);
        } else {
            throw new IndexOutOfBoundsException("Replace image failed: Provided index out of bounds!");
        }
    }

    public void deleteItemImage(String imageUrl) {
        if(offerImageUrlList.contains(imageUrl)) {
            int index = offerImageUrlList.indexOf(imageUrl);
            deleteItemImage(index);
        } else {
            throw new IllegalArgumentException("Delete image failed: cannot find url in image list!");
        }
    }

    public void deleteItemImage(int imageIndex) {
        if(offerImageUrlList.size() > imageIndex) {
            offerImageUrlList.remove(imageIndex);
        } else {
            throw new IndexOutOfBoundsException("Delete image failed: Provided index out of bounds!");
        }
    }

    public void modifyProperties(String name, String description, ItemCondition itemCondition, BigDecimal price, String location) {
        if(name != null && !name.isBlank()) {
            this.name = name;
        }
        if(description != null && !description.isBlank()) {
            this.description = description;
        }
        if(itemCondition != null) {
            this.itemCondition = itemCondition;
        }
        if(price != null) {
            this.price = price;
        }
        if(location != null && !location.isBlank()) {
            this.location = location;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(itemID, item.itemID) && Objects.equals(name, item.name) && Objects.equals(description, item.description) && itemCondition == item.itemCondition && Objects.equals(price, item.price) && Objects.equals(location, item.location) && Objects.equals(postDate, item.postDate) && Objects.equals(offerImageUrlList, item.offerImageUrlList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemID, name, description, itemCondition, price, location, postDate, offerImageUrlList);
    }
}

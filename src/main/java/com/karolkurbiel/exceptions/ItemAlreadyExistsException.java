package com.karolkurbiel.exceptions;

import java.util.UUID;

public class ItemAlreadyExistsException extends RuntimeException{
    public ItemAlreadyExistsException(UUID id) {
        super("Failed to add new item: Item " + id + " already exists!");
    }
}

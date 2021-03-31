package com.karolkurbiel.exceptions;

import java.util.UUID;

public class NonExistingItemIdException extends RuntimeException {
    public NonExistingItemIdException(UUID id) {
        super("Failed to return item: Item " + id + " does not exist!");
    }
}

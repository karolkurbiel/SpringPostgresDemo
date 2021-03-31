package com.karolkurbiel.exceptions;

public class FieldIsBlankException extends RuntimeException {
    public FieldIsBlankException() {
        super("Failed to create new item: field cannot be blank!");
    }
}

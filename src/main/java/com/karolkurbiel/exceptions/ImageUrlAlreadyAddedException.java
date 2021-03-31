package com.karolkurbiel.exceptions;

public class ImageUrlAlreadyAddedException extends RuntimeException {
    public ImageUrlAlreadyAddedException(String url) {
        super("Failed to add new image: " + url + " already added!");
    }
}

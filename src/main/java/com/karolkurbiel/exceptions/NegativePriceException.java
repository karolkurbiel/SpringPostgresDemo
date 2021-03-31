package com.karolkurbiel.exceptions;

import java.math.BigDecimal;

public class NegativePriceException extends RuntimeException{
    public NegativePriceException(BigDecimal price) {
        super("Filed to set items price: " + price + " must be >=0 !");
    }
}

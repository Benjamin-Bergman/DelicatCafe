/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.drinks;

import com.pluralsight.io.*;

/**
 * Represents a type of drink.
 */
public final class DrinkType extends BaseInventoried {
    private final String name;
    private final double[] prices;

    DrinkType(String name, double[] prices) {
        //noinspection IfCanBeAssertion
        if (prices.length != 3)
            throw new IllegalArgumentException("Bad length of \"prices\". Expected 3, got " + prices.length);
        this.name = name;
        this.prices = prices.clone();
    }

    /**
     * @return The name of this drink
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the price of this drink at a given size.
     *
     * @param size The size to get the price for
     * @return The price of the drink
     */
    public double getPrice(DrinkSize size) {
        return prices[switch (size) {
            case SMALL -> 0;
            case MEDIUM -> 1;
            case LARGE -> 2;
        }];
    }

    double[] getPrices() {
        return prices.clone();
    }
}

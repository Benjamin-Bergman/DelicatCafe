/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.toppings;

import com.pluralsight.io.*;
import com.pluralsight.orders.*;

/**
 * Represents a type of topping.
 */
public final class ToppingType extends BaseInventoried {
    @SuppressWarnings("FieldNamingConvention")
    private final String name;
    private final String category;
    private final double[] prices;

    ToppingType(String name, String category, double[] prices) {
        //noinspection IfCanBeAssertion
        if (prices.length != 6)
            throw new IllegalArgumentException("Bad length of \"prices\". Expected 6, got " + prices.length);

        this.name = name;
        this.category = category;
        this.prices = prices.clone();
    }

    /**
     * @return The name of the topping
     */
    public String getName() {
        return name;
    }

    /**
     * @return The category of the topping
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the price of this topping for a given size of sandwich.
     *
     * @param size The size of the sandwich
     * @return The price of the topping
     */
    public double getPrice(SandwichSize size) {
        return prices[switch (size) {
            case FOUR_IN -> 0;
            case EIGHT_IN -> 1;
            case TWELVE_IN -> 2;
        }];
    }

    /**
     * Gets the price for extra of this topping for a given size of sandwich.
     *
     * @param size The size of the sandwich
     * @return The price of the topping
     */
    public double getExtraPrice(SandwichSize size) {
        return prices[switch (size) {
            case FOUR_IN -> 3;
            case EIGHT_IN -> 4;
            case TWELVE_IN -> 5;
        }];
    }

    double[] getPrices() {
        return prices.clone();
    }
}

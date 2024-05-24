/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.extras;

import com.pluralsight.io.*;

/**
 * Represents a type of extra item, e.g. a bag of chips.
 */
public final class ExtraType extends BaseInventoried {
    private final String name;
    private final double price;

    ExtraType(String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * @return The price of this item
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return The name of this item
     */
    public String getName() {
        return name;
    }
}

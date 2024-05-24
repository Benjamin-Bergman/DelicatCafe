/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

/**
 * Represents an item with a tracked inventory.
 */
public interface Inventoried {
    /**
     * Consumes a certain amount of the item from stock.
     *
     * @param amount The amount to consume
     */
    void consume(int amount);

    /**
     * @return Whether this item is considered "in-stock"
     */
    boolean isInStock();
}

/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

/**
 * Represents a line item associated with an {@link Inventoried} item.
 */
public interface InventoriedLineItem extends LineItem {
    /**
     * Consumes the line item from its inventory.
     */
    void consume();
}

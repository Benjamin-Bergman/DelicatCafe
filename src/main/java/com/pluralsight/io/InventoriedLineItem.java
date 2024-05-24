/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

/**
 * Represents a line item associated with an {@link Inventoried} item.
 */
public interface InventoriedLineItem extends LineItem {
    /**
     * @return The inventoried item associated with this line item
     */
    Inventoried getInventoried();
}

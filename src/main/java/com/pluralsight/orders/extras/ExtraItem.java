/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.extras;

import com.pluralsight.io.*;

import java.util.*;

/**
 * Represents a line item for an extra item.
 */
public final class ExtraItem implements InventoriedLineItem {
    private ExtraType type;

    /**
     * @param type The type of item
     */
    public ExtraItem(ExtraType type) {
        this.type = type;
    }

    /**
     * @return The type of item
     */
    public ExtraType getType() {
        return type;
    }

    /**
     * @param type The type of item
     */
    public void setType(ExtraType type) {
        this.type = type;
    }

    @Override
    public Inventoried getInventoried() {
        return type;
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public double getPrice() {
        return type.getPrice();
    }

    @Override
    public List<LineItem> getSubItems() {
        return List.of();
    }
}

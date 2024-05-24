/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.bread;

import com.pluralsight.io.*;

import java.util.*;

/**
 * Represents a line item for bread.
 */
public final class BreadItem implements InventoriedLineItem {
    @SuppressWarnings("FieldNamingConvention")
    private BreadType type;

    /**
     * @param type The type of bread this line item is for
     */
    public BreadItem(BreadType type) {
        this.type = type;
    }

    /**
     * @return The type of bread this line item is for
     */
    public BreadType getType() {
        return type;
    }

    /**
     * @param type The type of bread this line item is for
     */
    public void setType(BreadType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public List<LineItem> getSubItems() {
        return List.of();
    }

    @Override
    public Inventoried getInventoried() {
        return type;
    }
}

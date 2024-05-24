/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.drinks;

import com.pluralsight.io.*;

import java.util.*;

/**
 * Represents a line item for a drink.
 */
public final class DrinkItem implements InventoriedLineItem {
    @SuppressWarnings("FieldNamingConvention")
    private DrinkSize size;
    @SuppressWarnings("FieldNamingConvention")
    private DrinkType type;

    /**
     * @param size The size of the drink
     * @param type The type of the drink
     */
    public DrinkItem(DrinkSize size, DrinkType type) {
        this.size = size;
        this.type = type;
    }

    /**
     * @return The size of the drink
     */
    public DrinkSize getSize() {
        return size;
    }

    /**
     * @param size The size of the drink
     */
    @SuppressWarnings("unused")
    public void setSize(DrinkSize size) {
        this.size = size;
    }

    /**
     * @return The type of the drink
     */
    public DrinkType getType() {
        return type;
    }

    /**
     * @param type The type of the drink
     */
    public void setType(DrinkType type) {
        this.type = type;
    }

    @Override
    public Inventoried getInventoried() {
        return type;
    }

    @Override
    public String getName() {
        return size + " " + type.getName();
    }

    @Override
    public double getPrice() {
        return type.getPrice(size);
    }

    @Override
    public List<LineItem> getSubItems() {
        return List.of();
    }
}

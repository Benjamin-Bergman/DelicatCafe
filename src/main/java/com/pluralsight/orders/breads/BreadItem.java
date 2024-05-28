/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.breads;

import com.pluralsight.io.*;
import com.pluralsight.orders.*;

import java.util.*;

/**
 * Represents a line item for bread.
 */
public final class BreadItem implements InventoriedLineItem {
    @SuppressWarnings("FieldNamingConvention")
    private BreadType type;
    @SuppressWarnings("FieldNamingConvention")
    private SandwichSize size;

    /**
     * @param type The type of bread this line item is for
     * @param size The size of sandwich this line item is for
     */
    public BreadItem(BreadType type, SandwichSize size) {
        this.type = type;
        this.size = size;
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
    public void consume() {
        type.consume(switch (size) {
            case FOUR_IN -> 1;
            case EIGHT_IN -> 2;
            case TWELVE_IN -> 3;
        });
    }

    /**
     * @return The size of sandwich this line item is for
     */
    public SandwichSize getSize() {
        return size;
    }

    /**
     * @param size The size of sandwich this line item is for
     */
    public void setSize(SandwichSize size) {
        this.size = size;
    }
}

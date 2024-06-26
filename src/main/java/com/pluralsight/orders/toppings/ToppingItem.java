/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.toppings;

import com.pluralsight.io.*;
import com.pluralsight.orders.*;

import java.util.*;

/**
 * Represents a line item for a topping.
 */
public final class ToppingItem implements InventoriedLineItem {
    @SuppressWarnings("FieldNamingConvention")
    private ToppingType type;
    @SuppressWarnings("FieldNamingConvention")
    private SandwichSize size;
    private boolean extra;

    /**
     * @param type  The type of topping
     * @param size  The size of sandwich this topping is on
     * @param extra Whether this is an extra serving
     */
    public ToppingItem(ToppingType type, SandwichSize size, boolean extra) {
        this.type = type;
        this.size = size;
        this.extra = extra;
    }

    /**
     * @param other A ToppingItem to copy
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public ToppingItem(ToppingItem other) {
        this(other.type, other.size, other.extra);
    }

    @Override
    public String getName() {
        return (extra ? "Extra " : "") + type.getName();
    }

    @Override
    public double getPrice() {
        return extra ? type.getExtraPrice(size) : type.getPrice(size);
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
     * @return The type of topping
     */
    public ToppingType getType() {
        return type;
    }

    /**
     * @param type The type of topping
     */
    public void setType(ToppingType type) {
        this.type = type;
    }

    /**
     * @return Whether this is an extra serving
     */
    public boolean isExtra() {
        return extra;
    }

    /**
     * @param extra Whether this is an extra serving
     */
    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    /**
     * @return The size of sandwich this topping is on
     */
    @SuppressWarnings("unused")
    public SandwichSize getSize() {
        return size;
    }

    /**
     * @param size The size of sandwich this topping is on
     */
    public void setSize(SandwichSize size) {
        this.size = size;
    }
}

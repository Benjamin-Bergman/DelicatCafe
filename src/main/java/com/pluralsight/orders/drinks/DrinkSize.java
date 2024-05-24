/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.drinks;

/**
 * Represents the size of a drink.
 */
public enum DrinkSize {
    /**
     * A small drink.
     */
    SMALL,
    /**
     * A medium drink.
     */
    MEDIUM,
    /**
     * A large drink.
     */
    LARGE;

    @Override
    public String toString() {
        var name = super.toString();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}

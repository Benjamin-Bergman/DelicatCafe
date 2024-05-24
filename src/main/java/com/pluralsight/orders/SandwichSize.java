/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders;

/**
 * Represents the size of a sandwich.
 */
public enum SandwichSize {
    /**
     * A 4" sandwich.
     */
    FOUR_IN("4\""),
    /**
     * An 8" sandwich.
     */
    EIGHT_IN("8\""),
    /**
     * A 12" sandwich.
     */
    TWELVE_IN("12\"");

    private final String display;

    SandwichSize(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}

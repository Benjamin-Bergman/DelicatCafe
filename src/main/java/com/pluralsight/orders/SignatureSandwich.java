/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders;

/**
 * Represents a signature sandwich.
 */
@SuppressWarnings("ClassCanBeRecord")
public final class SignatureSandwich {
    private final SandwichItem sandwich;
    @SuppressWarnings("FieldNamingConvention")
    private final String name;

    /**
     * @param sandwich The sandwich itself
     * @param name     The name of the sandwich
     */
    public SignatureSandwich(SandwichItem sandwich, String name) {
        this.sandwich = sandwich;
        this.name = name;
    }

    /**
     * @return The sandwich itself
     */
    public SandwichItem getSandwich() {
        return new SandwichItem(sandwich);
    }

    /**
     * @return The name of the sandwich
     */
    public String getName() {
        return name;
    }
}

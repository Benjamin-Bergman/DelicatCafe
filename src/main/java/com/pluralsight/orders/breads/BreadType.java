/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.breads;

import com.pluralsight.io.*;

/**
 * Represents a type of bread.
 */
public final class BreadType extends BaseInventoried {
    @SuppressWarnings("FieldNamingConvention")
    private final String name;

    BreadType(String name) {
        this.name = name;
    }

    /**
     * @return The name of this type of bread
     */
    public String getName() {
        return name;
    }
}

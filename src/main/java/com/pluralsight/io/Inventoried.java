/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

public interface Inventoried {
    void consume(int amount);

    boolean isInStock();
}

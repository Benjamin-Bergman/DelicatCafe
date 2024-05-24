/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

public interface InventoriedLineItem extends Inventoried {
    Inventoried getInventoried();
}

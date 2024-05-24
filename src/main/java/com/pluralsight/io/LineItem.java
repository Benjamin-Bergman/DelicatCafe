/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

import java.util.*;

/**
 * Represents a line item on a receipt.
 */
public interface LineItem {
    /**
     * @return The name describing this line item
     */
    String getName();

    /**
     * @return The price associated with this line item
     */
    double getPrice();

    /**
     * @return Any subcomponents of this line item, e.g. extra toppings
     */
    List<LineItem> getSubItems();
}

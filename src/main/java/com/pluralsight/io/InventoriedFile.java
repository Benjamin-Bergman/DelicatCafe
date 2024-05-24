/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

import java.util.*;

/**
 * Represents a resource holding statistics for a certain type of {@link Inventoried} item.
 *
 * @param <T> The type of {@code Inventoried} item this resource manages
 */
public interface InventoriedFile<T extends Inventoried> {
    /**
     * @return The items managed by this resource
     */
    List<T> getItems();
}
/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

import java.util.*;

public interface InventoriedFile<T extends Inventoried> {
    List<T> getItems();
}

/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

import java.util.*;

public interface LineItem {
    String getName();

    OptionalDouble getPrice();

    List<LineItem> getSubItems();
}

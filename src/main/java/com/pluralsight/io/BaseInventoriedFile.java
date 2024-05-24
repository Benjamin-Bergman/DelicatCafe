/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

import java.io.*;
import java.util.*;

/**
 * A file-backed implementation of {@link InventoriedFile}.
 *
 * @param <T> The type of {@code Inventoried} item this resource manages
 */
public abstract class BaseInventoriedFile<T extends BaseInventoried> implements InventoriedFile<T> {
    private final File file;
    private final List<T> items;

    /**
     * @param file The file to use as a backing.
     */
    protected BaseInventoriedFile(File file) {
        this.file = file;
        items = load(file);
    }

    private static <T> List<T> load(File file) {
        return List.of();
    }

    @Override
    public List<T> getItems() {
        return items;
    }

    void save() {

    }

    /**
     * Converts a serialized line of data into an item.
     *
     * @param line The line of data to convert
     * @return An item represented by that line of data
     */
    protected abstract T readFromLine(String line);

    /**
     * Converts an item into a serialized line of data.
     *
     * @param item The item to convert
     * @return A line of data representing that item
     */
    protected abstract String convertToLine(T item);
}

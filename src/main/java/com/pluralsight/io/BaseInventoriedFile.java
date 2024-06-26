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
    @SuppressWarnings("FieldNamingConvention")
    private final File file;
    private final List<T> items;

    /**
     * @param file The file to use as a backing.
     */
    protected BaseInventoriedFile(File file) {
        this.file = file;
        items = load();
    }

    @Override
    public final List<T> getItems() {
        return items.stream().filter(T::isInStock).toList();
    }

    @Override
    public final List<T> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    private List<T> load() {
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            return br.lines()
                .filter(s -> !s.isBlank() && !s.isEmpty())
                .map(this::readLine)
                .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load inventory file \"" + file.getPath() + "\"!", e);
        }
    }

    private T readLine(String line) {
        var sep = line.indexOf('|');
        int count = Integer.parseInt(line.substring(0, sep));

        var obj = readFromLine(line.substring(sep + 1));
        obj.setAmount(count);
        obj.setOwner(this);
        return obj;
    }

    final void save() {
        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (T item : items)
                bw.write("%d|%s%n".formatted(item.getAmount(), convertToLine(item)));
        } catch (IOException e) {
            System.err.println("Error: Could not update inventory file \"" + file.getPath() + "\"!");
            e.printStackTrace(System.err);
        }
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

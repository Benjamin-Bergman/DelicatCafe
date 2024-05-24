/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.extras;

import com.pluralsight.io.*;

import java.io.*;

/**
 * A resource holding stock information on {@link ExtraType}s.
 */
public final class ExtraTypeFile extends BaseInventoriedFile<ExtraType> {
    /**
     * @param file The file to use as a backing.
     */
    public ExtraTypeFile(File file) {
        super(file);
    }

    @Override
    protected ExtraType readFromLine(String line) {
        var split = line.indexOf('|');

        //noinspection IfCanBeAssertion
        if (split == -1)
            throw new IllegalArgumentException("The line did not have a separator");
        var price = Double.parseDouble(line.substring(0, split));
        var name = line.substring(split + 1);

        return new ExtraType(name, price);
    }

    @Override
    protected String convertToLine(ExtraType item) {
        return "%.2f|%s".formatted(item.getPrice(), item.getName());
    }
}

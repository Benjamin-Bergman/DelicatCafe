/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.breads;

import com.pluralsight.io.*;

import java.io.*;

/**
 * A resource holding stock information on {@link BreadType}s.
 */
public final class BreadTypeFile extends BaseInventoriedFile<BreadType> {
    /**
     * @param file The file to use as a backing.
     */
    public BreadTypeFile(File file) {
        super(file);
    }

    @Override
    protected BreadType readFromLine(String line) {
        return new BreadType(line);
    }

    @Override
    protected String convertToLine(BreadType item) {
        return item.getName();
    }
}

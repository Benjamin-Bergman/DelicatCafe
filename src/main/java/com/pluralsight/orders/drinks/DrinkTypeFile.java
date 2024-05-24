/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.drinks;

import com.pluralsight.io.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * A resource holding stock information on {@link DrinkType}s.
 */
public final class DrinkTypeFile extends BaseInventoriedFile<DrinkType> {
    /**
     * @param file The file to use as a backing.
     */
    public DrinkTypeFile(File file) {
        super(file);
    }

    @Override
    protected DrinkType readFromLine(String line) {
        var parts = line.split("\\|");

        var prices = new double[]{
            Double.parseDouble(parts[0]),
            Double.parseDouble(parts[1]),
            Double.parseDouble(parts[2]),
        };

        var name = Arrays.stream(parts).skip(3).collect(Collectors.joining("|"));
        return new DrinkType(name, prices);
    }

    @Override
    protected String convertToLine(DrinkType item) {
        var prices = item.getPrices();
        return "%.2f|%.2f|%.2f|%s".formatted(prices[0], prices[1], prices[2], item.getName());
    }
}

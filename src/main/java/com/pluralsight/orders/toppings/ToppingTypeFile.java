/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders.toppings;

import com.pluralsight.io.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * A resource holding stock information on {@link ToppingType}s.
 */
public final class ToppingTypeFile extends BaseInventoriedFile<ToppingType> {
    /**
     * @param file The file to use as a backing.
     */
    public ToppingTypeFile(File file) {
        super(file);
    }

    @Override
    protected ToppingType readFromLine(String line) {
        var parts = line.split("\\|");

        var prices = Arrays.stream(parts).limit(6).mapToDouble(Double::parseDouble).toArray();
        var category = parts[6];
        var name = Arrays.stream(parts).skip(7).collect(Collectors.joining("|"));

        return new ToppingType(name, category, prices);
    }

    @SuppressWarnings("FeatureEnvy")
    @Override
    protected String convertToLine(ToppingType item) {
        var prices = Arrays.stream(item.getPrices()).mapToObj("%.2f"::formatted).collect(Collectors.joining("|"));

        return "%s|%s|%s".formatted(prices, item.getCategory(), item.getName());
    }
}

/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.app;

import com.pluralsight.io.*;
import com.pluralsight.orders.bread.*;
import com.pluralsight.orders.drinks.*;
import com.pluralsight.orders.extras.*;
import com.pluralsight.orders.toppings.*;

import java.io.*;
import java.util.stream.*;

/**
 * Represents an app to run a sandwich shop.
 */
public final class SandwichShop {
    private final InventoriedFile<ToppingType> toppings;
    private final InventoriedFile<BreadType> breads;
    private final InventoriedFile<DrinkType> drinks;
    private final InventoriedFile<ExtraType> extras;

    /**
     * Creates a new shop with the specified inventory files.
     *
     * @param toppings The file for toppings
     * @param breads   The file for breads
     * @param drinks   The file for drinks
     * @param extras   The file for extras
     */
    public SandwichShop(InventoriedFile<ToppingType> toppings, InventoriedFile<BreadType> breads, InventoriedFile<DrinkType> drinks, InventoriedFile<ExtraType> extras) {
        this.toppings = toppings;
        this.breads = breads;
        this.drinks = drinks;
        this.extras = extras;
    }

    /**
     * Runs the shop.
     *
     * @param in  The stream for user input
     * @param out The stream for user output
     */
    public void runShop(InputStream in, PrintStream out) {
        out.println("Toppings:");
        out.println(toppings.getItems().stream().map(ToppingType::getName).collect(Collectors.joining(System.lineSeparator())));
        out.println("Breads:");
        out.println(breads.getItems().stream().map(BreadType::getName).collect(Collectors.joining(System.lineSeparator())));
        out.println("Drinks:");
        out.println(drinks.getItems().stream().map(DrinkType::getName).collect(Collectors.joining(System.lineSeparator())));
        out.println("Extras:");
        out.println(extras.getItems().stream().map(ExtraType::getName).collect(Collectors.joining(System.lineSeparator())));
    }
}

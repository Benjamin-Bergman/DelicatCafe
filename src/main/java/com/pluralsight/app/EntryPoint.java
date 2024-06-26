/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.app;

import com.pluralsight.io.*;
import com.pluralsight.orders.breads.*;
import com.pluralsight.orders.drinks.*;
import com.pluralsight.orders.extras.*;
import com.pluralsight.orders.toppings.*;

import java.io.*;

/**
 * Holds the entry point for the application.
 */
@SuppressWarnings("UtilityClass")
public final class EntryPoint {
    /**
     * The entry point for the application.
     *
     * @param args Unused
     */
    public static void main(String[] args) {
        var toppings = new ToppingTypeFile(new File("toppings.csv"));
        var breads = new BreadTypeFile(new File("breads.csv"));
        var drinks = new DrinkTypeFile(new File("drinks.csv"));
        var extras = new ExtraTypeFile(new File("extras.csv"));

        var signatures = SignatureFile.load(new File("signatures.csv"), toppings, breads);

        var shop = new SandwichShop(toppings, breads, drinks, extras, signatures);

        shop.runShop(System.in, System.out);
    }
}

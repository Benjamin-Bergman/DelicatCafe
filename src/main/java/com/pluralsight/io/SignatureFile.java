/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

import com.pluralsight.orders.*;
import com.pluralsight.orders.breads.*;
import com.pluralsight.orders.toppings.*;

import java.io.*;
import java.util.*;

/**
 * Represents a list of signature sandwiches backed by a file.
 */
public final class SignatureFile {
    private final List<SignatureSandwich> sandwiches;

    private SignatureFile(List<SignatureSandwich> sandwiches) {
        this.sandwiches = sandwiches;
    }

    /**
     * Loads a new {@code SignatureFile} from the given file.
     *
     * @param file     The file to load from
     * @param toppings The possible toppings
     * @param breads   The possible breads
     * @return A new list of signature sandwiches
     */
    public static SignatureFile load(File file, InventoriedFile<ToppingType> toppings, InventoriedFile<BreadType> breads) {
        return new SignatureFile(loadItems(file, toppings, breads));
    }

    private static SignatureSandwich parseLine(String line, InventoriedFile<ToppingType> toppings, InventoriedFile<BreadType> breads) {
        String[] parts = line.split("\\|");

        var name = parts[0];

        //noinspection OptionalGetWithoutIsPresent
        var bread = breads
            .getAllItems()
            .stream()
            .filter(br -> br.getName().equals(parts[1]))
            .findFirst()
            .get();

        var toasted = Boolean.parseBoolean(parts[2]);

        //noinspection OptionalGetWithoutIsPresent
        var sandwichToppings = Arrays.stream(parts)
            .skip(3)
            .map(part ->
                toppings
                    .getAllItems()
                    .stream()
                    .filter(tp -> tp.getName().equals(part))
                    .findFirst()
                    .get())
            .toList();

        var sand = new SandwichItem(SandwichSize.FOUR_IN, bread);
        for (//noinspection ReassignedVariable
            int i = 0; i < sandwichToppings.size(); i++) {
            ToppingType topping = sandwichToppings.get(i);
            sand.addTopping(topping, sandwichToppings.indexOf(topping) != i);
        }

        sand.setToasted(toasted);

        return new SignatureSandwich(sand, name);
    }

    private static List<SignatureSandwich> loadItems(File file, InventoriedFile<ToppingType> toppings, InventoriedFile<BreadType> breads) {
        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {
            return br.lines()
                .filter(s -> !s.isEmpty() && !s.isBlank())
                .map(line -> parseLine(line, toppings, breads))
                .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @return All the signature sandwiches in this file
     */
    public List<SignatureSandwich> getSignatures() {
        return Collections.unmodifiableList(sandwiches);
    }

}

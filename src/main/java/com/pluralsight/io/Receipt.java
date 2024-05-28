/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Represents a receipt for a sale.
 */
public final class Receipt {
    private static final int ITEM_WIDTH = 40;
    private static final int PRICE_WIDTH = 6;

    @SuppressWarnings("FieldNamingConvention")
    private final LineItem item;

    /**
     * @param item The item in this receipt, likely a collection of subitems
     */
    public Receipt(LineItem item) {
        this.item = item;
    }

    @SuppressWarnings("FeatureEnvy")
    private static Stream<String> processItem(LineItem item, int depth) {
        var subPrice = item.getSubItems().stream().mapToDouble(LineItem::getPrice).sum();

        var indent = 2 * depth;
        var width = ITEM_WIDTH - indent;

        var totalPrice = item.getPrice() + subPrice;

        //noinspection ConstantExpression
        return Stream.concat(
            Stream.of((" ".repeat(indent) + "%-" + width + "s%s").formatted(
                item.getName(),
                totalPrice == 0 ? "" : ("%" + PRICE_WIDTH + ".2f").formatted(totalPrice))),
            item.getSubItems().stream().flatMap(subItem -> processItem(subItem, depth + 1)));
    }

    private static Stream<LineItem> recursiveFlatMap(Stream<? extends LineItem> items) {
        var list = items.toList();
        return Stream.concat(list.stream(), list.stream().map(LineItem::getSubItems).map(List::stream).flatMap(Receipt::recursiveFlatMap));
    }

    @Override
    public String toString() {
        return processItem(item, 0).collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * @param file The file to save to
     * @return {@code true} if the operation succeeded, {@code false} otherwise
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean saveToFile(File file) {
        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(toString());
            return true;
        } catch (IOException e) {
            System.err.println("Error: Could not update receipt file \"" + file.getPath() + "\"!");
            e.printStackTrace(System.err);
            return false;
        }
    }

    /**
     * Updates the inventories of any {@link InventoriedLineItem}s on this receipt
     */
    public void processSale() {
        recursiveFlatMap(Stream.of(item)).forEach(flatItem -> {
            if (flatItem instanceof InventoriedLineItem inv)
                inv.consume();
        });
    }

    /**
     * @return The total price on this receipt
     */
    public double getPrice() {
        return recursiveFlatMap(item.getSubItems().stream()).mapToDouble(LineItem::getPrice).sum();
    }
}

/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders;

import com.pluralsight.io.*;
import com.pluralsight.orders.drinks.*;
import com.pluralsight.orders.extras.*;

import java.util.*;

/**
 * Represents an order.
 */
@SuppressWarnings("NewClassNamingConvention")
public final class Order implements LineItem {
    private final List<ExtraItem> extras;
    private final List<SandwichItem> sandwiches;
    private final List<DrinkItem> drinks;

    /**
     * Creates an empty order.
     */
    public Order() {
        extras = new ArrayList<>();
        sandwiches = new ArrayList<>();
        drinks = new ArrayList<>();
    }

    /**
     * @return An unmodifiable view of the extras in this order
     */
    public List<ExtraType> getExtras() {
        return extras.stream().map(ExtraItem::getType).toList();
    }

    /**
     * @return An unmodifiable view of the sandwiches in this order
     * @apiNote While the list is unmodifiable, the sandwiches within are modifiable.
     */
    public List<SandwichItem> getSandwiches() {
        return Collections.unmodifiableList(sandwiches);
    }

    /**
     * @return An unmodifiable view of the drinks in this order
     */
    public List<DrinkView> getDrinks() {
        return drinks.stream().map(DrinkView::new).toList();
    }

    /**
     * @param extra An extra to add to this order
     */
    public void addExtra(ExtraType extra) {
        extras.add(new ExtraItem(extra));
    }

    /**
     * @param extra An extra to remove from this order
     */
    public void removeExtra(ExtraType extra) {
        for (//noinspection ReassignedVariable
            int i = 0; i < extras.size(); i++)
            if (extras.get(i).getType() == extra) {
                extras.remove(i);
                return;
            }

        throw new IllegalStateException("That extra does not exist.");
    }

    /**
     * @param drink The drink to add to this order
     * @param size  The size of the drink to add
     */
    public void addDrink(DrinkType drink, DrinkSize size) {
        drinks.add(new DrinkItem(size, drink));
    }

    /**
     * @param drink The drink to remove from this order
     * @param size  The size of the drink to remove
     */
    public void removeDrink(DrinkType drink, DrinkSize size) {
        for (//noinspection ReassignedVariable
            int i = 0; i < drinks.size(); i++)
            if (drinks.get(i).getType() == drink && drinks.get(i).getSize() == size) {
                drinks.remove(i);
                return;
            }

        throw new IllegalStateException("That drink does not exist.");
    }

    /**
     * @param sandwich The sandwich to add to this order
     */
    public void addSandwich(SandwichItem sandwich) {
        sandwiches.add(sandwich);
    }

    /**
     * @param index The index of the sandwich to remove from this order
     */
    public void removeSandwich(int index) {
        sandwiches.remove(index);
    }

    @Override
    public String getName() {
        return "Order";
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public List<LineItem> getSubItems() {
        var subItems = new ArrayList<LineItem>();
        subItems.addAll(sandwiches);
        subItems.addAll(drinks);
        subItems.addAll(extras);
        return Collections.unmodifiableList(subItems);
    }

    /**
     * Represents an unmodifiable view of a drink line item.
     */
    public static final class DrinkView {
        @SuppressWarnings("FieldNamingConvention")
        private final DrinkType type;
        @SuppressWarnings("FieldNamingConvention")
        private final DrinkSize size;

        private DrinkView(DrinkItem item) {
            type = item.getType();
            size = item.getSize();
        }

        /**
         * @return The type of drink
         */
        public DrinkType getType() {
            return type;
        }

        /**
         * @return The size of drink
         */
        public DrinkSize getSize() {
            return size;
        }
    }
}

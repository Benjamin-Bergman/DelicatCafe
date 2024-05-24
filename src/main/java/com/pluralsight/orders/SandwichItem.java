/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.orders;

import com.pluralsight.io.*;
import com.pluralsight.orders.bread.*;
import com.pluralsight.orders.toppings.*;

import java.util.*;

/**
 * Represents a line item for a sandwich.
 */
public final class SandwichItem implements LineItem {
    private final List<ToppingItem> toppings;
    @SuppressWarnings("FieldNamingConvention")
    private SandwichSize size;
    private boolean toasted;
    private BreadItem bread;

    /**
     * The sandwich is not toasted by default.
     *
     * @param size  The size of the sandwich
     * @param bread The bread for the sandwich
     */
    public SandwichItem(SandwichSize size, BreadType bread) {
        this.size = size;
        this.bread = new BreadItem(bread);
        toppings = new ArrayList<>();
        toasted = false;
    }

    /**
     * @return The size of the sandwich
     */
    public SandwichSize getSize() {
        return size;
    }

    /**
     * @param size The size of the sandwich
     */
    @SuppressWarnings("unused")
    public void setSize(SandwichSize size) {
        this.size = size;
        for (ToppingItem item : toppings)
            item.setSize(size);
    }

    /**
     * @return Whether the sandwich is toasted
     */
    public boolean isToasted() {
        return toasted;
    }

    /**
     * @param toasted Whether the sandwich is toasted
     */
    public void setToasted(boolean toasted) {
        this.toasted = toasted;
    }

    /**
     * @return The type of bread on the sandwich
     */
    @SuppressWarnings("unused")
    public BreadType getBread() {
        return bread.getType();
    }

    /**
     * @param bread The type of bread on the sandwich
     */
    @SuppressWarnings("unused")
    public void setBread(BreadType bread) {
        this.bread = new BreadItem(bread);
    }

    /**
     * @return An unmodifiable view of the toppings on the sandwich
     */
    public List<ToppingView> getToppings() {
        return toppings.stream().map(ToppingView::new).toList();
    }

    /**
     * Adds a topping to the sandwich
     *
     * @param topping The topping to add
     * @param extra   Whether this topping is an extra serving
     */
    public void addTopping(ToppingType topping, boolean extra) {
        toppings.add(new ToppingItem(topping, size, extra));
    }

    /**
     * Removes a topping form the sandwich
     *
     * @param toppingType The type of topping
     * @param extra       Whether the topping is an extra serving
     */
    public void removeTopping(ToppingType toppingType, boolean extra) {
        for (//noinspection ReassignedVariable
            int i = 0; i < toppings.size(); i++)
            if (toppings.get(i).getType() == toppingType && toppings.get(i).isExtra() == extra) {
                toppings.remove(i);
                return;
            }

        throw new IllegalStateException("That topping does not exist.");
    }

    @Override
    public String getName() {
        return size + " Sandwich";
    }

    @Override
    public double getPrice() {
        return switch (size) {
            case FOUR_IN -> 5.5;
            case EIGHT_IN -> 7.0;
            case TWELVE_IN -> 8.5;
        };
    }

    @Override
    public List<LineItem> getSubItems() {
        List<LineItem> subItems = new ArrayList<>();

        subItems.add(bread);

        subItems.addAll(toppings);

        if (toasted)
            subItems.add(new ToastedItem());

        return Collections.unmodifiableList(subItems);
    }

    /**
     * Represents an unmodifiable view of a topping line item.
     */
    public static final class ToppingView {
        @SuppressWarnings("FieldNamingConvention")
        private final ToppingType type;
        private final boolean extra;

        private ToppingView(ToppingItem item) {
            type = item.getType();
            extra = item.isExtra();
        }

        /**
         * @return The type of topping
         */
        public ToppingType getType() {
            return type;
        }

        /**
         * @return Whether this is an extra serving
         */
        public boolean isExtra() {
            return extra;
        }
    }

    private static final class ToastedItem implements LineItem {
        @Override
        public String getName() {
            return "Toasted";
        }

        @Override
        public double getPrice() {
            return 0;
        }

        @Override
        public List<LineItem> getSubItems() {
            return List.of();
        }
    }
}

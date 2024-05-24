/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.app;

import com.pluralsight.io.*;
import com.pluralsight.orders.*;
import com.pluralsight.orders.bread.*;
import com.pluralsight.orders.drinks.*;
import com.pluralsight.orders.extras.*;
import com.pluralsight.orders.toppings.*;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Represents an app to run a sandwich shop.
 */
@SuppressWarnings("ObjectAllocationInLoop")
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
        if (toppings.getItems().isEmpty())
            throw new IllegalArgumentException("The must be some toppings");
        if (breads.getItems().isEmpty())
            throw new IllegalArgumentException("The must be some breads");
        if (drinks.getItems().isEmpty())
            throw new IllegalArgumentException("The must be some drinks");
        if (extras.getItems().isEmpty())
            throw new IllegalArgumentException("The must be some extras");
    }

    private static int queryCommand(Scanner scanner, PrintStream out, Collection<Integer> valid) {
        while (true) {
            if (!scanner.hasNextInt()) {
                out.printf("Unknown option \"%s\". Please try again.%n", scanner.next());
                continue;
            }

            int input = scanner.nextInt();
            if (!valid.contains(input)) {
                out.printf("Unknown option \"%s\". Please try again.%n", input);
                continue;
            }

            return input;
        }
    }

    private static <T> T queryListCommand(Scanner scanner, PrintStream out, List<? extends T> options) {
        return queryListCommand(scanner, out, options, Objects::toString);
    }

    private static <T> T queryListCommand(Scanner scanner, PrintStream out, List<? extends T> options, Function<? super T, String> displaySelector) {
        if (options.isEmpty()) {
            out.println("Nothing to choose");
            return null;
        }

        for (//noinspection ReassignedVariable
            int i = 0; i < options.size(); i++) {
            T option = options.get(i);
            out.printf("%d - %s%n", i + 1, displaySelector.apply(option));
        }
        int choice = queryCommand(scanner, out, IntStream.rangeClosed(1, options.size()).boxed().toList());
        return options.get(choice - 1);
    }

    @SuppressWarnings({"MethodWithMoreThanThreeNegations", "BooleanMethodNameMustStartWithQuestion"})
    private static boolean queryYN(Iterator<String> scanner, PrintStream out, Boolean defaultValue) {
        while (true) {
            var fullInput = scanner.next();
            var input = fullInput.trim().toLowerCase().charAt(0);
            if (defaultValue != null)
                return input == 'y' || input != 'n' && defaultValue;
            if (input != 'y' && input != 'n') {
                out.printf("Unknown option \"%s\". Please try again.%n", fullInput);
                continue;
            }
            return input == 'y';
        }
    }

    private static SandwichSize querySandwichSize(Scanner scanner, PrintStream out) {
        out.println("Enter the sandwich size:");
        return queryListCommand(scanner, out, List.of(SandwichSize.values()));
    }

    private static DrinkSize queryDrinkSize(Scanner scanner, PrintStream out) {
        out.println("Enter the size:");
        return queryListCommand(scanner, out, List.of(DrinkSize.values()));
    }

    @SuppressWarnings("FeatureEnvy")
    private static boolean runCheckOut(Iterator<String> scanner, PrintStream out, Receipt receipt) {
        out.printf("Your total is: $%.2f%n", receipt.getPrice());
        out.println("Are you sure you want to make this purchase?");
        if (!queryYN(scanner, out, true))
            return false;
        var file = new File("receipts", "%tY%<tm%<td-%<tH%<tM%<tS.txt".formatted(LocalDateTime.now()));
        if (!receipt.saveToFile(file)) {
            out.println("There was an error processing your order. Please try again later.");
            return false;
        }

        receipt.processSale();

        return true;
    }

    /**
     * Runs the shop.
     *
     * @param in  The stream for user input
     * @param out The stream for user output
     */
    public void runShop(InputStream in, PrintStream out) {
        try (Scanner scanner = new Scanner(in)) {
            while (true) {
                out.println("""
                    1 - New Order
                    2 - Exit""");

                int input = queryCommand(scanner, out, List.of(1, 2));

                if (input == 2) {
                    out.println("Thank you for coming!");
                    break;
                }

                runOrder(scanner, out);
            }
        }
    }

    private BreadType queryBreadType(Scanner scanner, PrintStream out) {
        out.println("Enter the bread type:");
        return queryListCommand(scanner, out, breads.getItems(), BreadType::getName);
    }

    @SuppressWarnings("FeatureEnvy")
    private DrinkType queryDrinkType(Scanner scanner, PrintStream out) {
        out.println("Enter the drink type:");
        return queryListCommand(scanner, out, drinks.getItems(),
            dt -> "%s ($%.2fL, $%.2fM, $%.2fS)".formatted(
                dt.getName(),
                dt.getPrice(DrinkSize.LARGE),
                dt.getPrice(DrinkSize.MEDIUM),
                dt.getPrice(DrinkSize.SMALL)
            ));
    }

    private ExtraType queryExtra(Scanner scanner, PrintStream out) {
        out.println("Enter the size:");
        return queryListCommand(scanner, out, extras.getItems(), et -> "%s ($%.2f)".formatted(et.getName(), et.getPrice()));
    }

    @SuppressWarnings({"FeatureEnvy", "OverlyComplexMethod"})
    private void runOrder(Scanner scanner, PrintStream out) {
        Order order = new Order();
        while (true) {
            out.println("""
                -- CUSTOMIZING AN ORDER --

                1 - Add Sandwich
                2 - Add Drink
                3 - Add Extra
                4 - View Cart
                5 - Cancel Order""");

            int input = queryCommand(scanner, out, List.of(1, 2, 3, 4, 5));

            switch (input) {
                case 1 -> {
                    var size = querySandwichSize(scanner, out);
                    assert size != null : "SandwichSize enum must have values";
                    var bread = queryBreadType(scanner, out);
                    assert bread != null : "breads.getItems() must have values";
                    var sandwich = new SandwichItem(size, bread);
                    runSandwichEditor(scanner, out, sandwich);
                    order.addSandwich(sandwich);
                }
                case 2 -> {
                    var drink = queryDrinkType(scanner, out);
                    assert drink != null : "drinks.getItems() must have values";
                    var size = queryDrinkSize(scanner, out);
                    assert size != null : "DrinkSize enum must have values";
                    order.addDrink(drink, size);
                }
                case 3 -> order.addExtra(queryExtra(scanner, out));
                case 4 -> {
                    if (runCartView(scanner, out, order))
                        return;
                }
                case 5 -> {
                    if (order.getSandwiches().isEmpty() && order.getDrinks().isEmpty() && order.getExtras().isEmpty())
                        return;

                    out.println("Are you sure you want to cancel the order?");
                    if (queryYN(scanner, out, false))
                        return;
                }
                default -> throw new IllegalStateException("This case should not be reachable.");
            }
        }
    }

    @SuppressWarnings("FeatureEnvy")
    private boolean runCartView(Scanner scanner, PrintStream out, Order order) {
        var receipt = new Receipt(order);
        while (true) {
            out.println(receipt);

            out.println("""
                1 - Modify Sandwich
                2 - Remove Sandwich
                3 - Remove Drink
                4 - Remove Extra
                5 - Check Out
                6 - Exit Cart View""");

            var choice = queryCommand(scanner, out, List.of(1, 2, 3, 4, 5, 6));

            switch (choice) {
                case 1 -> {
                    out.println("Modify which sandwich?");
                    var sandwich = queryListCommand(scanner, out, IntStream.rangeClosed(1, order.getSandwiches().size()).boxed().toList());
                    if (sandwich == null)
                        continue;
                    runSandwichEditor(scanner, out, order.getSandwiches().get(sandwich - 1));
                    return false;
                }
                case 2 -> {
                    out.println("Remove which sandwich?");
                    var sandwich = queryListCommand(scanner, out, IntStream.rangeClosed(1, order.getSandwiches().size()).boxed().toList());
                    if (sandwich == null)
                        continue;
                    order.removeSandwich(sandwich - 1);
                }
                case 3 -> {
                    out.println("Remove which drink?");
                    var drink = queryListCommand(scanner, out, order.getDrinks(), dv -> "%s %s".formatted(dv.getSize(), dv.getType().getName()));
                    if (drink == null)
                        continue;
                    order.removeDrink(drink.getType(), drink.getSize());
                }
                case 4 -> {
                    out.println("Remove which extra?");
                    var extra = queryListCommand(scanner, out, order.getExtras(), ExtraType::getName);
                    if (extra == null)
                        continue;
                    order.removeExtra(extra);
                }
                case 5 -> {
                    return runCheckOut(scanner, out, receipt);
                }
                case 6 -> {
                    return false;
                }
                default -> throw new IllegalStateException("This case should not be reachable.");
            }
        }
    }

    private void runSandwichEditor(Scanner scanner, PrintStream out, SandwichItem sandwich) {

    }
}

/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.app;

import com.pluralsight.io.*;
import com.pluralsight.orders.*;
import com.pluralsight.orders.breads.*;
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
@SuppressWarnings({"ObjectAllocationInLoop", "OverlyComplexClass"})
public final class SandwichShop {
    private final InventoriedFile<ToppingType> toppings;
    private final InventoriedFile<BreadType> breads;
    private final InventoriedFile<DrinkType> drinks;
    private final InventoriedFile<ExtraType> extras;
    private final SignatureFile signatures;

    /**
     * Creates a new shop with the specified inventory files.
     *
     * @param toppings   The file for toppings
     * @param breads     The file for breads
     * @param drinks     The file for drinks
     * @param extras     The file for extras
     * @param signatures The file for signature sandwiches
     */
    @SuppressWarnings({"FeatureEnvy", "IfCanBeAssertion"})
    public SandwichShop(InventoriedFile<ToppingType> toppings,
                        InventoriedFile<BreadType> breads,
                        InventoriedFile<DrinkType> drinks,
                        InventoriedFile<ExtraType> extras,
                        SignatureFile signatures) {
        this.toppings = toppings;
        this.breads = breads;
        this.drinks = drinks;
        this.extras = extras;
        this.signatures = signatures;
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

    private static <T> T queryListCommand(Scanner scanner,
                                          PrintStream out,
                                          List<? extends T> options) {
        return queryListCommand(scanner, out, options, Objects::toString);
    }

    private static <T> T queryListCommand(Scanner scanner,
                                          PrintStream out,
                                          List<? extends T> options,
                                          Function<? super T, String> displaySelector) {
        if (options.isEmpty()) {
            out.println("Nothing to choose");
            return null;
        }

        for (//noinspection ReassignedVariable
            int i = 0; i < options.size(); i++)
            out.printf("%d - %s%n", i + 1, displaySelector.apply(options.get(i)));
        int choice = queryCommand(scanner, out, IntStream.rangeClosed(1, options.size()).boxed().toList());
        return options.get(choice - 1);
    }

    private static <T> Optional<? extends T> queryListCommandOrCancel(Scanner scanner,
                                                                      PrintStream out,
                                                                      Collection<? extends T> options) {
        return queryListCommandOrCancel(scanner, out, options, Objects::toString);
    }

    private static <T> Optional<? extends T> queryListCommandOrCancel(Scanner scanner,
                                                                      PrintStream out,
                                                                      Collection<? extends T> options,
                                                                      Function<? super T, String> displaySelector) {
        return queryListCommand(scanner, out,
            Stream.concat(
                options.stream().map(Optional::of),
                Stream.of(Optional.<T>empty())
            ).toList(),
            op -> op.isEmpty() ? "Cancel" : displaySelector.apply(op.get()));
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

    @SuppressWarnings({"FeatureEnvy", "BooleanMethodNameMustStartWithQuestion"})
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
        out.println("Thanks for eating with us!");

        return true;
    }

    @SuppressWarnings("FeatureEnvy")
    private static String formatTopping(ToppingType type, SandwichSize size) {
        if (type.getPrice(size) == 0 && type.getExtraPrice(size) == 0)
            return type.getName();
        return "%s ($%.2f, $%.2f)".formatted(type.getName(), type.getPrice(size), type.getExtraPrice(size));
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
    private Optional<DrinkType> queryDrinkType(Scanner scanner, PrintStream out) {
        out.println("Enter the drink type:");
        //noinspection unchecked
        return (Optional<DrinkType>) queryListCommandOrCancel(scanner, out, drinks.getItems(),
            dt -> "%s ($%.2fL, $%.2fM, $%.2fS)".formatted(
                dt.getName(),
                dt.getPrice(DrinkSize.LARGE),
                dt.getPrice(DrinkSize.MEDIUM),
                dt.getPrice(DrinkSize.SMALL)
            ));
    }

    private Optional<ExtraType> queryExtra(Scanner scanner, PrintStream out) {
        out.println("Enter the item:");
        //noinspection unchecked
        return (Optional<ExtraType>) queryListCommandOrCancel(scanner, out, extras.getItems(),
            et -> "%s ($%.2f)".formatted(et.getName(), et.getPrice()));
    }

    @SuppressWarnings({"FeatureEnvy", "OverlyComplexMethod", "OverlyLongMethod"})
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

                    out.println("Choose a sandwich:");
                    var sig = queryListCommandOrCancel(scanner, out,
                        Stream.concat(
                            signatures.getSignatures().stream()
                                .filter(ss -> ss.getSandwich().getToppings().stream()
                                    .allMatch(tv -> tv.getType().isInStock())),
                            Stream.of(new SignatureSandwich(null, "Build your own"))
                        ).toList(),
                        SignatureSandwich::getName);

                    assert sig != null : "There is always the default choice";
                    if (sig.isEmpty())
                        continue;
                    SandwichItem sandwich;
                    if ("Build your own".equals(sig.get().getName())) {
                        var bread = queryBreadType(scanner, out);
                        if (bread == null) {
                            out.println("Sorry, we appear to be out of bread right now. Please try again later.");
                            continue;
                        }
                        sandwich = new SandwichItem(size, bread);
                    } else {
                        sandwich = sig.get().getSandwich();
                        sandwich.setSize(size);
                    }

                    runSandwichEditor(scanner, out, sandwich);
                    order.addSandwich(sandwich);
                }
                case 2 -> {
                    var drink = queryDrinkType(scanner, out);
                    if (drink == null) {
                        out.println("Sorry, we appear to be out of drinks right now. Please try again later.");
                        continue;
                    }
                    if (drink.isEmpty())
                        continue;
                    var size = queryDrinkSize(scanner, out);
                    assert size != null : "DrinkSize enum must have values";
                    order.addDrink(drink.get(), size);
                }
                case 3 -> {
                    var extra = queryExtra(scanner, out);
                    if (extra == null) {
                        out.println("Sorry, we appear to be out of extras right now. Please try again later.");
                        continue;
                    }
                    if (extra.isEmpty())
                        continue;
                    order.addExtra(extra.get());
                }
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

    @SuppressWarnings({"FeatureEnvy", "OverlyComplexMethod",
        "OverlyLongMethod", "BooleanMethodNameMustStartWithQuestion"})
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
                    var sandwich = queryListCommandOrCancel(scanner, out,
                        IntStream.rangeClosed(1, order.getSandwiches().size()).boxed().toList());
                    if (sandwich == null || sandwich.isEmpty())
                        continue;
                    runSandwichEditor(scanner, out, order.getSandwiches().get(sandwich.get() - 1));
                    return false;
                }
                case 2 -> {
                    out.println("Remove which sandwich?");
                    var sandwich = queryListCommandOrCancel(scanner, out,
                        IntStream.rangeClosed(1, order.getSandwiches().size()).boxed().toList());
                    if (sandwich == null || sandwich.isEmpty())
                        continue;
                    order.removeSandwich(sandwich.get() - 1);
                }
                case 3 -> {
                    out.println("Remove which drink?");
                    var drink = queryListCommandOrCancel(scanner, out, order.getDrinks(),
                        dv -> "%s %s".formatted(dv.getSize(), dv.getType().getName()));
                    if (drink == null || drink.isEmpty())
                        continue;
                    order.removeDrink(drink.get().getType(), drink.get().getSize());
                }
                case 4 -> {
                    out.println("Remove which extra?");
                    var extra = queryListCommandOrCancel(scanner, out,
                        order.getExtras(), ExtraType::getName);
                    if (extra == null || extra.isEmpty())
                        continue;
                    order.removeExtra(extra.get());
                }
                case 5 -> {
                    if (receipt.getPrice() == 0) {
                        out.println("There's nothing to check out!");
                        continue;
                    }
                    if (runCheckOut(scanner, out, receipt))
                        return true;
                }
                case 6 -> {
                    return false;
                }
                default -> throw new IllegalStateException("This case should not be reachable.");
            }
        }
    }

    @SuppressWarnings({"FeatureEnvy", "OverlyComplexMethod", "OverlyLongMethod", "MethodWithMoreThanThreeNegations"})
    private void runSandwichEditor(Scanner scanner, PrintStream out, SandwichItem sandwich) {
        var receipt = new Receipt(sandwich);
        while (true) {
            out.println("Editing your sandwich:");
            out.println(receipt);
            out.println("""
                1 - Toast
                2 - Add Toppings
                3 - Remove Toppings
                4 - Change Bread
                5 - Change Size
                6 - Done""");

            var choice = queryCommand(scanner, out, List.of(1, 2, 3, 4, 5, 6));

            switch (choice) {
                case 1 -> {
                    out.println("Would you like your sandwich toasted?");
                    sandwich.setToasted(queryYN(scanner, out, !sandwich.isToasted()));
                }
                case 2 -> {
                    out.println("Choose a category:");
                    var category = queryListCommandOrCancel(scanner, out,
                        toppings.getItems().stream()
                            .filter(tp -> sandwich.getToppings().stream().noneMatch(st -> st.getType() == tp))
                            .map(ToppingType::getCategory).distinct().toList());
                    if (category == null) {
                        out.println("No more toppings to add :(");
                        continue;
                    }
                    if (category.isEmpty())
                        continue;
                    out.println("Choose a topping:");
                    var topping = queryListCommandOrCancel(scanner, out,
                        toppings.getItems().stream()
                            .filter(tp -> sandwich.getToppings().stream().noneMatch(st -> st.getType() == tp))
                            .filter(t -> t.getCategory().equals(category.get())).toList(),
                        tt -> formatTopping(tt, sandwich.getSize()));
                    assert topping != null : "toppings.getItems() must have values after we just used it";
                    if (topping.isEmpty())
                        continue;
                    out.println("Would you like extra?");
                    var extra = queryYN(scanner, out, null);
                    sandwich.addTopping(topping.get(), false);
                    if (extra)
                        sandwich.addTopping(topping.get(), true);
                }
                case 3 -> {
                    out.println("Which topping do you want to remove?");
                    var topping = queryListCommandOrCancel(scanner, out, sandwich.getToppings(),
                        tv -> "%s%s".formatted(tv.isExtra() ? "Extra " : "", tv.getType().getName()));
                    if (topping == null || topping.isEmpty())
                        continue;
                    sandwich.removeTopping(topping.get().getType(), topping.get().isExtra());
                    if (!topping.get().isExtra() && sandwich.getToppings().stream()
                        .anyMatch(tp -> tp.getType() == topping.get().getType() && tp.isExtra()))
                        sandwich.removeTopping(topping.get().getType(), true);
                }
                case 4 -> {
                    var bread = queryBreadType(scanner, out);
                    if (bread == null) {
                        out.println("Sorry, we appear to be out of bread right now. Please try again later.");
                        continue;
                    }
                    sandwich.setBread(bread);
                }
                case 5 -> {
                    var size = querySandwichSize(scanner, out);
                    assert size != null : "SandwichSize enum must have values";
                    sandwich.setSize(size);
                }
                case 6 -> {
                    return;
                }
                default -> throw new IllegalStateException("This case should not be reachable.");
            }
        }
    }
}

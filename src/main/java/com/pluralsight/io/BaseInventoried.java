/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight.io;

/**
 * Base type for {@link Inventoried} items managed by a subclass of {@link BaseInventoriedFile}.
 */
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
public abstract class BaseInventoried implements Inventoried {
    private BaseInventoriedFile<?> owner;
    private int amount;

    @Override
    public void consume(int amount) {
        this.amount -= amount;
        owner.save();
    }

    @Override
    public boolean isInStock() {
        return amount > 0;
    }

    int getAmount() {
        return amount;
    }

    void setAmount(int amount) {
        this.amount = amount;
    }

    void setOwner(BaseInventoriedFile<?> owner) {
        this.owner = owner;
    }
}

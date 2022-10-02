package me.cable.donationslistener.component.donation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record IdentifiableDonation(int id, @NotNull String user, @Nullable String message, double amount,
                                   @NotNull String currency) implements Donation {

    public IdentifiableDonation(int id, @NotNull String user, @Nullable String message, double amount) {
        this(id, user, message, amount, DEFAULT_CURRENCY);
    }

    public IdentifiableDonation(int id, @NotNull String user, @Nullable String message) {
        this(id, user, message, 1);
    }

    public IdentifiableDonation(int id, @NotNull String user) {
        this(id, user, null);
    }
}

package me.cable.donationslistener.component.donation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record UnidentifiableDonation(@NotNull String user, @Nullable String message, double amount,
                                     @NotNull String currency) implements Donation {

    public UnidentifiableDonation(@NotNull String user, @Nullable String message, double amount) {
        this(user, message, amount, DEFAULT_CURRENCY);
    }

    public UnidentifiableDonation(@NotNull String user, @Nullable String message) {
        this(user, message, 1);
    }

    public UnidentifiableDonation(@NotNull String user) {
        this(user, null);
    }
}

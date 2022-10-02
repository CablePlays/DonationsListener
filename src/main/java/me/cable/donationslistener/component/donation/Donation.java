package me.cable.donationslistener.component.donation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Donation {

    String DEFAULT_CURRENCY = "USD";

    @NotNull String user();

    @Nullable String message();

    double amount();

    @NotNull String currency();
}

package me.cable.donationslistener.util;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class Utils {

    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));

    public static @NotNull String formatMoney(double value) {
        return MONEY_FORMAT.format(value);
    }
}

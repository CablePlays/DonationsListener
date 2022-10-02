package me.cable.donationslistener.handler;

import me.cable.donationslistener.action.Action;
import me.cable.donationslistener.component.donation.Donation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ActionHandler {

    private static final List<Action> actions = new ArrayList<>();

    public static void registerAction(@NotNull Action action) {
        if (actions.contains(action)) {
            throw new IllegalArgumentException("Action has already been registered");
        }

        actions.add(action);
    }

    public static void post(@NotNull Donation donation) {
        for (Action action : actions) {
            if (action.shouldRun(donation)) {
                action.run(donation);
            }
        }
    }
}

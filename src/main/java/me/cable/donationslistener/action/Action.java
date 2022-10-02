package me.cable.donationslistener.action;

import me.cable.donationslistener.component.donation.Donation;
import me.cable.donationslistener.handler.ActionHandler;
import org.jetbrains.annotations.NotNull;

public abstract class Action {

    public Action() { // register on instantiation
        ActionHandler.registerAction(this);
    }

    public abstract boolean shouldRun(@NotNull Donation donation);

    public abstract void run(@NotNull Donation donation);
}

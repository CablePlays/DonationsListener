package me.cable.donationslistener.action;

import me.cable.donationslistener.component.donation.Donation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/*
    Spawn n creepers at each player where n is the number value of the donation.
    Would be better to convert donation amount to a single currency to make it fair.
 */
public class CreeperAction extends Action {

    @Override
    public boolean shouldRun(@NotNull Donation donation) {
        String message = donation.message();
        return (message != null) && message.toLowerCase(Locale.ROOT).contains("creeper");
    }

    @Override
    public void run(@NotNull Donation donation) {
        int amount = (int) donation.amount();
        if (amount <= 0) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.DARK_GREEN + "Spawning creeper" + (amount == 1 ? "" : "s") + "!");

            for (int i = 0; i < amount; i++) {
                player.getWorld().spawn(player.getLocation(), Creeper.class);
            }
        }
    }
}

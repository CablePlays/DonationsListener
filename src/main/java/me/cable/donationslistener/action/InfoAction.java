package me.cable.donationslistener.action;

import me.cable.donationslistener.component.donation.Donation;
import me.cable.donationslistener.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InfoAction extends Action {

    @Override
    public boolean shouldRun(@NotNull Donation donation) {
        return true;
    }

    @Override
    public void run(@NotNull Donation donation) {
        String message = "Donation received: " + Utils.formatMoney(donation.amount()) + " " + donation.currency() + " from " + donation.user()
                + " with message \"" + donation.message() + "\"";
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.GOLD + message);
        }
    }
}

package me.cable.donationslistener.action;

import me.cable.donationslistener.component.donation.Donation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class HelloWorldAction extends Action {

    @Override
    public boolean shouldRun(@NotNull Donation donation) {
        String message = donation.message();
        return (message != null) && message.toLowerCase(Locale.ROOT).contains("hello world");
    }

    @Override
    public void run(@NotNull Donation donation) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.GOLD + "Hello World! " + ChatColor.WHITE + "from chat");
        }
    }
}

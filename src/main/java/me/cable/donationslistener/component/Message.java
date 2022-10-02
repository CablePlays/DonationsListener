package me.cable.donationslistener.component;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Message {

    private final List<String> lines;
    private final Map<String, String> placeholders = new HashMap<>();

    public Message(@NotNull List<String> lines) {
        this.lines = lines;
    }

    public @NotNull Message placeholder(@Nullable String placeholder, @Nullable String value) {
        if (placeholder != null && value != null) {
            placeholders.put(placeholder, value);
        }

        return this;
    }

    public void send(@NotNull CommandSender commandSender) {
        for (String line : lines) {
            for (Entry<String, String> entry : placeholders.entrySet()) {
                line = line.replace(entry.getKey(), entry.getValue());
            }

            line = ChatColor.translateAlternateColorCodes('&', line);
            commandSender.sendMessage(line);
        }
    }
}

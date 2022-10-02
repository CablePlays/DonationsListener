package me.cable.donationslistener.handler;

import me.cable.donationslistener.DonationsListener;
import me.cable.donationslistener.component.Loadable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class Settings implements Loadable {

    private static final String FILE_NAME = "config.yml";

    private final DonationsListener donationsListener;

    private YamlConfiguration config;
    private final File file;

    public Settings(@NotNull DonationsListener donationsListener) {
        this.donationsListener = donationsListener;
        file = new File(donationsListener.getDataFolder(), FILE_NAME);
        load(null);
    }

    @Override
    public void load(@Nullable Player player) {
        if (!file.exists()) {
            donationsListener.saveResource(FILE_NAME, false);
        }

        config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (InvalidConfigurationException e) {
            if (player != null) {
                player.sendMessage(ChatColor.RED + "Invalid formatting in " + FILE_NAME);
            }

            DonationsListener.warn("Invalid formatting in " + FILE_NAME);
            e.printStackTrace();
        } catch (IOException e) {
            if (player != null) {
                player.sendMessage(ChatColor.RED + "Could not load " + FILE_NAME);
            }

            DonationsListener.warn("Could not load " + FILE_NAME);
            e.printStackTrace();
        }
    }

    public long checkInterval() {
        return config.getLong("check-interval");
    }

    public @Nullable String streamlabsAccessToken() {
        return config.getString("streamlabs-access-token");
    }
}

package me.cable.donationslistener;

import me.cable.donationslistener.action.CreeperAction;
import me.cable.donationslistener.action.HelloWorldAction;
import me.cable.donationslistener.action.TestAction;
import me.cable.donationslistener.command.MainCommand;
import me.cable.donationslistener.handler.Messages;
import me.cable.donationslistener.handler.Settings;
import me.cable.donationslistener.network.StreamlabsConnector;
import me.cable.donationslistener.ticker.DonationChecker;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class DonationsListener extends JavaPlugin {

    private Settings settings;
    private Messages messages;
    private StreamlabsConnector streamlabsConnector;

    private DonationChecker donationChecker;

    /*
        For internal use only.
     */
    public static void warn(@NotNull String message) {
        JavaPlugin.getPlugin(DonationsListener.class).getLogger().warning(message);
    }

    @Override
    public void onEnable() {
        loadHelpFile();
        initializeHandlers();
        registerCommands();
        registerActions();
        startDonationChecker();
    }

    public void reload() {
        loadHelpFile();
        startDonationChecker();
    }

    private void loadHelpFile() {
        saveResource("help.md", true);
    }

    private void initializeHandlers() {
        settings = new Settings(this);
        messages = new Messages(this);
        streamlabsConnector = new StreamlabsConnector(this);
    }

    private void registerCommands() {
        new MainCommand(this).register("donationslistener");
    }

    private void startDonationChecker() {
        if (donationChecker != null) {
            donationChecker.cancel();
        }

        donationChecker = new DonationChecker(this);
        long checkInterval = settings.checkInterval() * 20;
        donationChecker.runTaskTimer(this, checkInterval, checkInterval);
    }

    private void registerActions() {
        new CreeperAction();
        new HelloWorldAction();
        new TestAction();
    }

    public Settings getSettings() {
        return settings;
    }

    public Messages getMessages() {
        return messages;
    }

    public StreamlabsConnector getStreamlabsConnector() {
        return streamlabsConnector;
    }
}

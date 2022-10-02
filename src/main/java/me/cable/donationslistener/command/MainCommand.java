package me.cable.donationslistener.command;

import me.cable.donationslistener.DonationsListener;
import me.cable.donationslistener.component.Loadable;
import me.cable.donationslistener.component.donation.Donation;
import me.cable.donationslistener.component.donation.UnidentifiableDonation;
import me.cable.donationslistener.handler.ActionHandler;
import me.cable.donationslistener.handler.Messages;
import me.cable.donationslistener.handler.Settings;
import me.cable.donationslistener.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements TabExecutor {

    private final DonationsListener donationsListener;
    private final Settings settings;
    private final Messages messages;

    public MainCommand(@NotNull DonationsListener donationsListener) {
        this.donationsListener = donationsListener;
        settings = donationsListener.getSettings();
        messages = donationsListener.getMessages();
    }

    public void register(@NotNull String label) {
        PluginCommand pluginCommand = donationsListener.getCommand(label);

        if (pluginCommand == null) {
            throw new IllegalStateException("Plugin command was not created");
        }

        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String path = "command.donationslistener";
        int length = args.length;

        if (length == 0) {
            PluginDescriptionFile pluginDescriptionFile = donationsListener.getDescription();
            messages.get(path + ".information")
                    .placeholder("{command}", label)
                    .placeholder("{name}", pluginDescriptionFile.getName())
                    .placeholder("{version}", pluginDescriptionFile.getVersion())
                    .send(sender);
        } else switch (args[0]) {
            case "help" -> messages.get(path + ".help")
                    .placeholder("{command}", label)
                    .send(sender);
            case "reload" -> {
                long time = System.currentTimeMillis();
                Player player = (sender instanceof Player a) ? a : null;

                for (Loadable loadable : List.of(settings, messages)) {
                    loadable.load(player);
                }

                donationsListener.reload();
                messages.get(path + ".reload")
                        .placeholder("{milliseconds}", Long.toString(System.currentTimeMillis() - time))
                        .send(sender);
            }
            case "trigger" -> {
                String user = "Anonymous";
                double amount = 1;
                String currency = Donation.DEFAULT_CURRENCY;
                StringBuilder message = new StringBuilder();

                for (int i = 1; i < args.length; i++) {
                    String arg = args[i];

                    if (arg.startsWith("user:")) {
                        user = arg.substring("user:".length());
                    } else if (arg.startsWith("amount:")) {
                        String amountString = arg.substring("amount:".length());

                        try {
                            amount = Double.parseDouble(amountString);
                        } catch (NumberFormatException ex) {
                            // ignored
                        }
                    } else if (arg.startsWith("currency:")) {
                        currency = arg.substring("currency:".length());
                    } else {
                        if (!message.isEmpty()) {
                            message.append(' ');
                        }

                        message.append(arg);
                    }
                }

                String messageString = message.toString();
                UnidentifiableDonation donation = new UnidentifiableDonation(user, messageString, amount, currency);
                messages.get(path + ".trigger")
                        .placeholder("{amount}", Utils.formatMoney(amount))
                        .placeholder("{currency}", currency)
                        .placeholder("{message}", messageString)
                        .placeholder("{user}", user)
                        .send(sender);
                ActionHandler.post(donation);
            }
            default -> messages.get(path + ".unknown-command")
                    .placeholder("{command}", label)
                    .send(sender);
        }

        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> result = new ArrayList<>();
        int length = args.length;

        if (length == 1) {
            for (String a : List.of("help", "reload", "trigger")) {
                if (a.startsWith(args[0])) {
                    result.add(a);
                }
            }
        } else if (args[0].equals("trigger")) {
            List<String> list = new ArrayList<>(List.of("amount:", "currency:", "user:"));

            for (int i = 1; i < args.length; i++) {
                String arg = args[i];

                for (String s : list) {
                    if (arg.startsWith(s)) {
                        list.remove(s);
                        break;
                    }
                }
            }

            String lastArg = args[args.length - 1];

            for (String s : list) {
                if (s.startsWith(lastArg)) {
                    result.add(s);
                }
            }
        }

        return result;
    }
}

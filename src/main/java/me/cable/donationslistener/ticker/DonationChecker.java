package me.cable.donationslistener.ticker;

import me.cable.donationslistener.DonationsListener;
import me.cable.donationslistener.component.donation.Donation;
import me.cable.donationslistener.component.donation.IdentifiableDonation;
import me.cable.donationslistener.handler.ActionHandler;
import me.cable.donationslistener.network.StreamlabsConnector;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DonationChecker extends BukkitRunnable {

    private final StreamlabsConnector streamlabsConnector;

    public DonationChecker(@NotNull DonationsListener donationsListener) {
        streamlabsConnector = donationsListener.getStreamlabsConnector();
    }

    @Override
    public void run() {
        List<IdentifiableDonation> donations = streamlabsConnector.getRecentDonations();
        if (donations == null) return;

        Collections.reverse(donations); // make oldest to newest

        for (Donation donation : donations) {
            ActionHandler.post(donation);
        }
    }
}

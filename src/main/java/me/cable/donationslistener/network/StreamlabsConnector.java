package me.cable.donationslistener.network;

import com.google.gson.Gson;
import me.cable.donationslistener.DonationsListener;
import me.cable.donationslistener.component.donation.IdentifiableDonation;
import me.cable.donationslistener.handler.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class StreamlabsConnector {

    private final Settings settings;

    private boolean initialQuery = true;
    private @Nullable Integer latestDonationId;

    public StreamlabsConnector(@NotNull DonationsListener donationsListener) {
        settings = donationsListener.getSettings();
    }

    /**
     * Gets all donations created after the previous query.
     * If there is no previous query,
     * only a single donation will attempt to be fetched and will be used as the latest donation ID,
     * and no donations will be returned.
     *
     * @return new donations, or null if the donations could not be fetched.
     */
    public @Nullable List<IdentifiableDonation> getRecentDonations() {
        String accessToken = settings.streamlabsAccessToken();
        if (accessToken == null || accessToken.isBlank()) return null;

        HttpRequest request = createRequest(accessToken, initialQuery ? 1 : null, latestDonationId);
        HttpResponse<String> response;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            DonationsListener.warn("Could not check for new donations");
            e.printStackTrace();
            return null;
        }

        ReturnedDonations returnedDonations = new Gson().fromJson(response.body(), ReturnedDonations.class);

        if (returnedDonations.data == null) {
            DonationsListener.warn("Could not get new donations: " + returnedDonations.error);
            DonationsListener.warn("Could not get new donations: " + returnedDonations.message);
            return null;
        }

        List<IdentifiableDonation> donations = new ArrayList<>();

        for (ReturnedDonation returnedDonation : returnedDonations.data) {
            donations.add(new IdentifiableDonation(
                    returnedDonation.donation_id,
                    returnedDonation.name,
                    returnedDonation.message,
                    returnedDonation.amount,
                    returnedDonation.currency
            ));
        }

        if (!donations.isEmpty()) {
            IdentifiableDonation latest = donations.get(0);
            latestDonationId = latest.id();
        }
        if (initialQuery) {
            initialQuery = false;
            return new ArrayList<>();
        }

        return donations;
    }

    private @NotNull HttpRequest createRequest(@NotNull String accessToken, @Nullable Integer limit, @Nullable Integer after) {
        StringBuilder urlBuilder = new StringBuilder("https://streamlabs.com/api/v1.0/donations?access_token=" + accessToken);

        if (limit != null) {
            urlBuilder.append("&limit=").append(limit);
        }
        if (after != null) {
            urlBuilder.append("&after=").append(after);
        }

        return HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder.toString()))
                .header("accept", "application/json")
                .method("POST", HttpRequest.BodyPublishers.noBody())
                .build();
    }

    private static class ReturnedDonation {
        int donation_id;
        String currency;
        double amount;
        String name;
        String message;
    }

    private static class ReturnedDonations {
        ReturnedDonation[] data;
        String error;
        String message;
    }
}

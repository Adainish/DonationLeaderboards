package io.github.adainish.donationleaderboards.sponge;

import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.obj.Donator;
import io.github.adainish.donationleaderboards.storage.DonatorStorage;
import net.craftingstore.sponge.events.DonationReceivedEvent;
import org.spongepowered.api.event.Listener;

import java.util.UUID;

public class DonationListener {

    @Listener
    public void onDonationReceived(DonationReceivedEvent event) {
        Donator donator = DonationLeaderboards.wrapper.getDonator(event.getDonation().getPlayer().getUUID());
        donator.increaseAmount(event.getDonation().getPackage().getPriceInCents());
        UUID uuid = event.getDonation().getPlayer().getUUID();
        if (DonationLeaderboards.wrapper.donators.containsKey(uuid)) {
            DonationLeaderboards.wrapper.donators.replace(uuid, donator);
        } else DonationLeaderboards.wrapper.donators.put(uuid, donator);

        DonatorStorage.saveDonatorData(donator);
        DonationLeaderboards.wrapper.updateDonatorSpotData();
    }
}

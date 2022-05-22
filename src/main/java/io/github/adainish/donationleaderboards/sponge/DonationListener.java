package io.github.adainish.donationleaderboards.sponge;

import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.obj.Donator;
import io.github.adainish.donationleaderboards.storage.DonatorStorage;
import net.craftingstore.sponge.events.DonationReceivedEvent;
import org.spongepowered.api.event.Listener;

public class DonationListener {


    @Listener
    public void onDonationReceived(DonationReceivedEvent event) {
        Donator donator = DonationLeaderboards.wrapper.getDonator(event.getDonation().getPlayer().getUUID());
        donator.increaseAmount(event.getDonation().getPackage().getPriceInCents());
        DonationLeaderboards.wrapper.donators.replace(event.getDonation().getPlayer().getUUID(), donator);
        DonatorStorage.saveDonatorData(donator);
        DonationLeaderboards.wrapper.updateDonatorSpotData();
    }
}

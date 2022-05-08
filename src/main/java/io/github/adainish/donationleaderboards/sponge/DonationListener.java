package io.github.adainish.donationleaderboards.sponge;

import io.github.adainish.donationleaderboards.DonationLeaderboards;
import net.craftingstore.sponge.events.DonationReceivedEvent;
import org.spongepowered.api.event.Listener;

public class DonationListener {


    @Listener
    public void onDonationReceived(DonationReceivedEvent event) {

        DonationLeaderboards.log.info("Fake purchase received");
    }
}

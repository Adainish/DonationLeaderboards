package io.github.adainish.donationleaderboards.listeners;


import io.github.adainish.donationleaderboards.DonationLeaderboards;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerListener {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player == null)
            return;

        DonationLeaderboards.wrapper.donatorCache(event.player.getUniqueID(), true);
    }

    @SubscribeEvent
    public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player == null)
            return;

        DonationLeaderboards.wrapper.donatorCache(event.player.getUniqueID(), false);
    }

}

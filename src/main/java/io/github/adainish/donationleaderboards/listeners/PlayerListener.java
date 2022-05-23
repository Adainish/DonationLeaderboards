package io.github.adainish.donationleaderboards.listeners;


import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import io.github.adainish.donationleaderboards.DonationLeaderboards;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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


    @SubscribeEvent
    public void onEntityInteractEvent(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() == null)
            return;

        if (event.getTarget() instanceof NPCChatting) {
            NPCChatting npc = (NPCChatting) event.getTarget();
            DonationLeaderboards.log.info(npc.getTextureIndex());
            DonationLeaderboards.log.info(npc.getCustomSteveTexture());
        }
    }

}

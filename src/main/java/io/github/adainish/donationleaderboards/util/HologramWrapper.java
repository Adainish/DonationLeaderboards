package io.github.adainish.donationleaderboards.util;

import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.obj.Hologram;
import io.github.adainish.donationleaderboards.wrapper.LeaderBoardWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HologramWrapper {

    public HologramWrapper() {}


    public Hologram createHologram(World world, BlockPos pos, String title)  {
        EntityArmorStand stand = createArmorStand(world, pos, title);
        Hologram h = new Hologram();
        h.setIdentifier(String.valueOf(stand.getEntityId()));
        h.setEntityID(stand.getEntityId());
        h.setWorldID(stand.world.provider.getDimension());
        h.setX(stand.posX);
        h.setY(stand.posY);
        h.setZ(stand.posZ);
        h.setDisplay(title);
        return h;
    }


    private EntityArmorStand createArmorStand(World world, BlockPos pos, String title) {
        EntityArmorStand entityArmorStand;
        entityArmorStand = new EntityArmorStand(world);
        entityArmorStand.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
        world.spawnEntity(entityArmorStand);
        entityArmorStand.setInvisible(true);
        entityArmorStand.setEntityInvulnerable(true);
        entityArmorStand.setNoGravity(true);
        entityArmorStand.setCustomNameTag(Util.formattedString(title));
        entityArmorStand.setAlwaysRenderNameTag(true);
        entityArmorStand.setSilent(true);
        return entityArmorStand;
    }

    public Hologram getHologram(World world, int entityID) {
        Hologram hologram = null;
        for (Hologram h:DonationLeaderboards.wrapper.getLeaderboard().hologramList) {
            if (h.getEntityID() == entityID)
                hologram = h;
                break;
        }
        return hologram;
    }

    public Hologram getHologram(String identifier) {
        Hologram hologram = null;
        for (Hologram h:DonationLeaderboards.wrapper.getLeaderboard().hologramList) {
            if (h.getIdentifier().equals(identifier))
                hologram = h;
            break;
        }
        return hologram;
    }

    public EntityArmorStand getArmorStand(World world, int entityID) {
        EntityArmorStand result = null;
        Entity entity = world.getEntityByID(entityID);
        if (entity != null) {
            if (entity instanceof EntityArmorStand) {
                result = (EntityArmorStand) entity;
            }
        }
        return result;
    }

    public void updateHologram(World world, int entityID, String display) {
        EntityArmorStand stand;
        stand = getArmorStand(world, entityID);
        if (stand != null) {
            DonationLeaderboards.log.info("Updated Donation Leaderboards Hologram");
            stand.setCustomNameTag(Util.formattedString(display));
        } else {
            DonationLeaderboards.log.info("Tried updating a donation leaderboards hologram but the provided entity did not exist");
        }
        for (int i = 0, hologramListSize = DonationLeaderboards.wrapper.getLeaderboard().hologramList.size(); i < hologramListSize; i++) {
            Hologram h = DonationLeaderboards.wrapper.getLeaderboard().hologramList.get(i);
            if (h.getEntityID() == entityID) {
                DonationLeaderboards.wrapper.getLeaderboard().hologramList.remove(i);
                h.setDisplay(display);
                DonationLeaderboards.wrapper.getLeaderboard().hologramList.add(h);
                DonationLeaderboards.wrapper.saveLeaderBoard();
                break;
            }
        }
    }


    public void killHologram(World world, int entityID) {
        EntityArmorStand stand;
        stand = getArmorStand(world, entityID);
        if (stand != null) {
            DonationLeaderboards.log.info("Deleted Donation Leaderboards Hologram");
            stand.setDead();
        } else {
            DonationLeaderboards.log.info("Tried deleting a donation leaderboards hologram but the provided entity did not exist");
        }
        for (int i = 0, hologramListSize = DonationLeaderboards.wrapper.getLeaderboard().hologramList.size(); i < hologramListSize; i++) {
            Hologram h = DonationLeaderboards.wrapper.getLeaderboard().hologramList.get(i);
            if (h.getEntityID() == entityID) {
                DonationLeaderboards.wrapper.getLeaderboard().hologramList.remove(h);
                DonationLeaderboards.wrapper.saveLeaderBoard();
                break;
            }
        }
    }
}

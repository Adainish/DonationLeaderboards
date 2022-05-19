package io.github.adainish.donationleaderboards.obj;

import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GeneralNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.config.DonatorSpotConfig;
import io.github.adainish.donationleaderboards.util.ProfileFetcher;
import io.github.adainish.donationleaderboards.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DonatorSpot {

    private UUID uuid;
    private int npcID;
    private int worldID;
    private double x;
    private double y;
    private double z;
    private int rankingNumber;
    private List <String> displayText = new ArrayList <>();
    private String hologramDisplay = "";

    public DonatorSpot(String identifier) {
        setWorldID(DonatorSpotConfig.getConfig().get().getNode("DonatorSpot", identifier, "WorldID").getInt());
        setX(DonatorSpotConfig.getConfig().get().getNode("DonatorSpot", identifier, "X").getDouble());
        setY(DonatorSpotConfig.getConfig().get().getNode("DonatorSpot", identifier, "Y").getDouble());
        setZ(DonatorSpotConfig.getConfig().get().getNode("DonatorSpot", identifier, "Z").getDouble());
        setRankingNumber(DonatorSpotConfig.getConfig().get().getNode("DonatorSpot", identifier, "Ranking").getInt());
        spawnNPC();
    }

    public DonatorSpot(String identifier, int rankingNumber) {

    }
    public DonatorSpot(int worldID, double x, double y, double z, int rankingNumber) {
        setWorldID(worldID);
        setX(x);
        setY(y);
        setZ(z);
        setRankingNumber(rankingNumber);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getWorldID() {
        return worldID;
    }

    public void setWorldID(int worldID) {
        this.worldID = worldID;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getRankingNumber() {
        return rankingNumber;
    }

    public void setRankingNumber(int rankingNumber) {
        this.rankingNumber = rankingNumber;
    }

    public List <String> getDisplayText() {
        return displayText;
    }

    public void setDisplayText(List <String> displayText) {
        this.displayText = displayText;
    }

    public String getHologramDisplay() {
        return hologramDisplay;
    }

    public void setHologramDisplay(String hologramDisplay) {
        this.hologramDisplay = hologramDisplay;
    }

    public void killNPC() {
        World world = Util.getInstance().getWorld(worldID);
        Entity entity = world.getEntityByID(getNpcID());
        if (entity == null) {
            return;
        }

        entity.setDead();
    }

    public void spawnNPC() {
        World world = Util.getInstance().getWorld(worldID);
        if (world.getWorldInfo().getWorldName().isEmpty()) {
            DonationLeaderboards.log.info("Something went wrong loading the NPC world, did not spawn the NPC");
            return;
        }
        NPCChatting npc = new NPCChatting(world);
        GeneralNPCData data = ServerNPCRegistry.villagers.getRandom();
        if (uuid == null)
        npc.init(data);
        else {
            try {
                npc.init(ProfileFetcher.getName(uuid));
                npc.setCustomSteveTexture(ProfileFetcher.getName(uuid));
            } catch (IOException e) {
                npc.init(data);
                DonationLeaderboards.log.info("Failed to connect with mojang, set random NPC as skin");
            }
            catch (NullPointerException e) {
                npc.init(data);
                DonationLeaderboards.log.info("Failed to verify Donator Spot Account with mojang, set random NPC as skin");
            }
        }
        world.spawnEntity(npc);
        npc.initDefaultAI();
        npc.setPosition(getX(), getY(), getZ());
        setNpcID(npc.getId());
    }

    public int getNpcID() {
        return npcID;
    }

    public void setNpcID(int npcID) {
        this.npcID = npcID;
    }
}

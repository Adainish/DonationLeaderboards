package io.github.adainish.donationleaderboards.obj;

import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GeneralNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.config.DonatorSpotConfig;
import io.github.adainish.donationleaderboards.util.ProfileFetcher;
import io.github.adainish.donationleaderboards.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
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
    private int hologramID;
    private int rankingNumber;
    private List <String> displayText = new ArrayList <>();
    private String hologramDisplay = "Example Text";

    public DonatorSpot(String identifier) {
        setWorldID(DonatorSpotConfig.getConfig().get().getNode("DonatorSpot", identifier, "WorldID").getInt());
        setX(DonatorSpotConfig.getConfig().get().getNode("DonatorSpot", identifier, "X").getDouble());
        setY(DonatorSpotConfig.getConfig().get().getNode("DonatorSpot", identifier, "Y").getDouble());
        setZ(DonatorSpotConfig.getConfig().get().getNode("DonatorSpot", identifier, "Z").getDouble());
        setRankingNumber(DonatorSpotConfig.getConfig().get().getNode("DonatorSpot", identifier, "Ranking").getInt());
        setHologramDisplay(DonatorSpotConfig.getConfig().get().getNode("DonatorSpot", identifier, "Display").getString());
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

    public void updateHologram(World world) {
        Hologram hologram;
        hologram = DonationLeaderboards.hologramWrapper.getHologram(String.valueOf(hologramID));

        try {
            if (hologram != null) {
                DonationLeaderboards.hologramWrapper.updateHologram(world, hologram.getEntityID(), getHologramDisplay());
            } else {
                throw new Exception("Hologram did not exist or could not be found, can't update it");
            }
        } catch (Exception e) {
            DonationLeaderboards.log.error(e.getMessage());
        }

    }

    public void setNPCSteve(NPCChatting npc, String username) {
        npc.setTextureIndex(5);
        npc.setCustomSteveTexture(username.replaceAll("\"", ""));
    }

    public void updateNPC() {
        try {
            World world = Util.getInstance().getWorld(worldID);
            Entity entity = world.getEntityByID(getNpcID());

            if (entity == null) {
                throw new Exception("Entity did not exist or could not be found, can't update it");
            }

            NPCChatting npc = null;
            if (entity instanceof EntityNPC) {
                npc = (NPCChatting) entity;
            } else {
                throw new Exception("Entity was not instance of NPCChatting, was it moved? Did not update. Worst case reset the leaderboard npc storage");
            }
            npc.setName("Donator Rank: " + getRankingNumber());
            npc.setChat(new ArrayList <>());
            if (uuid != null) {
                try {
                    String username = ProfileFetcher.getName(uuid);
                    setNPCSteve(npc, username);
                } catch (IOException e) {
                    DonationLeaderboards.log.info("Failed to connect with mojang, did not update NPC");
                } catch (NullPointerException e) {
                    DonationLeaderboards.log.info("Failed to verify Donator Spot Account with mojang, did not update NPC");
                }
            }
            updateHologram(world);
        } catch (Exception e) {
            DonationLeaderboards.log.error(e.getMessage());
        }
    }

    public void killNPC() {
        try {
            World world = Util.getInstance().getWorld(worldID);
            Entity entity = world.getEntityByID(getNpcID());
            if (entity == null) {
                throw new Exception("Entity did not exist or could not be found, can't remove it");
            }
            entity.setDead();
            Hologram hologram;
            hologram = DonationLeaderboards.hologramWrapper.getHologram(String.valueOf(hologramID));

            if (hologram != null) {
                DonationLeaderboards.hologramWrapper.killHologram(world, getHologramID());
            } else {
                throw new Exception("Hologram did not exist or could not be found, can't remove it");
            }

        } catch (Exception e) {
            DonationLeaderboards.log.error(e.getMessage());
        }
    }

    public NPCChatting createNPCChatting(World world) {
        NPCChatting npc = new NPCChatting(world);
        npc.initDefaultAI();
        npc.setPositionAndUpdate(getX(), getY(), getZ());
        npc.getEntityData().setBoolean("donatorNPC", true);
        npc.enablePersistence();
        world.spawnEntity(npc);
        return npc;
    }

    public void spawnNPC(NPCChatting npc) {
        World world = Util.getInstance().getWorld(worldID);


        if (world.getWorldInfo().getWorldName().isEmpty()) {
            DonationLeaderboards.log.info("Something went wrong loading the NPC world, did not spawn the NPC");
            return;
        }
        BlockPos pos1 = new BlockPos(getX(), getY() + 0.5, getZ());
        BlockPos pos2 = new BlockPos(getX() + 30, getY() + 100, getZ() + 30);
        if (!world.isAreaLoaded(pos1, pos2)) {
            DonationLeaderboards.log.info("Area wasn't loaded, delaying npc reloading");
            return;
        }


        Hologram hologram = DonationLeaderboards.hologramWrapper.createHologram(world, pos1, hologramDisplay);
        setHologramID(hologram.getEntityID());
        DonationLeaderboards.wrapper.getLeaderboard().hologramList.add(hologram);
        GeneralNPCData data = ServerNPCRegistry.villagers.getRandom();
        setNpcID(npc.getEntityId());
        if (uuid == null) {
            npc.init(data);
            npc.setCustomSteveTexture(data.getRandomTexture());
            npc.setChat(new ArrayList <>());
            npc.setName("Donator Rank: " + getRankingNumber());
        }
        else {
            npc.setChat(new ArrayList <>());
            npc.setName("Donator Rank: " + getRankingNumber());
            try {
                String username = ProfileFetcher.getName(uuid);
                setNPCSteve(npc, username);
            } catch (IOException e) {
                npc.init(data);
                npc.setCustomSteveTexture(data.getRandomTexture());
                DonationLeaderboards.log.info("Failed to connect with mojang, set random NPC as skin");
            } catch (NullPointerException e) {
                npc.init(data);
                npc.setName("Donator Rank: " + getRankingNumber());
                DonationLeaderboards.log.info("Failed to verify Donator Spot Account with mojang, set random NPC as skin");
            }
        }
    }

    public void spawnNPC() {
        World world = Util.getInstance().getWorld(worldID);


        if (world.getWorldInfo().getWorldName().isEmpty()) {
            DonationLeaderboards.log.info("Something went wrong loading the NPC world, did not spawn the NPC");
            return;
        }
        BlockPos pos1 = new BlockPos(getX(), getY() + 0.5, getZ());
        BlockPos pos2 = new BlockPos(getX() + 30, getY() + 100, getZ() + 30);
        if (!world.isAreaLoaded(pos1, pos2)) {
            DonationLeaderboards.log.info("Area wasn't loaded, delaying npc reloading");
            return;
        }

        Hologram hologram = DonationLeaderboards.hologramWrapper.createHologram(world, pos1, hologramDisplay);
        setHologramID(hologram.getEntityID());
        DonationLeaderboards.wrapper.getLeaderboard().hologramList.add(hologram);
        NPCChatting npc = createNPCChatting(world);
        GeneralNPCData data = ServerNPCRegistry.villagers.getRandom();
        setNpcID(npc.getEntityId());
        if (uuid == null) {
            npc.init(data);
            npc.setCustomSteveTexture(data.getRandomTexture());
            npc.setChat(new ArrayList <>());
            npc.setName("Donator Rank: " + getRankingNumber());
        }
        else {
            try {
                String username = ProfileFetcher.getName(uuid);
                setNPCSteve(npc, username);
                npc.setChat(new ArrayList <>());
            } catch (IOException e) {
                npc.init(data);
                npc.setCustomSteveTexture(data.getRandomTexture());
                npc.setName("Donator Rank: " + getRankingNumber());
                DonationLeaderboards.log.info("Failed to connect with mojang, set random NPC as skin");
            }
            catch (NullPointerException e) {
                npc.init(data);
                npc.setChat(new ArrayList <>());
                npc.setCustomSteveTexture(data.getRandomTexture());
                npc.setName("Donator Rank: " + getRankingNumber());
                DonationLeaderboards.log.info("Failed to verify Donator Spot Account with mojang, set random NPC as skin");
            }
        }
    }

    public int getNpcID() {
        return npcID;
    }

    public void setNpcID(int npcID) {
        this.npcID = npcID;
    }

    public int getHologramID() {
        return hologramID;
    }

    public void setHologramID(int hologramID) {
        this.hologramID = hologramID;
    }
}

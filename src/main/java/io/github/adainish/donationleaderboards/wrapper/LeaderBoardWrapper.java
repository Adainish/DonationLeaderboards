package io.github.adainish.donationleaderboards.wrapper;

import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.obj.Donator;
import io.github.adainish.donationleaderboards.obj.DonatorSpot;
import io.github.adainish.donationleaderboards.obj.Leaderboard;
import io.github.adainish.donationleaderboards.storage.DonatorStorage;
import io.github.adainish.donationleaderboards.storage.LeaderboardStorage;
import io.github.adainish.donationleaderboards.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.*;


public class LeaderBoardWrapper {
    private Leaderboard leaderboard;
    public HashMap <UUID, Donator> donators = new HashMap <>();

    public List<Donator> cachedDonorList = new ArrayList <>();
    public List<Donator> highestDonorList = new ArrayList <>();
    public LeaderBoardWrapper() {
        initLeaderBoard();
        initDonators();
    }

    public void saveLeaderBoard() {
        LeaderboardStorage.saveLeaderBoard(leaderboard);
        initLeaderBoard();
    }


    public void initLeaderBoard() {
        if (LeaderboardStorage.getLeaderboard() == null) {
            LeaderboardStorage.makeLeaderBoard();
        }
        reloadLeaderBoard();
    }

    public void reloadLeaderBoard() {
        leaderboard = LeaderboardStorage.getLeaderboard();
    }

    public void donatorCache(UUID uuid, boolean cache) {
        Donator donator;
        if (donators.containsKey(uuid))
            donator = donators.get(uuid);
        else donator = DonatorStorage.getDonator(uuid);
        if (donator == null) {
            DonatorStorage.makeDonatorData(uuid);
            donator = DonatorStorage.getDonator(uuid);
        }

        if (cache)
            donators.put(uuid, donator);
        else {
            donators.remove(uuid, donator);
            if (donator != null) {
                DonatorStorage.saveDonatorData(donator);
            }
        }
    }


    public void updateDonatorSpotData() {
        if (donators.isEmpty())
            return;
        cachedDonorList.clear();
        highestDonorList.clear();

        cachedDonorList.addAll(donators.values());
        cachedDonorList.sort(Comparator.comparing(Donator::getAmount));
        for (int i = 0; i < cachedDonorList.size(); i++) {
            if (i > getLeaderboard().getDonatorSpots().size())
                break;
            highestDonorList.add(cachedDonorList.get(i));
        }
        highestDonorList.sort(Comparator.comparing(Donator::getAmount).reversed());
        for (int i = 0; i < highestDonorList.size(); i++) {
            Donator d = highestDonorList.get(i);
            for (int j = 0; j < getLeaderboard().getDonatorSpots().size(); j++) {
                DonatorSpot spot = getLeaderboard().getDonatorSpots().get(i);
                spot.setUuid(d.getUuid());
                if (spot.getNpcID() <= 0) {
                    NPCChatting npc = spot.createNPCChatting(Util.getInstance().getWorld(spot.getWorldID()));
                    spot.spawnNPC(npc);
                } else {
                    World world = Util.getInstance().getWorld(spot.getWorldID());
                    Entity entity = world.getEntityByID(spot.getNpcID());
                    try {
                        if (entity == null) {
                            spot.spawnNPC();
                            throw new Exception("Entity did not exist or could not be found, Spawning new NPC");
                        } else spot.updateNPC();
                    } catch (Exception e) {
                        DonationLeaderboards.log.info(e.getMessage());
                    }
                };
            }
        }
        saveLeaderBoard();
    }

    public void saveAll() {
        if (donators.values().isEmpty())
            return;
        donators.values().forEach(DonatorStorage::saveDonatorData);
    }

    public void updateDonatorSpots() {

    }

    public void updateProgressBar() {

    }

    public void handOutRewards() {

    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }


    public void updateNPCs() {

    }

    public void updateHologram() {

    }

    public void initDonators() {
        DonatorStorage.donatorList().forEach(d -> donators.put(d.getUuid(), d));
    }

    public Donator getDonator(UUID uuid) {
        if (donators.containsKey(uuid))
            return donators.get(uuid);
        if (DonatorStorage.getDonator(uuid) == null) {
            DonatorStorage.makeDonatorData(uuid);
            return DonatorStorage.getDonator(uuid);
        }
        return donators.get(uuid);
    }

    public Collection <Donator> getDonators() {
        return donators.values();
    }

}

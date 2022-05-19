package io.github.adainish.donationleaderboards.obj;

import info.pixelmon.repack.ninja.leaping.configurate.commented.CommentedConfigurationNode;
import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.config.DonatorSpotConfig;
import io.github.adainish.donationleaderboards.storage.DonatorSpotStorage;
import io.github.adainish.donationleaderboards.storage.DonatorStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Leaderboard {

    private float requiredAmount;
    private float currentAmount;
    private long lastReset;
    private List<DonatorSpot> donatorSpots = new ArrayList <>();
    private List<Donator> donators = new ArrayList <>();


    public Leaderboard() {
        getDonatorSpots().addAll(DonatorSpotStorage.donatorSpotList());
        getDonatorSpots().sort(Comparator.comparing(DonatorSpot::getRankingNumber));
    }



    public void initDonatorSpots() {
        if (donatorSpots.isEmpty()) {
            CommentedConfigurationNode rootNode = DonatorSpotConfig.getConfig().get().getNode("DonatorSpot");
            Map nodeMap = rootNode.getChildrenMap();

            for (Object nodeObject : nodeMap.keySet()) {
                if (nodeObject != null) {
                    String node = nodeObject.toString();
                    if (node != null) {
                        donatorSpots.add(new DonatorSpot(node));
                    } else DonationLeaderboards.log.info("%node% returned null, check your config!");
                } else DonationLeaderboards.log.info("%node% returned null, check your config!");
            }
        }
        else for (DonatorSpot sp:donatorSpots) {
            sp.killNPC();
            sp.spawnNPC();
        }
    }

    public void initDonators() {
        getDonators().addAll(DonatorStorage.donatorList());
    }


    public float getRequiredAmount() {
        return requiredAmount;
    }

    public void setRequiredAmount(float requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    public float getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(float currentAmount) {
        this.currentAmount = currentAmount;
    }

    public long getLastReset() {
        return lastReset;
    }

    public void setLastReset(long lastReset) {
        this.lastReset = lastReset;
    }

    public List <DonatorSpot> getDonatorSpots() {
        return donatorSpots;
    }

    public void setDonatorSpots(List <DonatorSpot> donatorSpots) {
        this.donatorSpots = donatorSpots;
    }

    public List <Donator> getDonators() {
        return donators;
    }

    public void setDonators(List <Donator> donators) {
        this.donators = donators;
    }
}

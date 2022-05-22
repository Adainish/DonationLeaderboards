package io.github.adainish.donationleaderboards.obj;

import info.pixelmon.repack.ninja.leaping.configurate.commented.CommentedConfigurationNode;
import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.config.DonatorSpotConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Leaderboard {

    private float requiredAmount;
    private float currentAmount;
    private long lastReset;
    private List<DonatorSpot> donatorSpots = new ArrayList <>();

    public List<Hologram> hologramList = new ArrayList <>();

    public Leaderboard() {}


    public void loadFromConfig() {
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


    public void initDonatorSpots() {
        try {
            if (!donatorSpots.isEmpty()) {
                getDonatorSpots().sort(Comparator.comparing(DonatorSpot::getRankingNumber));
                donatorSpots.forEach(DonatorSpot::updateNPC);
            } else {
                loadFromConfig();
            }

            DonationLeaderboards.wrapper.saveLeaderBoard();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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

}

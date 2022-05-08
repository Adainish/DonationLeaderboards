package io.github.adainish.donationleaderboards.obj;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DonatorSpot {
    private UUID uuid;
    private int worldID;
    private double x;
    private double y;
    private double z;
    private int rankingNumber;
    private List <String> displayText = new ArrayList <>();
    private String hologramDisplay = "";

    public DonatorSpot(String identifier) {

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
}

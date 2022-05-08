package io.github.adainish.donationleaderboards.obj;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard {

    private float requiredAmount;
    private float currentAmount;
    private long lastReset;
    private List<DonatorSpot> donatorSpots = new ArrayList <>();
    private List<Donator> donators = new ArrayList <>();


    public Leaderboard() {

    }

}

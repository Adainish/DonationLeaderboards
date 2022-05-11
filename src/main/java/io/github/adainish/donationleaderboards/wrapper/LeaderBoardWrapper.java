package io.github.adainish.donationleaderboards.wrapper;

import io.github.adainish.donationleaderboards.obj.Donator;
import io.github.adainish.donationleaderboards.obj.DonatorSpot;
import io.github.adainish.donationleaderboards.obj.Leaderboard;
import io.github.adainish.donationleaderboards.storage.DonatorStorage;
import io.github.adainish.donationleaderboards.storage.LeaderboardStorage;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardWrapper {
    private Leaderboard leaderboard;
    private List<Donator> donatorList = new ArrayList<>();
    private List<DonatorSpot> donatorSpots = new ArrayList <>();

    public LeaderBoardWrapper() {
        if (LeaderboardStorage.getLeaderboard() == null) {
            LeaderboardStorage.makeLeaderBoard();
        } else setLeaderboard(LeaderboardStorage.getLeaderboard());

        setDonatorList(DonatorStorage.donatorList());

    }


    public void initLeaderBoard() {

    }

    public void reloadLeaderBoard() {

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

    public List <Donator> getDonatorList() {
        return donatorList;
    }

    public void setDonatorList(List <Donator> donatorList) {
        this.donatorList = donatorList;
    }

    public List <DonatorSpot> getDonatorSpots() {
        return donatorSpots;
    }

    public void setDonatorSpots(List <DonatorSpot> donatorSpots) {
        this.donatorSpots = donatorSpots;
    }
}

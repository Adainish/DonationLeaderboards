package io.github.adainish.donationleaderboards.wrapper;

import io.github.adainish.donationleaderboards.obj.Leaderboard;
import io.github.adainish.donationleaderboards.storage.LeaderboardStorage;


public class LeaderBoardWrapper {
    private Leaderboard leaderboard;

    public LeaderBoardWrapper() {
        if (LeaderboardStorage.getLeaderboard() == null) {
            LeaderboardStorage.makeLeaderBoard();
        }
        setLeaderboard(LeaderboardStorage.getLeaderboard());
        leaderboard.initDonators();
        leaderboard.initDonatorSpots();
    }

    public void saveLeaderBoard() {
        LeaderboardStorage.saveLeaderBoard(leaderboard);
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


    public void updateNPCs() {

    }

    public void updateHologram() {

    }
}

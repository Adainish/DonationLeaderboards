package io.github.adainish.donationleaderboards.tasks;

import io.github.adainish.donationleaderboards.DonationLeaderboards;

public class UpdateDonatorSpotsTask implements Runnable{
    @Override
    public void run() {
        DonationLeaderboards.wrapper.updateDonatorSpotData();
    }
}

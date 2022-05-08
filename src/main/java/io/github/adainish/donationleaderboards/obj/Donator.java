package io.github.adainish.donationleaderboards.obj;

import java.util.UUID;

public class Donator {
    private UUID uuid;
    private float amount;


    public Donator(UUID uuid) {

    }

    public void updateCache() {

    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}

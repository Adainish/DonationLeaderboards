package io.github.adainish.donationleaderboards.obj;

import java.util.UUID;

public class Donator {
    private UUID uuid;
    private float amount;


    public Donator(UUID uuid) {
        setUuid(uuid);
    }

    public void updateCache() {

    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void increaseAmount(float amount) {
        this.amount += amount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}

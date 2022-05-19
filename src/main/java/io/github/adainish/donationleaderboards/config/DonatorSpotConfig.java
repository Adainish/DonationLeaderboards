package io.github.adainish.donationleaderboards.config;

public class DonatorSpotConfig extends Configurable{
    private static DonatorSpotConfig config;

    public static DonatorSpotConfig getConfig() {
        if (config == null) {
            config = new DonatorSpotConfig();
        }
        return config;
    }

    public void setup() {
        super.setup();
    }

    public void load() {
        super.load();
    }
    @Override
    public void populate() {

        for (int i = 0; i < 3; i++) {
            this.get().getNode("DonatorSpot", String.valueOf(i + 1), "WorldID").setValue(0).setComment("The World ID");
            this.get().getNode("DonatorSpot", String.valueOf(i + 1), "X").setValue(String.valueOf(i + 1)).setComment("The X coordinate");
            this.get().getNode("DonatorSpot", String.valueOf(i + 1), "Y").setValue(80).setComment("The Y coordinate");
            this.get().getNode("DonatorSpot", String.valueOf(i + 1), "Z").setValue(20).setComment("The Z Coordinate");
            this.get().getNode("DonatorSpot", String.valueOf(i + 1), "Ranking").setValue(i + 1).setComment("The Ranking Number");
            this.get().getNode("DonatorSpot", String.valueOf(i + 1), "Display").setValue("&c# " + (i + 1)).setComment("The Hologram displayed above the spot");
        }
    }

    @Override
    public String getConfigName() {
        return "DonatorSpot.conf";
    }
}

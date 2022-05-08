package io.github.adainish.donationleaderboards.config;

import info.pixelmon.repack.ninja.leaping.configurate.commented.CommentedConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import info.pixelmon.repack.ninja.leaping.configurate.loader.ConfigurationLoader;
import io.github.adainish.donationleaderboards.DonationLeaderboards;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Configurable {
    protected CommentedConfigurationNode configNode;
    private Path configFile = Paths.get(DonationLeaderboards.getConfigDir() + "//DonationLeaderboards//" + this.getConfigName());
    private ConfigurationLoader configLoader;

    public Configurable() {
        this.configLoader = HoconConfigurationLoader.builder().setPath(this.configFile).build();
    }

    public abstract void populate();

    public abstract String getConfigName();

    public ConfigurationLoader getConfigLoader() {
        return this.configLoader;
    }

    public void setup() {
        File configDirectory = new File(DonationLeaderboards.getConfigDir() + "//DonationLeaderboards//");
        if (!configDirectory.exists()) {
            configDirectory.mkdirs();
        }

        if (!Files.exists(this.configFile)) {
            try {
                Files.createFile(this.configFile);
                this.load();
                this.populate();
                this.save();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        } else {
            this.load();
        }

    }

    public void load() {
        try {
            this.configNode = (CommentedConfigurationNode) this.configLoader.load();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public void setConfigNode(CommentedConfigurationNode configNode) {
        this.configNode = configNode;
    }

    public void save() {
        try {
            this.configLoader.save(this.configNode);
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public CommentedConfigurationNode get() {
        return this.configNode;
    }
}

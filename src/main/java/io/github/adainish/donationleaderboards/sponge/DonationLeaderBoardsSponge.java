package io.github.adainish.donationleaderboards.sponge;

import com.google.inject.Inject;
import io.github.adainish.donationleaderboards.DonationLeaderboards;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(

        id = "donationleaderboardssponge",
        name = DonationLeaderboards.MOD_NAME,
        version = DonationLeaderboards.VERSION,
        description = "Donation Leaderboards Plugin developed by Winglet, offering automated Donation Leaderboards to track progress and encourage donations",
        authors = DonationLeaderboards.AUTHORS
)
public class DonationLeaderBoardsSponge {
    private static DonationLeaderBoardsSponge instance;
    @Inject
    private Logger logger;
    @Inject private Game game;
    private PluginContainer plugin;

    public static void setInstance(DonationLeaderBoardsSponge instance) {
        DonationLeaderBoardsSponge.instance = instance;
    }

    @Listener
    public void onAboutToStart(GameAboutToStartServerEvent event) {
        DonationLeaderboards.log.info("Initialising Sponge and Listeners for Donations to support DonationLeaderboards");
        setInstance(this);
        game = instance.game;

        game.getEventManager().registerListeners(this, new DonationListener());
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        setPlugin(Sponge.getPluginManager().getPlugin("donationleaderboards").get());

        DonationLeaderboards.log.info("Sponge system and donation leaderboards initialised");
    }

    public PluginContainer getPlugin() {
        return plugin;
    }

    public void setPlugin(PluginContainer plugin) {
        this.plugin = plugin;
    }

}

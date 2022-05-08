package io.github.adainish.donationleaderboards;

import io.github.adainish.donationleaderboards.obj.Donator;
import io.github.adainish.donationleaderboards.wrapper.LeaderBoardWrapper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

@Mod(
        modid = DonationLeaderboards.MOD_ID,
        name = DonationLeaderboards.MOD_NAME,
        version = DonationLeaderboards.VERSION,
        acceptableRemoteVersions = "*"
)
public class DonationLeaderboards {

    public static final String MOD_ID = "donationleaderboards";
    public static final String MOD_NAME = "DonationLeaderboards";
    public static final String VERSION = "1.0-SNAPSHOT";
    public static final String AUTHORS = "Winglet";
    public static final String YEAR = "2022";
    public static Logger log = LogManager.getLogger(MOD_NAME);

    public static LeaderBoardWrapper wrapper;

    private static HashMap <UUID, Donator> cachedDonators = new HashMap <>();

    private static File configDir;
    private static File dataDir;

    private static File leaderboardDir;

    @Mod.Instance(MOD_ID)
    public static DonationLeaderboards INSTANCE;


    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        log.info("Booting up %n by %authors %v %y"
                .replace("%n", MOD_NAME)
                .replace("%authors", AUTHORS)
                .replace("%v", VERSION)
                .replace("%y", YEAR)
        );
        setConfigDir(new File(event.getModConfigurationDirectory() + "/"));
        getConfigDir().mkdirs();

        setDataDir(new File(getConfigDir() + "/DonationLeaderboards/data"));
        getDataDir().mkdirs();

        setLeaderboardDir(new File(getDataDir() + "/leaderboard"));
        getLeaderboardDir().mkdirs();


    }


    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        wrapper = new LeaderBoardWrapper();
    }

    public static File getConfigDir() {
        return configDir;
    }

    public static void setConfigDir(File configDir) {
        DonationLeaderboards.configDir = configDir;
    }

    public static File getDataDir() {
        return dataDir;
    }

    public static void setDataDir(File dataDir) {
        DonationLeaderboards.dataDir = dataDir;
    }

    public static File getLeaderboardDir() {
        return leaderboardDir;
    }

    public static void setLeaderboardDir(File leaderboardDir) {
        DonationLeaderboards.leaderboardDir = leaderboardDir;
    }


    public static HashMap <UUID, Donator> getCachedDonators() {
        return cachedDonators;
    }

    public static void setCachedDonators(HashMap <UUID, Donator> cachedDonators) {
        DonationLeaderboards.cachedDonators = cachedDonators;
    }

}

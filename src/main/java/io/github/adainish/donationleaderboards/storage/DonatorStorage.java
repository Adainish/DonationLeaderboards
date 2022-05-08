package io.github.adainish.donationleaderboards.storage;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.obj.Donator;
import io.github.adainish.donationleaderboards.util.Adapters;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.*;
import java.util.UUID;

public class DonatorStorage {
    public static void makeDonatorData(EntityPlayerMP player) {
        File dir = DonationLeaderboards.getDataDir();
        dir.mkdirs();


        Donator donator = new Donator(player.getUniqueID());

        File file = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(player.getUniqueID())));
        if (file.exists()) {
            DonationLeaderboards.log.error("There was an issue generating the Donator Data, Donator already exists? Ending function");
            return;
        }

        Gson gson = Adapters.PRETTY_MAIN_GSON;
        String json = gson.toJson(donator);

        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDonatorData(Donator donator) {

        File dir = DonationLeaderboards.getDataDir();
        dir.mkdirs();

        File file = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(donator.getUuid())));
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (reader == null) {
            DonationLeaderboards.log.error("Something went wrong attempting to read the Donator Data");
            return;
        }

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(donator));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        donator.updateCache();
    }

    public static Donator getDonator(UUID uuid) {
        File dir = DonationLeaderboards.getDataDir();
        dir.mkdirs();

        if (DonationLeaderboards.getCachedDonators().containsKey(uuid))
            return DonationLeaderboards.getCachedDonators().get(uuid);

        File guildFile = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(uuid)));
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(guildFile));
        } catch (FileNotFoundException e) {
            DonationLeaderboards.log.error("Something went wrong attempting to read the Donator Data, new Entry Perhaps?");
            return null;
        }

        return gson.fromJson(reader, Donator.class);
    }
}

package io.github.adainish.donationleaderboards.storage;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.obj.DonatorSpot;
import io.github.adainish.donationleaderboards.util.Adapters;

import java.io.*;
import java.util.UUID;

public class DonatorSpotStorage {
    public static void makeDonatorSpotData(String identifier, int rankingNumber) {
        File dir = DonationLeaderboards.getDataDir();
        dir.mkdirs();


        DonatorSpot donatorSpot = new DonatorSpot(identifier, rankingNumber);

        File file = new File(dir, "%uuid%.json".replaceAll("%uuid%", identifier));
        if (file.exists()) {
            DonationLeaderboards.log.error("There was an issue generating the DonatorSpot Data, Spot already exists? Ending function");
            return;
        }

        Gson gson = Adapters.PRETTY_MAIN_GSON;
        String json = gson.toJson(donatorSpot);

        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDonatorSpot(DonatorSpot donator) {

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

       // execute update code
    }

    public static DonatorSpot getDonatorSpot(UUID uuid) {
        File dir = DonationLeaderboards.getDataDir();
        dir.mkdirs();


        File guildFile = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(uuid)));
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(guildFile));
        } catch (FileNotFoundException e) {
            DonationLeaderboards.log.error("Something went wrong attempting to read the Data, new Entry Perhaps?");
            return null;
        }

        return gson.fromJson(reader, DonatorSpot.class);
    }
}

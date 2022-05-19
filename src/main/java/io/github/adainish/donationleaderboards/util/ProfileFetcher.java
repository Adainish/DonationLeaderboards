package io.github.adainish.donationleaderboards.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class ProfileFetcher {
    public static final String uuidURL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    public static final String nameURL = "https://api.mojang.com/users/profiles/minecraft/";

    public static JsonObject getPlayerJson(UUID uuid, String name) throws IOException {
        URL url;

        if (name != null) {
            url = new URL(nameURL + name);
        } else {
            url = new URL(uuidURL + uuid);
        }
        URLConnection urlConnection = url.openConnection();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonReader jsonReader = null;
        jsonReader = new JsonReader(bufferedReader);

        return gson.fromJson(jsonReader, JsonObject.class);
    }

    public static UUID formatFromInput(String uuid) throws IllegalArgumentException{
        if(uuid == null) throw new IllegalArgumentException();
        uuid = uuid.trim();
        return uuid.length() == 32 ? fromTrimmed(uuid.replaceAll("-", "")) : UUID.fromString(uuid);
    }

    public static UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException{
        if(trimmedUUID == null) throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        /* Backwards adding to avoid index adjustments */
        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
        } catch (StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException();
        }

        return UUID.fromString(builder.toString());
    }

    public static UUID getUUID(String name) throws IOException {
        String uuidTemp = getPlayerJson(null, name).get("id").getAsString();
        if(getPlayerJson(null, name) != null){
            return formatFromInput(uuidTemp);
        }
        return null;
    }

    public static String getName(UUID uuid) throws IOException {
        if(getPlayerJson(uuid, null) != null){
            return getPlayerJson(uuid, null).get("name").toString();
        }
        return null;
    }
}

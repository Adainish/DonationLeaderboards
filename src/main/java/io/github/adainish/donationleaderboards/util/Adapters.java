package io.github.adainish.donationleaderboards.util;

import com.google.gson.*;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.status.NoStatus;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ComingSoon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.*;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.enums.EnumPokerusType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;

import java.lang.reflect.Type;

public class Adapters {
    public static Gson PRETTY_MAIN_GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Pokemon.class, new PokemonAdapter(true))
            .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
            .create();

    public static Gson UGLY_MAIN_GSON = new GsonBuilder()
            .registerTypeAdapter(Pokemon.class, new PokemonAdapter(false))
            .create();

    private static Gson PRETTY_SUB_GSON = applyAdapters(new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping())
            .create();

    private static Gson UGLY_SUB_GSON = applyAdapters(new GsonBuilder()
            .disableHtmlEscaping())
            .create();

    private static Pokemon targetting = null;

    public static GsonBuilder applyAdapters(GsonBuilder builder)
    {
        ExclusionStrategy exclusionStrategy = new PokemonExclusionStrategy();
        return builder
                .registerTypeAdapter(Pokemon.class, new PokemonInstanceCreator())
                .registerTypeAdapter(NBTTagCompound.class, new NBTAdapter())
                .registerTypeAdapter(Moveset.class, new MovesetAdapter())
                .registerTypeAdapter(Level.class, new LevelAdapter())
                .registerTypeAdapter(IVStore.class, new IVStoreAdapter())
                .registerTypeAdapter(EVStore.class, new EVStoreAdapter())
                .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
                .registerTypeHierarchyAdapter(StatusPersist.class, new StatusAdapter())
                .registerTypeHierarchyAdapter(ExtraStats.class, new ExtraStatsAdapter())
                .registerTypeHierarchyAdapter(AbilityBase.class, new AbilityAdapter())
                .registerTypeAdapter(Pokerus.class, new PokerusAdapter())
                .registerTypeAdapter(ResourceLocation.class, new ResourceLocationAdapter())
                .registerTypeAdapter(Tuple.class, new TupleAdapter())
                .addSerializationExclusionStrategy(exclusionStrategy)
                .addDeserializationExclusionStrategy(exclusionStrategy);
    }

    public static class PokemonAdapter implements JsonDeserializer <Pokemon>, JsonSerializer <Pokemon>
    {
        public boolean pretty = false;

        public PokemonAdapter(boolean pretty)
        {
            this.pretty = pretty;
        }

        @Override
        public Pokemon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            targetting = Pixelmon.pokemonFactory.create(EnumSpecies.getFromName(json.getAsJsonObject().get("species").getAsString()).get());
            return (pretty ? PRETTY_SUB_GSON : UGLY_SUB_GSON).fromJson(json, Pokemon.class);
        }

        @Override
        public JsonElement serialize(Pokemon src, Type typeOfSrc, JsonSerializationContext context)
        {
            return (pretty ? PRETTY_SUB_GSON : UGLY_SUB_GSON).toJsonTree(src, Pokemon.class);
        }
    }

    public static class PokemonInstanceCreator implements InstanceCreator<Pokemon>
    {
        @Override
        public Pokemon createInstance(Type type)
        {
            return targetting;
        }
    }

    public static class PokemonExclusionStrategy implements ExclusionStrategy
    {

        @Override
        public boolean shouldSkipField(FieldAttributes f)
        {
            return f.getName().equalsIgnoreCase("dataSyncMap");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz)
        {
            return false;
        }
    }

    public static class MovesetAdapter implements JsonSerializer<Moveset>, JsonDeserializer<Moveset>
    {
        @Override
        public Moveset deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            Moveset moveset = new Moveset();
            JsonArray arr = json.getAsJsonArray();
            for (JsonElement jsonElement : arr)
            {
                JsonObject moveObj = jsonElement.getAsJsonObject();
                Attack attack = new Attack(moveObj.get("name").getAsString());
                attack.pp = moveObj.get("pp").getAsInt();
                attack.ppLevel = moveObj.get("ppLevel").getAsInt();
                moveset.add(attack);
            }

            return moveset;
        }

        @Override
        public JsonElement serialize(Moveset src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonArray json = new JsonArray();
            for (Attack attack : src)
            {
                JsonObject move = new JsonObject();
                move.addProperty("name", attack.getMove().getAttackName());
                move.addProperty("pp", attack.pp);
                move.addProperty("ppLevel", attack.ppLevel);
                json.add(move);
            }

            return json;
        }
    }

    public static class NBTAdapter implements JsonSerializer<NBTTagCompound>, JsonDeserializer<NBTTagCompound>
    {
        @Override
        public NBTTagCompound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            try
            {
                return JsonToNBT.getTagFromJson(json.getAsString());
            }
            catch (NBTException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public JsonElement serialize(NBTTagCompound src, Type typeOfSrc, JsonSerializationContext context)
        {
            return context.serialize(src.toString(), String.class);
        }
    }

    public static class LevelAdapter implements JsonSerializer<Level>, JsonDeserializer<Level>
    {
        @Override
        public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            Level level = new Level(new DelegateLink(targetting));
            level.expToNextLevel = json.getAsInt();
            return level;
        }

        @Override
        public JsonElement serialize(Level src, Type typeOfSrc, JsonSerializationContext context)
        {
            return context.serialize(src.expToNextLevel, Integer.class);
        }
    }

    public static class EVStoreAdapter implements JsonSerializer<EVStore>, JsonDeserializer<EVStore>
    {
        @Override
        public EVStore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            JsonArray arr = json.getAsJsonArray();

            EVStore store = new EVStore();
            int[] evs = new int[6];
            for (int i = 0 ; i < 6 ; i++)
                evs[i] = arr.get(i).getAsInt();
            store.fillFromArray(evs);

            store.withPokemon(targetting);

            return store;
        }

        @Override
        public JsonElement serialize(EVStore src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonArray arr = new JsonArray();
            for (int i = 0 ; i < 6 ; i++)
                arr.add(src.getArray()[i]);
            return arr;
        }
    }

    public static class IVStoreAdapter implements JsonSerializer<IVStore>, JsonDeserializer<IVStore>
    {
        @Override
        public IVStore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            JsonArray arr = json.getAsJsonArray();

            IVStore store = new IVStore();
            for (int i = 0 ; i < 6 ; i++)
            {
                store.set(StatsType.getStatValues()[i], arr.get(i).getAsJsonObject().get("amount").getAsInt());
                store.setHyperTrained(StatsType.getStatValues()[i], arr.get(i).getAsJsonObject().get("hypertrained").getAsBoolean());
            }

            store.withPokemon(targetting);

            return store;
        }

        @Override
        public JsonElement serialize(IVStore src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonArray arr = new JsonArray();
            for (int i = 0 ; i < 6 ; i++)
            {
                JsonObject obj = new JsonObject();
                obj.addProperty("amount", src.getArray()[i]);
                obj.addProperty("hypertrained", src.isHyperTrained(StatsType.getStatValues()[i]));
                arr.add(obj);
            }

            return arr;
        }
    }

    public static class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack>
    {
        @Override
        public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            try
            {
                String nbtString = json.getAsString();
                if (nbtString == null || nbtString.isEmpty()) {
                    return null;
                }

                ItemStack item = new ItemStack(JsonToNBT.getTagFromJson(nbtString));
                return item.isEmpty() ? ItemStack.EMPTY : item;
            }
            catch (NBTException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context)
        {
            if (src.isEmpty())
                return context.serialize("", String.class);
            else
                return context.serialize(src.writeToNBT(new NBTTagCompound()).toString(), String.class);
        }
    }

    public static class StatusAdapter implements JsonSerializer<StatusPersist>, JsonDeserializer<StatusPersist>
    {
        @Override
        public StatusPersist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            StatusPersist status = StatusType.getEffectInstance(StatusType.getStatusEffect(json.getAsString()).ordinal());
            return status == null ? NoStatus.noStatus : status;
        }

        @Override
        public JsonElement serialize(StatusPersist src, Type typeOfSrc, JsonSerializationContext context)
        {
            return context.serialize(src.type.toString(), String.class);
        }
    }

    public static class ExtraStatsAdapter implements JsonSerializer<ExtraStats>, JsonDeserializer<ExtraStats>
    {
        @Override
        public ExtraStats deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            if (targetting.getExtraStats() != null)
            {
                try
                {
                    targetting.getExtraStats().readFromNBT(JsonToNBT.getTagFromJson(json.getAsString()));
                    return targetting.getExtraStats();
                }
                catch (NBTException e)
                {
                    e.printStackTrace();
                    return null;
                }
            }

            return null;
        }

        @Override
        public JsonElement serialize(ExtraStats src, Type typeOfSrc, JsonSerializationContext context)
        {
            NBTTagCompound nbt = new NBTTagCompound();
            src.writeToNBT(nbt);
            return context.serialize(nbt.toString(), String.class);
        }
    }

    public static class AbilityAdapter implements JsonSerializer<AbilityBase>, JsonDeserializer<AbilityBase>
    {
        @Override
        public AbilityBase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            return AbilityBase.getAbility(json.getAsString()).orElse(new ComingSoon(targetting.getBaseStats().abilities[targetting.getAbilitySlot()]));
        }

        @Override
        public JsonElement serialize(AbilityBase src, Type typeOfSrc, JsonSerializationContext context)
        {
            return context.serialize(src.getName());
        }
    }

    public static class PokerusAdapter implements JsonSerializer<Pokerus>, JsonDeserializer<Pokerus>
    {
        @Override
        public Pokerus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            EnumPokerusType type = EnumPokerusType.valueOf(json.getAsJsonObject().get("type").getAsString());
            int secondsSinceInfection = json.getAsJsonObject().get("secondsSinceInfection").getAsInt();
            boolean announced = json.getAsJsonObject().get("announced").getAsBoolean();

            Pokerus rus = new Pokerus(type);
            rus.secondsSinceInfection = secondsSinceInfection;
            rus.announced = announced;
            return rus;
        }

        @Override
        public JsonElement serialize(Pokerus src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", src.type.toString());
            obj.addProperty("secondsSinceInfection", src.secondsSinceInfection);
            obj.addProperty("announced", src.announced);
            return obj;
        }
    }

    public static class ResourceLocationAdapter implements JsonSerializer<ResourceLocation>, JsonDeserializer<ResourceLocation>
    {
        @Override
        public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            return new ResourceLocation(json.getAsString());
        }

        @Override
        public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context)
        {
            return context.serialize(src.toString(), String.class);
        }
    }

    public static class TupleAdapter implements JsonSerializer<Tuple>, JsonDeserializer<Tuple>
    {
        @Override
        public Tuple deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            return new Tuple(json.getAsJsonObject().get("a").getAsLong(), json.getAsJsonObject().get("b").getAsLong());
        }

        @Override
        public JsonElement serialize(Tuple src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject obj = new JsonObject();
            obj.addProperty("a", (Long)src.getFirst());
            obj.addProperty("b", (Long)src.getSecond());
            return obj;
        }
    }
}

package io.github.adainish.donationleaderboards.command;

import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.obj.Donator;
import io.github.adainish.donationleaderboards.storage.DonatorStorage;
import io.github.adainish.donationleaderboards.util.PermissionUtil;
import io.github.adainish.donationleaderboards.util.ProfileFetcher;
import io.github.adainish.donationleaderboards.util.Util;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Command extends CommandBase {
    @Override
    public String getName() {
        return "donationleaderboards";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "&b/donationleaderboards";
    }

    public void sendNoPermMessage(ICommandSender sender) {
        Util.send(sender, "&c(&4!&c) &eYou're not allowed to do this");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (!PermissionUtil.canUse("donationleaderboards.command.messages.base", sender)) {
            sendNoPermMessage(sender);
            return;
        }

        if (args.length >= 4) {
            Util.send(sender, getUsage(sender));
            return;
        }


        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!PermissionUtil.canUse("donationleaderboards.command.messages.reload", sender)) {
                        sendNoPermMessage(sender);
                        break;
                    }
                    DonationLeaderboards.INSTANCE.load();
                    Util.send(sender, "&cReloaded Donation Leaderboards");
                    break;
                }
            case 3: {
                if (args[0].equalsIgnoreCase("add")) {
                    if (!PermissionUtil.canUse("donationleaderboards.command.messages.reload", sender)) {
                        sendNoPermMessage(sender);
                        break;
                    }
                    UUID uuid;
                    String target = args[1];
                    float amount = Float.parseFloat(args[2]);
                    if (target.isEmpty()) {
                        Util.send(sender, "&cPlease provide a username");
                        break;
                    }
                    try {
                        uuid = ProfileFetcher.getUUID(target);
                        Util.send(sender, "&aAccount was verified with mojang!");
                    } catch (IOException e) {
                        DonationLeaderboards.log.info("Failed to connect with mojang");
                        Util.send(sender, "&eFailed to connect with mojang");
                        break;
                    }
                    catch (NullPointerException e) {
                        DonationLeaderboards.log.info("Failed to verify provided account with mojang");
                        Util.send(sender, "&4Failed to verify provided account with mojang");
                        break;
                    }

                    Donator donator = DonationLeaderboards.wrapper.getDonator(uuid);
                    donator.increaseAmount(amount);
                    DonationLeaderboards.wrapper.donators.replace(uuid, donator);
                    DonatorStorage.saveDonatorData(donator);
                    DonationLeaderboards.INSTANCE.load();
                    Util.send(sender, "&cUpdated donator with specified amount");
                    break;
                }
            }
            default:
                Util.send(sender, getUsage(sender));
                break;
        }




    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List <String> getAliases() {
        return Arrays.asList("dl", "donorleaderboard");
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}

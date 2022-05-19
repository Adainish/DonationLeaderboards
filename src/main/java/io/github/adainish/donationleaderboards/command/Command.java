package io.github.adainish.donationleaderboards.command;

import io.github.adainish.donationleaderboards.DonationLeaderboards;
import io.github.adainish.donationleaderboards.util.PermissionUtil;
import io.github.adainish.donationleaderboards.util.Util;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.Arrays;
import java.util.List;
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

        if (args.length >= 2) {
            Util.send(sender, getUsage(sender));
            return;
        }


        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!PermissionUtil.canUse("donationleaderboards.command.messages.reload", sender)) {
                        sendNoPermMessage(sender);
                        return;
                    }
                    DonationLeaderboards.INSTANCE.load();
                    Util.send(sender, "&cReloaded Donation Leaderboards");
                    return;
                }
            default:
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

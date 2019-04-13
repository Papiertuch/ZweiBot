package de.einjava.zweibot.command;

import de.einjava.zweibot.ZweiBot;
import de.einjava.zweibot.utils.VerifyHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.managers.GuildController;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.awt.*;

/**
 * Created by Leon on 11.08.2018.
 * development with love.
 * © Copyright by Papiertuch
 */

public class Verify extends Command {

    public Verify() {
        super(ZweiBot.getInstance().getFileHandler().getString("Module.verify.command"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            VerifyHandler verifyManager = new VerifyHandler(player);
            verifyManager.createPlayerIngame();
            if (args.length == 0) {
                player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.verify"));
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("update")) {
                    if (verifyManager.getTypebyUUID() == 3) {
                        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.synchronize"));
                        ZweiBot.getInstance().getMethods().setClientGroups(player, ZweiBot.getInstance().getJda().getUserById(verifyManager.getIDbyUUID()));
                    } else {
                        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.not_verify"));
                    }
                }

                if (args[0].equalsIgnoreCase("info")) {
                    if (verifyManager.getTypebyUUID() == 3) {
                        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Prefix") + "§7Spieler §8» §e" + player.getName());
                        player.sendMessage("§8» §eUUID §8» §7" + player.getUniqueId().toString());
                        player.sendMessage("§8» §eRank §8» §7" + verifyManager.getRankbyUUID());
                        player.sendMessage("§8» §eID §8» §7" + verifyManager.getIDbyUUID());
                    } else {
                        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.notverify"));
                    }
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (verifyManager.getTypebyUUID() == 3) {
                        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.delete"));
                        GuildController guildController = new GuildController(ZweiBot.getInstance().getJda().getGuilds().get(0));
                        Member member = guildController.getGuild().getMemberById(verifyManager.getIDbyUUID());
                        for (Role role : member.getRoles()) {
                            guildController.removeRolesFromMember(member, role).complete();
                        }
                        guildController.addRolesToMember(member, guildController.getGuild().getRolesByName(ZweiBot.getInstance().getFileHandler().getString("Module.verify.default_rank"), true)).complete();
                        verifyManager.delete();
                    } else {
                        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.notverify"));
                    }
                }
                if (args[0].equalsIgnoreCase("accept")) {
                    if (ZweiBot.name.containsKey(player)) {
                        ZweiBot.getInstance().sendPrivateMessage(ZweiBot.member.get(player), "Info:", "Du bist nun mit **" + player.getName() + "** verifiziert", Color.GREEN);
                        verifyManager.setTypebyUUID(3);
                        verifyManager.setIDbyUUID(ZweiBot.member.get(player).getId());
                        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.synchronize"));
                        ZweiBot.getInstance().getMethods().setClientGroups(player, ZweiBot.member.get(player));
                        ZweiBot.name.remove(player);
                        ZweiBot.member.remove(player);
                    } else {
                        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.norequest"));
                    }
                }
                if (args[0].equalsIgnoreCase("deny")) {
                    if (ZweiBot.name.containsKey(player)) {
                        verifyManager.setTypebyUUID(0);
                        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.verify_deny").replace("%client%", ZweiBot.member.get(player).getName()));
                        ZweiBot.getInstance().sendPrivateMessage(ZweiBot.member.get(player), "Info:", "**" + player.getName() + "** hat deine Anfrage abgehlehnt!", Color.RED);
                        ZweiBot.name.remove(player);
                        ZweiBot.member.remove(player);
                    } else {
                        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.norequest"));
                    }
                }
                if (args[0].equalsIgnoreCase("help")) {
                    sendHelp(player);
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("update")) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                    VerifyHandler verify = new VerifyHandler(target);
                    verify.createPlayerIngame();
                    if (verify.getTypebyUUID() == 3) {
                        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.synchronize"));
                        ZweiBot.getInstance().getMethods().setClientGroups(player, ZweiBot.getInstance().getJda().getUserById(verify.getIDbyUUID()));
                    }
                } else {
                    player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.not_verify"));
                }
            } else {
                sendHelp(player);
            }
        } catch (Exception ignored) {
        }

    }

    private void sendHelp(ProxiedPlayer player) {
        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.help_update"));
        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.help_info"));
        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.help_delete"));
        player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.help_help"));

    }
}

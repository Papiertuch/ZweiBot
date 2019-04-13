package de.einjava.zweibot.utils;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import de.einjava.zweibot.BungeeBootstrap;
import de.einjava.zweibot.ZweiBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.managers.GuildController;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;

/**
 * Created by Leon on 11.08.2018.
 * development with love.
 * © Copyright by Papiertuch
 */

public class Methods {

    public void setClientGroups(ProxiedPlayer player, User member) {
        new Thread(() -> {
            ProxyServer.getInstance().getScheduler().runAsync(BungeeBootstrap.getInstance(), new Runnable() {
                @Override
                public void run() {
                    String groupName;
                    String rank = "";
                    if (ZweiBot.getInstance().getFileHandler().getBoolean("Module.cloudnet.enable")) {
                        PermissionGroup permissionGroup = CloudAPI.getInstance().getOnlinePlayer(player.getUniqueId()).getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool());
                        rank = permissionGroup.getName();
                    } else {
                        rank = getRank(player);
                    }
                    new VerifyHandler(player).setRankbyUUID(rank);
                    for (Role role : ZweiBot.getInstance().getJda().getRoles()) {
                        groupName = role.getName();
                        if (groupName.equalsIgnoreCase(rank)) {
                            GuildController guildController = ZweiBot.getInstance().getJda().getGuilds().get(0).getController();
                            guildController.addRolesToMember(ZweiBot.getInstance().getJda().getGuilds().get(0).getMember(member), role).complete();
                            String group = ZweiBot.getInstance().getFileHandler().getString("Module.verify.verify_rank");
                            guildController.addRolesToMember(ZweiBot.getInstance().getJda().getGuilds().get(0).getMember(member), ZweiBot.getInstance().getJda().getRolesByName(group, true)).complete();
                            if (player.hasPermission(ZweiBot.getInstance().getFileHandler().getString("Module.support.permission"))) {
                                guildController.addRolesToMember(ZweiBot.getInstance().getJda().getGuilds().get(0).getMember(member), ZweiBot.getInstance().getJda().getRolesByName(ZweiBot.getInstance().getFileHandler().getString("Module.support.role"), true)).complete();
                            }
                        }
                        if (!groupName.equalsIgnoreCase(rank) && !groupName.equalsIgnoreCase(ZweiBot.getInstance().getFileHandler().getString("Module.verify.verify_rank"))) {
                            for (int i = 0; i < ZweiBot.getInstance().getFileHandler().ranklist.size(); i++) {
                                if (ZweiBot.getInstance().getFileHandler().ranklist.get(i).split("; ")[0].equalsIgnoreCase(groupName)) {
                                    GuildController guildController = new GuildController(ZweiBot.getInstance().getJda().getGuilds().get(0));
                                    guildController.removeRolesFromMember(ZweiBot.getInstance().getJda().getGuilds().get(0).getMember(member), role).complete();
                                }
                            }
                        }
                    }
                    ZweiBot.getInstance().sendPrivateMessage(member, "Info:", ZweiBot.getInstance().getFileHandler().getString("Message.reload_discord"), Color.GREEN);
                    player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.reload_ingame"));
                }
            });
        }).start();
    }

    public String getRank(ProxiedPlayer p) {
        String rank = "";
        for (int i = 0; i < ZweiBot.getInstance().getFileHandler().ranklist.size(); i++) {
            if (p.hasPermission(ZweiBot.getInstance().getFileHandler().ranklist.get(i).split("; ")[1])) {
                rank = ZweiBot.getInstance().getFileHandler().ranklist.get(i).split("; ")[0];
                break;
            }
        }
        return rank;
    }
}

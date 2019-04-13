package de.einjava.zweibot.listener;

import de.einjava.zweibot.ZweiBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;

/**
 * Created by Leon on 30.10.2018.
 * development with love.
 * © Copyright by Papiertuch
 */

public class GuildVoiceJoinListener extends ListenerAdapter {

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        if (event.getChannelJoined().getId().equalsIgnoreCase(ZweiBot.getInstance().getFileHandler().getString("Module.support.channel"))) {
            for (ProxiedPlayer a : ProxyServer.getInstance().getPlayers()) {
                if (a.hasPermission(ZweiBot.getInstance().getFileHandler().getString("Module.support.permission"))) {
                    a.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.supportwaiting").replace("%client%", event.getMember().getEffectiveName()).replace("%channel%", event.getChannelJoined().getName()));
                }
            }
            for (Member member : event.getGuild().getMembersWithRoles(event.getJDA().getRolesByName(ZweiBot.getInstance().getFileHandler().getString("Module.support.role"), true))) {
                member.getUser().openPrivateChannel().complete().sendMessage(new MessageBuilder(new EmbedBuilder()
                        .setAuthor("Support:")
                        .setColor(Color.YELLOW)
                        .setFooter("@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator(), member.getUser().getAvatarUrl())
                        .setDescription("**" + event.getMember().getUser().getName() + "** wartet im Support!")).build()).complete();
            }
        }
    }
}

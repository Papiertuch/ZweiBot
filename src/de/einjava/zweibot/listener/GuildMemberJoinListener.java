package de.einjava.zweibot.listener;

import de.einjava.zweibot.ZweiBot;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Created by Leon on 13.08.2018.
 * development with love.
 * © Copyright by Papiertuch
 */

public class GuildMemberJoinListener extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        event.getGuild().getController().addRolesToMember(event.getMember(), event.getGuild().getRolesByName(ZweiBot.getInstance().getFileHandler().getString("Module.verify.default_rank"), true)).complete();
    }
}

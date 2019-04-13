package de.einjava.zweibot;

import de.einjava.zweibot.listener.GuildMemberJoinListener;
import de.einjava.zweibot.listener.GuildVoiceJoinListener;
import de.einjava.zweibot.listener.MessageReceivedListener;
import de.einjava.zweibot.utils.FileHandler;
import de.einjava.zweibot.utils.Methods;
import de.einjava.zweibot.utils.MySQL;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Leon on 09.08.2018.
 * development with love.
 * © Copyright by Papiertuch
 */

public class ZweiBot {

    public static HashMap<ProxiedPlayer, String> name = new HashMap<>();
    public static HashMap<ProxiedPlayer, User> member = new HashMap<>();
    private static JDA jda;
    private static MySQL mySQL;
    private static FileHandler fileHandler;
    private static ZweiBot instance;
    private static Methods methods;

    public static ZweiBot getInstance() {
        return instance;
    }

    public void startBot() {
        instance = new ZweiBot();
        fileHandler = new FileHandler();
        methods = new Methods();
        fileHandler.loadConfig();
        fileHandler.loadRanks();
        mySQL = new MySQL();
        if (ZweiBot.getInstance().getFileHandler().getBoolean("Module.verify.enable")) {
            mySQL.createTable();
        }
        try {
            JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
            jdaBuilder.setToken(getFileHandler().getString("Discord.Token"));
            jdaBuilder.setAutoReconnect(true);
            jdaBuilder.setStatus(OnlineStatus.ONLINE);
            jdaBuilder.setActivity(Activity.playing(getFileHandler().getString("Discord.Game")));
            register(jdaBuilder);
            jda = jdaBuilder.build();
            sendMessage("Der DiscordBot ist nun online!");
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage("Leider gab es einen Fehler beim starten....");
        }
    }

    private void register(JDABuilder jdaBuilder) {
        jdaBuilder.addEventListeners(new MessageReceivedListener());
        jdaBuilder.addEventListeners(new GuildMemberJoinListener());
        if (getFileHandler().getBoolean("Module.support.enable")) {
            jdaBuilder.addEventListeners(new GuildVoiceJoinListener());
        }
    }

    public void sendPrivateMessage(User member, String author, String message, Color color) {
        member.openPrivateChannel().complete().sendMessage(new MessageBuilder(new EmbedBuilder()
                .setAuthor(author)
                .setColor(color)
                .setFooter("@" + member.getName() + "#" + member.getDiscriminator(), member.getAvatarUrl())
                .setDescription(message)).build()).queue();
    }

    public Methods getMethods() {
        return methods;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public void sendMessage(String message) {
        ProxyServer.getInstance().getConsole().sendMessage(message);
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public JDA getJda() {
        return jda;
    }
}

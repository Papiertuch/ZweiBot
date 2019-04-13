package de.einjava.zweibot.listener;

import de.einjava.zweibot.ZweiBot;
import de.einjava.zweibot.utils.VerifyHandler;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;

/**
 * Created by Leon on 09.08.2018.
 * development with love.
 * © Copyright by Papiertuch
 */

public class MessageReceivedListener extends ListenerAdapter {

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;
        String command = event.getMessage().getContentStripped();
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (command.startsWith("!" + ZweiBot.getInstance().getFileHandler().getString("Module.verify.command"))) {
            if (args.length == 2) {
                VerifyHandler verify = new VerifyHandler(event.getAuthor().getId());
                if (verify.isExistPlayer()) {
                    if (verify.getTypebyID() == 3) {
                        ZweiBot.getInstance().sendPrivateMessage(event.getAuthor(), "Info:", "Du bist bereits verifiziert!", Color.RED);
                        return;
                    }
                }
                String name = args[1];
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
                if (player == null) {
                    ZweiBot.getInstance().sendPrivateMessage(event.getAuthor(), "Info:", "Dieser Spieler ist offline", Color.RED);
                } else if (new VerifyHandler(player).getTypebyUUID() == 3) {
                    ZweiBot.getInstance().sendPrivateMessage(event.getAuthor(), "Info:", "Dieser Spieler ist bereits verifiziert", Color.RED);
                } else {
                    ZweiBot.getInstance().sendPrivateMessage(event.getAuthor(), "Info:", "Es wurde eine Anfrage versendet", Color.YELLOW);
                    VerifyHandler verifyManager = new VerifyHandler(player);
                    verifyManager.createPlayerIngame();
                    verifyManager.setTypebyUUID(2);
                    ZweiBot.member.put(player, event.getAuthor());
                    ZweiBot.name.put(player, event.getAuthor().getName());
                    player.sendMessage(ZweiBot.getInstance().getFileHandler().getString("Message.request").replace("%client%", event.getAuthor().getName()));
                    sendRequest(player);
                }
            } else {
                ZweiBot.getInstance().sendPrivateMessage(event.getAuthor(), "Fehler:", "Bitte verwenden **!" + ZweiBot.getInstance().getFileHandler().getString("Module.verify.command") + " <Name>**", Color.RED);
            }
        }
    }

    private void sendRequest(ProxiedPlayer player) {
        TextComponent accept = new TextComponent(ZweiBot.getInstance().getFileHandler().getString("Message.hover_accept"));
        accept.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(ZweiBot.getInstance().getFileHandler().getString("Message.hover_accept"))
                                .create()));
        accept.setClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + ZweiBot.getInstance().getFileHandler().getString("Module.verify.command") + " accept"));

        TextComponent deny = new TextComponent(ZweiBot.getInstance().getFileHandler().getString("Message.hover_deny"));
        deny.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(ZweiBot.getInstance().getFileHandler().getString("Message.hover_deny"))
                                .create()));
        deny.setClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + ZweiBot.getInstance().getFileHandler().getString("Module.verify.command") + " deny"));

        TextComponent txt = new TextComponent(" §8┃§r ");
        TextComponent msg = new TextComponent(ZweiBot.getInstance().getFileHandler().getString("Message.request_text"));
        msg.addExtra(accept);
        msg.addExtra(txt);
        msg.addExtra(deny);
        player.sendMessage(msg);
    }
}

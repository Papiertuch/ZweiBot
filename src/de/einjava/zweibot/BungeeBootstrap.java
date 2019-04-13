package de.einjava.zweibot;

import de.einjava.zweibot.command.Verify;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

/**
 * Created by Leon on 09.08.2018.
 * development with love.
 * © Copyright by Papiertuch
 */

public class BungeeBootstrap extends Plugin {

    private static BungeeBootstrap instance;

    public static BungeeBootstrap getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        sendHeader();
        new ZweiBot().startBot();
        PluginManager pluginManager = BungeeCord.getInstance().getPluginManager();
        if (ZweiBot.getInstance().getFileHandler().getBoolean("Module.verify.enable")) {
            pluginManager.registerCommand(this, new Verify());
        }
    }

    @Override
    public void onDisable() {
        if (ZweiBot.getInstance().getMySQL().getConnection() != null) {
            ZweiBot.getInstance().getMySQL().disconnect();
        }
    }

    private void sendHeader() {
        System.out.print(" ______            _______       _   ");
        System.out.print("|___  /           (_) ___ \\     | |  ");
        System.out.print("   / /_      _____ _| |_/ / ___ | |_ ");
        System.out.print("  / /\\ \\ /\\ / / _ \\ | ___ \\/ _ \\| __|");
        System.out.print("./ /__\\ V  V /  __/ | |_/ / (_) | |_ ");
        System.out.print("\\_____/\\_/\\_/ \\___|_\\____/ \\___/ \\__|");
        System.out.print("                                     ");
        System.out.print("» ZweiBot Discord Verifizierung von Papiertuch");
        System.out.print("» Version: " + getDescription().getVersion());
        System.out.print("» Java: " + System.getProperty("java.version") + " System: " + System.getProperty("os.name"));
        System.out.print("                                     ");
    }
}

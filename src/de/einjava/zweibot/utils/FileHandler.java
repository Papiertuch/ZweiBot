package de.einjava.zweibot.utils;

import de.einjava.zweibot.BungeeBootstrap;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 09.08.2018.
 * development with love.
 * © Copyright by Papiertuch
 */

public class FileHandler {

    private static final ConfigurationProvider configurationProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
    private static final Path configPath = Paths.get("plugins/ZweiBot/config.yml");
    public static List<String> ranklist;
    private static Configuration configuration;
    private static ArrayList<String> ranks = new ArrayList<>();

    public String getString(String key) {
        return configuration.getString(key)
                .replace("%prefix%", configuration.getString("Prefix"))
                .replace("&", "§");
    }

    public Boolean getBoolean(String key) {
        return configuration.getBoolean(key);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Integer getInt(String key) {
        return configuration.getInt(key);
    }


    public void loadConfig() {
        try {
            if (!Files.exists(Paths.get("plugins/ZweiBot"))) {
                Files.createDirectories(Paths.get("plugins/ZweiBot"));
            }
            if (Files.exists(configPath)) {
                try (InputStream inputStream = Files.newInputStream(configPath); InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                    configuration = configurationProvider.load(inputStreamReader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            configuration = new Configuration();

            configuration.set("MySQL.Host", "localhost");
            configuration.set("MySQL.Database", "verify");
            configuration.set("MySQL.User", "root");
            configuration.set("MySQL.Password", "1234");

            configuration.set("Discord.Token", "NDc3MTQ5NzA2MDg3MTcwMDYw.Dk4gWw.-lK9xz6vZDYkiHJRMWhvX2U9l18");
            configuration.set("Discord.Game", "noch nichts...");

            configuration.set("Prefix", "§cVerify §8»§7");

            configuration.set("Module.support.enable", true);
            configuration.set("Module.support.channel", "'ID'");
            configuration.set("Module.support.role", "Support");
            configuration.set("Module.support.permission", "zweibot.support");

            configuration.set("Module.cloudnet.enable", false);
            configuration.set("Module.verify.enable", true);
            configuration.set("Module.verify.command", "verify");
            configuration.set("Module.verify.default_rank", "default");
            configuration.set("Module.verify.verify_rank", "Verifiziert");


            configuration.set("Message.supportwaiting", "%prefix% %client% wartet im %channel%");
            configuration.set("Message.verify", "%prefix% Schreibe mich im Discord mit !verify <Name> an!");
            configuration.set("Message.synchronize", "%prefix% &eDaten werden Synchronisiert...");
            configuration.set("Message.reload_discord", "Dein Client wurde neugeladen");
            configuration.set("Message.reload_ingame", "%prefix% &aDaten wurden geladen");
            configuration.set("Message.not_verify", "%prefix% &cDu bist nicht verifiziert!");
            configuration.set("Message.request", "%prefix% &e%client% &7möchte sich mit dir verifizieren");
            configuration.set("Message.request_text", "%prefix% &7Bestätigen mit: ");
            configuration.set("Message.hover_accept", "&8[&aJa&8]");
            configuration.set("Message.hover_deny", "&8[&cNein&8]");
            configuration.set("Message.norequest", "%prefix% &cDu hast keine Anfrage erhalten!");
            configuration.set("Message.verify_deny", "%prefix% &7Du hast die Anfrage von &e%client% &7abgehlehnt!");
            configuration.set("Message.help_delete", "%prefix% &cBenutze &8\u00BB &7/verify delete");
            configuration.set("Message.help_help", "%prefix% &cBenutze &8\u00BB &7/verify help");
            configuration.set("Message.help_info", "%prefix% &cBenutze &8\u00BB &7/verify info");
            configuration.set("Message.help_update", "%prefix% &cBenutze &8\u00BB &7/verify update");
            configuration.set("Message.notverify", "%prefix% &cDu bist nicht verifiziert!");
            configuration.set("Message.delete", "%prefix% &cDu hast alle deine Daten gelöscht!");


            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(configPath), StandardCharsets.UTF_8)) {
                configurationProvider.save(configuration, outputStreamWriter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRanks() {
        if (!BungeeBootstrap.getInstance().getDataFolder().exists()) {
            BungeeBootstrap.getInstance().getDataFolder().mkdir();
        }
        try {
            File file = new File(BungeeBootstrap.getInstance().getDataFolder().getPath(), "ranks.yml");
            if (!file.exists()) {
                file.createNewFile();
                Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                ranks.add("Admin; einbot.rank.admin");
                ranks.add("Developer; einbot.rank.dev");
                ranks.add("SrModerator; einbot.rank.srmod");
                ranks.add("Moderator; einbot.rank.mod");
                ranks.add("Supporter; einbot.rank.sup");
                ranks.add("YouTuber; einbot.rank.youtuber");
                ranks.add("Premium; einbot.rank.premium");
                ranks.add("Spieler; einbot.rank.default");
                cfg.set("Ranks", ranks);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
            }
            Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            ranklist = cfg.getStringList("Ranks");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}

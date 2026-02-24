package com.wunanc.DomFly.core;

import com.wunanc.DomFly.DomFly;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public final class Context {
    private final DomFly plugin;
    private FileConfiguration config;
    private FileConfiguration lang;
    private final Map<String, String> templateCache = new ConcurrentHashMap<>();
    public Context(DomFly plugin) {
        this.plugin = plugin;
        reload();
    }
    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        saveDefaultLang();
        this.lang = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "lang.yml"));
        templateCache.clear();
    }
    private void saveDefaultLang() {
        File langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }
    }
    public boolean getBool(String path, boolean def) {
        return config.getBoolean(path, def);
    }
    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }
    public long getLong(String path, long def) {
        return config.getLong(path, def);
    }
    public FileConfiguration getFileConfig() {
        return config;
    }
    public DomFly getPlugin() {
        return plugin;
    }
    public void log(String key, Map<String, String> params) {
        log(Level.INFO, key, params);
    }
    public void log(Level level, String key, Map<String, String> params) {
        String msg = PlainTextComponentSerializer.plainText().serialize(format(getRawTemplate(key), params));
        plugin.getLogger().log(level, "[DomFly] " + msg);
    }
    public void send(CommandSender sender, String key) {
        send(sender, key, Map.of());
    }
    public void send(CommandSender sender, String key, Map<String, String> params) {
        String prefix = sender instanceof org.bukkit.entity.Player ? "" : "[DomFly] ";
        Component formattedMsg = format(getRawTemplate(key), params);
        if (!prefix.isEmpty()) {
            Component prefixComp = LegacyComponentSerializer.legacyAmpersand().deserialize(prefix);
            sender.sendMessage(prefixComp.append(formattedMsg));
        } else {
            sender.sendMessage(formattedMsg);
        }
    }
    private String getRawTemplate(String key) {
        return templateCache.computeIfAbsent(key, k -> {
            String def = "&c[" + k + "]";
            return lang.getString(k, config.getString("lang_default." + k, def));
        });
    }
    private Component format(String template, Map<String, String> params) {
        if (template == null) {
            return Component.empty();
        }
        String processed = template;
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                processed = processed.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return LegacyComponentSerializer.legacyAmpersand().deserialize(processed);
    }
}
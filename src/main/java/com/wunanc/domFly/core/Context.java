package com.wunanc.domfly.core;

import com.wunanc.domfly.DomFly;
import org.bukkit.ChatColor;
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
    private final Map<String, String> messageCache = new ConcurrentHashMap<>();

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
        messageCache.clear();
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
    public String getString(String path, String def) {
        return config.getString(path, def);
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
        String msg = getRaw(key);
        plugin.getLogger().log(level, "[DomFly] " + format(msg, params));
    }

    public void send(CommandSender sender, String key) {
        send(sender, key, Map.of());
    }

    public void send(CommandSender sender, String key, Map<String, String> params) {
        String prefix = sender instanceof org.bukkit.entity.Player ? "" : "[DomFly] ";
        String msg = getRaw(key);
        sender.sendMessage(prefix + format(msg, params));
    }

    private String getRaw(String key) {
        if (messageCache.containsKey(key)) return messageCache.get(key);

        String raw = lang.getString(key, config.getString("lang_default." + key, "&c[" + key + "]"));
        messageCache.put(key, raw);
        return raw;
    }

    private String format(String template, Map<String, String> params) {
        if (template == null || params == null) return ChatColor.translateAlternateColorCodes('&', template);
        String result = template;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return ChatColor.translateAlternateColorCodes('&', result);
    }
}

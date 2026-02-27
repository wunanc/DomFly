package top.wunanc.domfly.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Configuration {

    private final JavaPlugin plugin;
    private final File configFile;
    private YamlConfiguration config;

    // 配置字段
    private boolean debug;

    // 默认值
    private static final boolean DEFAULT_DEBUG = true;

    public Configuration(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        load();
    }

    /**
     * 加载配置文件，若不存在则创建默认配置
     */
    public void load() {
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        loadFields();
    }

    /**
     * 从YAML配置中读取字段到内存
     */
    private void loadFields() {
        debug = config.getBoolean("debug", DEFAULT_DEBUG);
    }

    /**
     * 重载配置文件（丢弃内存修改，重新从文件加载）
     */
    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
        loadFields();
    }

    // Getter 和 Setter 方法

    public boolean isDebug() {
        return debug;
    }
}
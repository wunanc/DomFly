package top.wunanc.domfly.config;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import top.wunanc.domfly.utils.LegacyToMiniMessage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class LanguageManager {

    private final JavaPlugin plugin;
    private final File langFile;
    private YamlConfiguration langConfig;

    // 语言字段
    private String pluginPrefix;

    // 默认值
    private static final String DEFAULT_PLUGIN_PREFIX = "&f[&aDomFly&f]";

    public LanguageManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.langFile = new File(plugin.getDataFolder(), "lang.yml");
        load();
    }

    /**
     * 加载语言文件，检查缺失项并补充
     */
    public void load() {
        // 如果文件不存在，从资源中复制默认文件
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }

        // 加载配置
        langConfig = YamlConfiguration.loadConfiguration(langFile);

        // 检查并补充缺失的键值
        checkAndFillMissingKeys();

        // 加载字段到内存
        loadFields();
    }

    /**
     * 检查并补充缺失的语言键值
     */
    private void checkAndFillMissingKeys() {
        boolean needsSave = false;

        // 检查每个必需的键是否存在
        if (!langConfig.contains("PluginPrefix")) {
            langConfig.set("PluginPrefix", DEFAULT_PLUGIN_PREFIX);
            needsSave = true;
        }

        // 这里可以添加更多语言键的检查
        // 例如：
        // if (!langConfig.contains("Messages.FlyEnabled")) {
        //     langConfig.set("Messages.FlyEnabled", "&a领地飞行已开启！你可以在自己的领地内飞行。");
        //     needsSave = true;
        // }

        // 如果有缺失项，保存更新后的配置
        if (needsSave) {
            try {
                langConfig.save(langFile);
                plugin.getLogger().info("语言文件已更新，补充了缺失的键值");
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "无法保存语言文件: " + e.getMessage(), e);
            }
        }
    }

    /**
     * 从YAML配置中读取字段到内存
     */
    private void loadFields() {
        pluginPrefix = langConfig.getString("PluginPrefix", DEFAULT_PLUGIN_PREFIX);
    }

    /**
     * 重载语言文件
     */
    public void reload() {
        langConfig = YamlConfiguration.loadConfiguration(langFile);
        checkAndFillMissingKeys();
        loadFields();
    }

    /**
     * 获取插件前缀（已解析颜色代码）
     */
    public Component getPluginPrefix() {
        return LegacyToMiniMessage.parse(pluginPrefix);
    }


    /**
     * 根据键名获取转换mm的消息
     * @param key 键名
     * @return 原始消息，如果键不存在则返回null
     */
    public Component getMessage(String key) {
        return LegacyToMiniMessage.parse(langConfig.getString(key));
    }

}

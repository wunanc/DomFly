package top.wunanc.DomFly;

import org.bukkit.plugin.java.JavaPlugin;

public final class DomFly extends JavaPlugin {
    
    private ConfigManager configManager;
    private Fly fly;

    @Override
    public void onEnable() {
        // 初始化配置管理器
        configManager = new ConfigManager(this);
        
        // 初始化Fly类时传递ConfigManager
        fly = new Fly(this, configManager);
        
        // 注册命令
        MainCommand mainCommand = new MainCommand(this, fly);
        getCommand("domfly").setExecutor(mainCommand);
        getCommand("domfly").setTabCompleter(mainCommand);
        
        getLogger().info("DomFly插件已启用！");
    }

    @Override
    public void onDisable() {
        getLogger().info("DomFly插件已禁用！");
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public Fly getFly() {
        return fly;
    }
    
    public void reloadPluginConfig() {
        configManager.reloadConfig();
    }
}

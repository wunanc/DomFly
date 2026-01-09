package com.wunanc.domFly;

import cn.lunadeer.dominion.api.DominionAPI;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.http.WebSocket;

public final class DomFly extends JavaPlugin implements WebSocket.Listener {

    private DominionAPI dominionAPI;
    private Fly Fly;

    @Override
    public void onEnable() {
        //获取DominionAPI实例
        if (Bukkit.getPluginManager().isPluginEnabled("Dominion")) {
            dominionAPI = DominionAPI.getInstance();
            this.getLogger().info("已成功挂钩到Dominion");
            int pluginId = 28704;
            Metrics metrics = new Metrics(this, pluginId);
        } else {
            throw new IllegalStateException("Dominion 插件未启用!请确保已安装并启用它.");
        }
        saveDefaultConfig();
        loadConfig();
        registerCommands();

        this.Fly = new Fly(this);
    }

    private void registerCommands() {
        Fly Fly = new Fly(this);
        MainCommand mainCommand = new MainCommand(this, Fly);

        this.getCommand("Domfly").setExecutor(mainCommand);
        this.getCommand("Domfly").setTabCompleter(mainCommand);
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        this.getLogger().info("配置文件已加载");
    }

    public void reloadPluginConfig() {
        this.reloadConfig();
        getLogger().info("插件配置已重载");
    }

    @Override
    public void onDisable() {

    }
}

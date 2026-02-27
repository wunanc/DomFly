package top.wunanc.domfly;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import top.wunanc.domfly.commands.MainCommand;
import top.wunanc.domfly.config.Configuration;
import top.wunanc.domfly.config.LanguageManager;
import top.wunanc.domfly.handler.Fly;
import top.wunanc.domfly.hooks.Bstats;

public final class DomFly extends JavaPlugin {
    
    private Configuration configuration;
    private LanguageManager languageManager;
    private final CommandSender console = Bukkit.getConsoleSender();
    private Fly fly;
    private Component PlgPre = Component.text("[DomFly] ").color(NamedTextColor.GREEN);

    @Override
    public void onEnable() {
        init();
        // 注册命令
        registerCommand();

        sendlog(console, Component.text("DomFly插件已启动！").color(NamedTextColor.GREEN));
    }

    @Override
    public void onDisable() {
        sendlog(console, Component.text("DomFly插件已禁用！").color(NamedTextColor.RED));
    }

    public void init() {
        Metrics metrics = new Metrics(this, 28704);
        configuration = new Configuration(this);
        languageManager = new LanguageManager(this);
        fly = new Fly(this, configuration, languageManager);
        Bstats bstats = new Bstats(metrics, fly);
        bstats.FlightTable();
        PlgPre = languageManager.getPluginPrefix();
    }

    private void sendlog(CommandSender console, Component log) {
        console.sendMessage(PlgPre.append(log));
    }


    public void registerCommand() {
        var command = getCommand("domfly");
        if (command != null) {
            MainCommand mainCommand = new MainCommand(this, fly, languageManager);
            command.setExecutor(mainCommand);
            command.setTabCompleter(mainCommand);
        } else {
            sendlog(console, Component.text("无法注册domfly命令，请联系开发者或发送issues!").color(NamedTextColor.RED));
        }
    }

    public void reloadPluginConfig() {
        configuration.reload();
        languageManager.reload();
    }
}

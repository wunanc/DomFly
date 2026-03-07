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
import top.wunanc.domfly.hooks.LuckPermsListener;

public final class DomFly extends JavaPlugin {

    private Configuration configuration;
    private LanguageManager languageManager;
    private final CommandSender console = Bukkit.getConsoleSender();
    private Fly fly;
    private Component PlgPre = Component.text("[DomFly] ").color(NamedTextColor.GREEN);

    @Override
    public void onEnable() {
        // 先初始化，如果初始化失败（比如没装前置），则不注册后续内容
        if (!init()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        registerCommand();
        sendlog(console, Component.text("DomFly插件已启动！").color(NamedTextColor.GREEN));
    }

    @Override
    public void onDisable() {
        if (fly != null) {
            fly.disableAllFlight();
        }
        sendlog(console, Component.text("DomFly插件已禁用！").color(NamedTextColor.RED));
    }

    /**
     * 初始化方法
     * @return 如果初始化成功返回 true
     */
    public boolean init() {
        this.configuration = new Configuration(this);
        this.languageManager = new LanguageManager(this);

        this.PlgPre = languageManager.getPluginPrefix();

        if (Bukkit.getPluginManager().getPlugin("Dominion") == null) {
            sendlog(console, Component.text("未找到 Dominion 插件! 插件已禁用。").color(NamedTextColor.RED));
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        sendlog(console, Component.text("成功挂载 Dominion。").color(NamedTextColor.AQUA));

        this.fly = new Fly(this, configuration, languageManager);

        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            new LuckPermsListener(this, fly).register();
            sendlog(console, Component.text("成功挂载 LuckPerms。").color(NamedTextColor.AQUA));
        } else sendlog(console, Component.text("未找到 LuckPerms 插件，可能导致部分功能无法使用!").color(NamedTextColor.YELLOW));

        Metrics metrics = new Metrics(this, 28704);
        Bstats bstats = new Bstats(metrics, fly);
        bstats.FlightTable();

        return true;
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
        }
    }

    public void reloadPluginConfig() {
        configuration.reload();
        languageManager.reload();
    }
}
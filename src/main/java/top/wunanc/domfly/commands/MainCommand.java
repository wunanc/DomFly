package top.wunanc.domfly.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.wunanc.domfly.DomFly;
import top.wunanc.domfly.config.LanguageManager;
import top.wunanc.domfly.handler.Fly;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final DomFly plugin;
    private final Fly domflyListener;
    private final LanguageManager languageManager;

    public MainCommand(DomFly plugin, Fly domflyListener, LanguageManager languageManager) {
        this.plugin = plugin;
        this.domflyListener = domflyListener;
        this.languageManager = languageManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // 主命令（无参数） - 执行飞行功能
        if (args.length == 0) {
            // 检查发送者是否为玩家
            if (!(sender instanceof Player player)) {
                sendMessage(sender, languageManager.getMessage("OnlyPlayer"));
                return true;
            }

            domflyListener.executeFlyCommand(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                if (!sender.hasPermission("domfly.use")) {
                    sendMessage(sender, languageManager.getMessage("NoPermission"));
                    return true;
                }
                CommandHelp.sendHelp(sender, languageManager);
                break;

            case "reload":
                // 检查权限
                if (!sender.hasPermission("domfly.admin") && !sender.isOp()) {
                    sendMessage(sender, languageManager.getMessage("NoPermission"));
                    return true;
                }

                // 执行重载
                try {
                    plugin.reloadPluginConfig();
                    //成功
                    sendMessage(sender, languageManager.getMessage("Reload.Success"));
                } catch (Exception e) {
                    //错误处理
                    sendMessage(sender, languageManager.getMessage("Reload.Error").append(Component.text(e.getMessage())));
                    sendMessage(Bukkit.getConsoleSender(), languageManager.getMessage("Reload.Error").append(Component.text(e.getMessage())));
                }
                break;

            case "undomfly":
                // 检查权限
                if (!sender.hasPermission("domfly.admin") && !sender.isOp()) {
                    sendMessage(sender, languageManager.getMessage("NoPermission"));
                    return true;
                }

                // 检查参数数量
                if (args.length < 2) {
                    sendMessage(sender, Component.text("usage: /Domfly undomfly <player>").color(NamedTextColor.RED));
                    return true;
                }

                // 获取目标玩家
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sendMessage(sender, languageManager.getMessage("CanNotFindPlayer").append(Component.text(args[1]).color(NamedTextColor.YELLOW)));
                    return true;
                }

                // 强制关闭领地飞行
                forceDisableDomfly(target, sender);
                break;

            default:
                //未知命令
                sendMessage(sender, languageManager.getMessage("UnknownCommand"));
                break;
        }

        return true;
    }

    /**
     * 强制关闭玩家的领地飞行
     */
    private void forceDisableDomfly(Player target, CommandSender executor) {
        try {
            if (domflyListener != null) {
                domflyListener.forceDisableFlight(target);
                Component message = languageManager.getMessage("SudoDisabledexecutor")
                        .replaceText(builder -> builder.matchLiteral("{player}").replacement(target.getName()));
                sendMessage(executor, message);
            } else {
                sendMessage(executor, languageManager.getMessage("CannotVisitListener"));
            }
        } catch (Exception e) {
            //错误捕捉
            sendMessage(executor, languageManager.getMessage("SudoDisableError").append(Component.text(e.getMessage())));
            sendMessage(Bukkit.getConsoleSender(), languageManager.getMessage("SudoDisableError").append(Component.text(e.getMessage())));
        }
    }

    public void sendMessage(CommandSender sender, Component message) {
        if (message != null) {
            sender.sendMessage(languageManager.getPluginPrefix().append(message));
        } else sender.sendMessage(Component.text("Can't find the message, please contact your administrator to check the DomFly language file!").color(NamedTextColor.RED));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // 第一级补全：显示所有可用命令
            List<String> commands = new ArrayList<>();

            // 所有玩家都能看到 help 和无参数命令（飞行功能）
            commands.add("help");

            // 只有有权限的玩家才能看到 reload 和 undomfly
            if (sender.hasPermission("domfly.admin") || sender.isOp()) {
                commands.add("reload");
            }
            if (sender.hasPermission("domfly.admin") || sender.isOp()) {
                commands.add("undomfly");
            }

            // 过滤匹配当前输入的命令
            for (String cmd : commands) {
                if (cmd.startsWith(args[0].toLowerCase())) {
                    completions.add(cmd);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("undomfly")) {
            // undomfly 命令的玩家名补全
            if (sender.hasPermission("domfly.admin") || sender.isOp()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(player.getName());
                    }
                }
            }
        }

        return completions;
    }
}
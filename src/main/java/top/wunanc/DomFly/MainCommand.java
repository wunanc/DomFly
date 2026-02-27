package top.wunanc.DomFly;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final DomFly plugin;
    private final Fly domflyListener;

    public MainCommand(DomFly plugin, Fly domflyListener) {
        this.plugin = plugin;
        this.domflyListener = domflyListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 主命令（无参数） - 执行飞行功能
        if (args.length == 0) {
            // 检查发送者是否为玩家
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c只有玩家可以使用此命令！");
                return true;
            }

            Player player = (Player) sender;
            domflyListener.executeFlyCommand(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                if (!sender.hasPermission("domfly.admin")) {
                    sender.sendMessage("§c你没有权限执行此命令！");
                    return true;
                }
                CommandHelp.sendHelp1(sender);
                break;

            case "reload":
                // 检查权限
                if (!sender.hasPermission("domfly.admin") && !sender.isOp()) {
                    sender.sendMessage("§c你没有权限执行此命令！");
                    return true;
                }

                // 执行重载
                try {
                    plugin.reloadPluginConfig();
                    sender.sendMessage("§a插件配置已重载!");
                } catch (Exception e) {
                    sender.sendMessage("§c重载配置时发生错误: " + e.getMessage());
                    plugin.getLogger().severe("重载配置失败: " + e.getMessage());
                }
                break;

            case "undomfly":
                // 检查权限
                if (!sender.hasPermission("domfly.admin") && !sender.isOp()) {
                    sender.sendMessage("§c你没有权限执行此命令！");
                    return true;
                }

                // 检查参数数量
                if (args.length < 2) {
                    sender.sendMessage("§c用法: /Domfly undomfly <玩家名>");
                    return true;
                }

                // 获取目标玩家
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§c找不到玩家: " + args[1]);
                    return true;
                }

                // 强制关闭领地飞行
                forceDisableDomfly(target, sender);
                break;

            default:
                sender.sendMessage("§c未知命令，使用 /Domfly help 查看帮助");
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
                executor.sendMessage("§a已强制关闭玩家 " + target.getName() + " 的领地飞行");
                target.sendMessage("§c管理员已强制关闭你的领地飞行");
            } else {
                executor.sendMessage("§c错误：无法访问领地飞行监听器");
            }
        } catch (Exception e) {
            executor.sendMessage("§c执行失败: " + e.getMessage());
            plugin.getLogger().warning("强制关闭领地飞行时出错: " + e.getMessage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
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
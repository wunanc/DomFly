package top.wunanc.domfly.commands;

import org.bukkit.command.CommandSender;

public class CommandHelp {

    public static void sendHelp1(CommandSender sender) {
        sender.sendMessage("§6=== 插件帮助 1 ===");
        sender.sendMessage("§a/Domfly help §7- 查看详细帮助");
        sender.sendMessage("§a/Domfly reload §7- 重载插件配置");
        sender.sendMessage("§6==================");
    }
}

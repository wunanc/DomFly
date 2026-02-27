package top.wunanc.domfly.commands;

import org.bukkit.command.CommandSender;
import top.wunanc.domfly.config.LanguageManager;

public class CommandHelp {

    public static void sendHelp(CommandSender sender, LanguageManager languageManager) {



        sender.sendMessage(languageManager.getMessage("PlgHelp.help1"));
        sender.sendMessage(languageManager.getMessage("PlgHelp.help2"));
        sender.sendMessage(languageManager.getMessage("PlgHelp.help3"));
        if (sender.hasPermission("domfly.admin")) {
            sender.sendMessage(languageManager.getMessage("PlgHelp.help4"));
            sender.sendMessage(languageManager.getMessage("PlgHelp.help5"));
        }
        sender.sendMessage(languageManager.getMessage("PlgHelp.help6"));
    }
}

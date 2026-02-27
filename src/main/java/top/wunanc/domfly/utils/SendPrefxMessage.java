package top.wunanc.domfly.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import top.wunanc.domfly.config.LanguageManager;

public class SendPrefxMessage {
    private final LanguageManager languageManager;
    
    public SendPrefxMessage(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }
    
    public void send(CommandSender sender, Component message) {
        sender.sendMessage(languageManager.getPluginPrefix().append(message));
    }
}
package com.wunanc.DomFly.command;

import com.wunanc.DomFly.core.Context;
import com.wunanc.DomFly.exception.FlightCommandException;
import com.wunanc.DomFly.service.FlightService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class CommandRegistry implements CommandExecutor, TabCompleter {

    private final Context context;
    private final FlightService flightService;
    private final Map<String, CommandHandler> handlers = new HashMap<>();
    public CommandRegistry(@NotNull Context context, @NotNull FlightService flightService) {
        this.context = context;
        this.flightService = flightService;
        register("", new ToggleCommand());
        register("help", new HelpCommand());
        register("reload", new ReloadCommand());
        register("undomfly", new ForceDisableCommand());
    }
    private void register(@NotNull String name, @NotNull CommandHandler handler) {
        handlers.put(name.toLowerCase(), handler);
    }
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String @NonNull [] args
    ) {
        String subCmd = args.length > 0 ? args[0].toLowerCase() : "";
        CommandHandler handler = handlers.get(subCmd);
        if (handler == null && !subCmd.isEmpty()) {
            handler = handlers.get("");
        }
        if (handler != null) {
            try {
                handler.execute(sender, args);
                return true;
            } catch (FlightCommandException e) {
                context.send(sender, e.getMessageKey(), e.getParams());
                return true;
            } catch (Exception e) {
                logError(sender, "Unexpected error executing command", e);
                context.send(sender, "error.internal", Map.of("err", e.getMessage()));
                return true;
            }
        }
        return false;
    }
    @Override
    @NotNull
    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String @NonNull [] args
    ) {
        if (args.length == 1) {
            return handlers.keySet().stream()
                    .filter(key -> !key.isEmpty())
                    .filter(key -> "help".equals(key) || sender.hasPermission("DomFly." + key))
                    .filter(key -> key.startsWith(args[0].toLowerCase()))
                    .sorted()
                    .collect(Collectors.toList());
        }
        if (args.length == 2 && "undomfly".equalsIgnoreCase(args[0])) {
            if (!sender.hasPermission("DomFly.admin")) {
                return Collections.emptyList();
            }
            String input = args[1].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    private void logError(CommandSender sender, String message, Throwable e) {
        String senderInfo = sender != null ? sender.getName() : "Unknown";
        context.getPlugin().getLogger().log(Level.SEVERE, message + " (Sender: " + senderInfo + ")", e);
    }
    private void checkPermission(CommandSender sender, String permission) throws FlightCommandException {
        if (!sender.hasPermission(permission)) {
            throw new FlightCommandException("error.no_perm");
        }
    }
    @FunctionalInterface
    private interface CommandHandler {
        void execute(CommandSender sender, String[] args) throws FlightCommandException;
    }
    private final class ToggleCommand implements CommandHandler {
        @Override
        public void execute(@NotNull CommandSender sender, @NotNull String[] args) throws FlightCommandException {
            if (!(sender instanceof Player player)) {
                throw new FlightCommandException("error.player_only");
            }
            checkPermission(sender, "DomFly.use");
            flightService.toggle(player);
        }
    }
    private final class HelpCommand implements CommandHandler {
        @Override
        public void execute(@NotNull CommandSender sender, @NotNull String[] args) throws FlightCommandException {
            checkPermission(sender, "DomFly.use");
            context.send(sender, "admin.help.header");
            context.send(sender, "admin.help.row", Map.of("cmd", "/DomFly", "desc", "Toggle your flight"));
            if (sender.hasPermission("DomFly.admin")) {
                context.send(sender, "admin.help.row",
                        Map.of("cmd", "/DomFly reload", "desc", "Reload configuration"));
                context.send(sender, "admin.help.row",
                        Map.of("cmd", "/DomFly undomfly <player>", "desc", "Force disable flight"));
            }
            context.send(sender, "admin.help.footer");
        }
    }
    private final class ReloadCommand implements CommandHandler {
        @Override
        public void execute(@NotNull CommandSender sender, @NotNull String[] args) throws FlightCommandException {
            checkPermission(sender, "DomFly.admin");
            try {
                context.reload();
                context.send(sender, "admin.reload.success");
            } catch (Exception e) {
                logError(sender, "Failed to reload configuration", e);
                throw new FlightCommandException("admin.reload.fail", Map.of("err", e.getMessage()));
            }
        }
    }
    private final class ForceDisableCommand implements CommandHandler {
        @Override
        public void execute(@NotNull CommandSender sender, @NotNull String[] args) throws FlightCommandException {
            checkPermission(sender, "DomFly.admin");
            if (args.length < 2) {
                throw new FlightCommandException("error.usage",
                        Map.of("usage", "/DomFly undomfly <player>"));
            }
            String targetName = args[1];
            Player target = Bukkit.getPlayer(targetName);
            if (target == null || !target.isOnline()) {
                throw new FlightCommandException("error.player_not_found", Map.of("player", targetName));
            }
            flightService.forceDisable(target);
            context.send(sender, "admin.force.success", Map.of("player", target.getName()));
        }
    }
}

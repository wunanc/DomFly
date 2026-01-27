package com.wunanc.domfly.command;

import com.wunanc.domfly.core.Context;
import com.wunanc.domfly.exception.FlightCommandException;
import com.wunanc.domfly.service.FlightService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public final class CommandRegistry implements CommandExecutor, TabCompleter {

    private final Context context;
    private final FlightService flightService;

    private final Map<String, CommandHandler> handlers = new HashMap<>();

    public CommandRegistry(Context context, FlightService flightService) {
        this.context = context;
        this.flightService = flightService;

        register("", new ToggleCommand());
        register("help", new HelpCommand());
        register("reload", new ReloadCommand());
        register("undomfly", new ForceDisableCommand());
    }

    private void register(String name, CommandHandler handler) {
        handlers.put(name.toLowerCase(), handler);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String subCmd = args.length > 0 ? args[0].toLowerCase() : "";
        CommandHandler handler = handlers.get(subCmd);

        if (handler == null && !subCmd.isEmpty()) {
            handler = handlers.get("");
        }

        if (handler != null) {
            try {
                handler.execute(sender, args);
            } catch (FlightCommandException e) {
                context.send(sender, e.getMessageKey(), e.getParams());
            } catch (Exception e) {
                e.printStackTrace();
                context.send(sender, "error.internal", Map.of("err", e.getMessage()));
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return handlers.keySet().stream()
                    .filter(key -> !key.isEmpty())
                    .filter(key -> "help".equals(key) || sender.hasPermission("domfly." + key))
                    .filter(key -> key.startsWith(args[0].toLowerCase()))
                    .sorted()
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && "undomfly".equalsIgnoreCase(args[0])) {
            if (!sender.hasPermission("domfly.admin")) return Collections.emptyList();

            String input = args[1].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private boolean hasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            throw new FlightCommandException("error.no_perm");
        }
        return true;
    }

    private interface CommandHandler {
        void execute(CommandSender sender, String[] args) throws FlightCommandException;
    }

    private class ToggleCommand implements CommandHandler {
        @Override
        public void execute(CommandSender sender, String[] args) throws FlightCommandException {
            if (!(sender instanceof Player)) {
                throw new FlightCommandException("error.player_only");
            }
            hasPermission(sender, "domfly.use");
            flightService.toggle((Player) sender);
        }
    }

    private class HelpCommand implements CommandHandler {
        @Override
        public void execute(CommandSender sender, String[] args) throws FlightCommandException {
            hasPermission(sender, "domfly.use");

            context.send(sender, "admin.help.header");
            context.send(sender, "admin.help.row", Map.of("cmd", "/domfly", "desc", "Toggle your flight"));

            if (sender.hasPermission("domfly.admin")) {
                context.send(sender, "admin.help.row", Map.of("cmd", "/domfly reload", "desc", "Reload configuration"));
                context.send(sender, "admin.help.row", Map.of("cmd", "/domfly undomfly <player>", "desc", "Force disable flight"));
            }

            context.send(sender, "admin.help.footer");
        }
    }

    private class ReloadCommand implements CommandHandler {
        @Override
        public void execute(CommandSender sender, String[] args) throws FlightCommandException {
            hasPermission(sender, "domfly.admin");

            try {
                context.reload();
                context.send(sender, "admin.reload.success");
            } catch (Exception e) {
                throw new FlightCommandException("admin.reload.fail", Map.of("err", e.getMessage()));
            }
        }
    }

    private class ForceDisableCommand implements CommandHandler {
        @Override
        public void execute(CommandSender sender, String[] args) throws FlightCommandException {
            hasPermission(sender, "domfly.admin");

            if (args.length < 2) {
                throw new FlightCommandException("error.usage", Map.of("usage", "/domfly undomfly <player>"));
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

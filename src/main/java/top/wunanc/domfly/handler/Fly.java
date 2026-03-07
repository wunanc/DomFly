package top.wunanc.domfly.handler;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.DominionAPI;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import top.wunanc.domfly.config.Configuration;
import top.wunanc.domfly.config.LanguageManager;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Fly implements Listener {

    private final JavaPlugin plugin;
    private final Configuration configuration;
    private final Set<UUID> flyingPlayers = ConcurrentHashMap.newKeySet();
    private final LanguageManager languageManager;
    private final DominionAPI dominionAPI = DominionAPI.getInstance();

    public Fly(JavaPlugin plugin, Configuration configuration, LanguageManager languageManager) {
        this.plugin = plugin;
        this.configuration = configuration;
        this.languageManager = languageManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public boolean isFlying(UUID uuid) {
        return flyingPlayers.contains(uuid);
    }

    public void executeFlyCommand(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage(languageManager.getMessage("IsCreativeMode"));
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR) {
            player.sendMessage(languageManager.getMessage("IsSpectatorMode"));
            return;
        }

        if (!player.hasPermission("domfly.use")) {
            player.sendMessage(languageManager.getMessage("NoPermission"));
            return;
        }

        if (isInOwnClaim(player)) {
            // 切换飞行状态
            toggleFlight(player);
        } else {
            player.sendMessage(languageManager.getMessage("OnlyOwner"));

            if (configuration.isDebug()) {
                DominionDTO dominion = dominionAPI.getDominion(player.getLocation());
                String dominionName = dominion != null ? dominion.getName() : "无";
                plugin.getLogger().info("DEBUG - 玩家: " + player.getName() + " 尝试开启飞行，但不在其领地。所在领地: " + dominionName);
            }
        }
    }

    public void toggleFlight(Player player) {
        if (flyingPlayers.contains(player.getUniqueId())) {
            disableFlight(player);
            player.sendMessage(languageManager.getMessage("FlyDisabled"));
        } else {
            enableFlight(player);
            player.sendMessage(languageManager.getMessage("FlyEnabled"));
        }
    }

    private void enableFlight(Player player) {
        flyingPlayers.add(player.getUniqueId());
        player.getScheduler().execute(plugin, () -> {
            player.setAllowFlight(true);
            player.setFlying(true);
        }, null, 0);
    }

    private void disableFlight(Player player) {
        flyingPlayers.remove(player.getUniqueId());
        player.getScheduler().execute(plugin, () -> {
            player.setAllowFlight(false);
            player.setFlying(false);
        }, null, 0);
    }

    private boolean isInOwnClaim(Player player) {
        try {
            DominionDTO dominion = DominionAPI.getInstance().getDominion(player.getLocation());
            if (dominion == null) return false;

            if (dominion.getOwner().equals(player.getUniqueId())) return true;

            return DominionAPI.getInstance().checkPrivilegeFlag(
                    player.getLocation(),
                    Flags.ADMIN,
                    player
            );
        } catch (Exception e) {
            plugin.getLogger().warning("领地检查错误: " + e.getMessage());
            return false;
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (flyingPlayers.contains(player.getUniqueId())) {
            if (!isInOwnClaim(player)) {
                disableFlight(player);
                player.sendMessage(languageManager.getMessage("AutoDisable"));
            }
        }
    }

    /**
     * 监听玩家退出：清理集合防止内存泄漏，并重置状态
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (flyingPlayers.contains(player.getUniqueId())) {
            disableFlight(player);
        }
    }

    /**
     * 关服清理：强制重置所有在线玩家的飞行状态
     */
    public void disableAllFlight() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (flyingPlayers.contains(player.getUniqueId())) {
                player.getScheduler().execute(plugin, () -> {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }, null, 0);
            }
        }
        flyingPlayers.clear();
    }

    /**
     * 强制关闭方法 (供管理员命令或权限变动钩子使用)
     */
    public void forceDisableFlight(Player player, Boolean isAuto) {
        if (flyingPlayers.contains(player.getUniqueId())) {
            disableFlight(player);
            if (!isAuto) player.sendMessage(languageManager.getMessage("SudoDisabled"));
            else player.sendMessage(languageManager.getMessage("DeprivedOfFlightPrivileges"));
        } else {
            player.getScheduler().execute(plugin, () -> {
                player.setAllowFlight(false);
                player.setFlying(false);
            }, null, 0);
        }
    }

    public int getFlyingPlayerCount() {
        return flyingPlayers.size();
    }
}
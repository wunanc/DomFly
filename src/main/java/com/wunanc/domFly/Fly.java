package com.wunanc.domFly;

import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.DominionAPI;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Fly implements Listener {

    private final JavaPlugin plugin;
    private final Set<UUID> flyingPlayers = new HashSet<>();
    DominionAPI dominionAPI = DominionAPI.getInstance();

    public Fly(JavaPlugin plugin) {
        this.plugin = plugin;
        // 注册事件监听器
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void executeFlyCommand(Player player) {
        // 原来onCommand中的逻辑，但去掉玩家检查，因为现在传入的肯定是Player
        if (player.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage(ChatColor.YELLOW + "你已经是创造模式了，无需使用领地飞行！");
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR) {
            player.sendMessage(ChatColor.YELLOW + "你已经是旁观模式了，无需使用领地飞行！");
            return;
        }

        // 检查玩家是否有权限
        if (!player.hasPermission("domfly.use")) {
            player.sendMessage(ChatColor.RED + "你没有使用此命令的权限！");
            return;
        }

        if (!isInOwnClaim(player)) {
            player.sendMessage(ChatColor.RED + "你只能在自己的领地内使用飞行功能！");
            return;
        }

        // 切换飞行状态
        toggleFlight(player);
    }

    /**
     * 切换玩家的飞行状态
     */
    public void toggleFlight(Player player) {
        UUID playerId = player.getUniqueId();

        if (flyingPlayers.contains(playerId)) {
            // 关闭飞行
            disableFlight(player);
            player.sendMessage(ChatColor.YELLOW + "领地飞行已关闭！");
        } else {
            // 开启飞行
            enableFlight(player);
            player.sendMessage(ChatColor.GREEN + "领地飞行已开启！你可以在自己的领地内飞行。");
        }
    }

    /**
     * 启用飞行功能
     */
    private void enableFlight(Player player) {
        UUID playerId = player.getUniqueId();
        flyingPlayers.add(playerId);

        // 设置允许飞行
        player.setAllowFlight(true);
        player.setFlying(true);
    }

    /**
     * 禁用飞行功能
     */
    private void disableFlight(Player player) {
        UUID playerId = player.getUniqueId();
        flyingPlayers.remove(playerId);

        // 取消飞行状态
        player.setAllowFlight(false);
        player.setFlying(false);

    }

    /**
     * 简化的领地检查方法
     */
    private boolean isInOwnClaim(Player player) {
        try {
            DominionDTO dominion = DominionAPI.getInstance().getDominion(player.getLocation());
            if (dominion == null) {
                return false;
            }

            //检查领地所有者UUID是否匹配玩家UUID
            UUID ownerUUID = dominion.getOwner();
            if (ownerUUID != null && ownerUUID.equals(player.getUniqueId())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            plugin.getLogger().warning("领地检查错误: " + e.getMessage());
            return false;
        }
    }

    /**
     * 玩家移动事件 - 检查是否离开自己的领地
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // 如果玩家正在使用领地飞行
        if (flyingPlayers.contains(playerId)) {
            // 检查玩家是否还在自己的领地内
            if (!isInOwnClaim(player)) {
                // 离开领地，自动关闭飞行
                disableFlight(player);
                player.sendMessage(ChatColor.RED + "你已离开自己的领地，飞行功能自动关闭！");
            }
        }
    }

    /**
     * 强制关闭玩家的飞行功能（供管理员命令使用）
     */
    public void forceDisableFlight(Player player) {
        UUID playerId = player.getUniqueId();

        if (flyingPlayers.contains(playerId)) {
            // 禁用飞行
            disableFlight(player);
            player.sendMessage(ChatColor.RED + "管理员已强制关闭你的领地飞行！");
        }
        // 即使不在flyingPlayers中，也确保飞行被关闭
        player.setAllowFlight(false);
        player.setFlying(false);
    }
}
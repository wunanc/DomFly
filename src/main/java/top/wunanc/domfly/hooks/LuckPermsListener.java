package top.wunanc.domfly.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import top.wunanc.domfly.handler.Fly;

import java.util.UUID;

public class LuckPermsListener {

    private final JavaPlugin plugin;
    private final Fly flyHandler;

    public LuckPermsListener(JavaPlugin plugin, Fly flyHandler) {
        this.plugin = plugin;
        this.flyHandler = flyHandler;
    }

    @SuppressWarnings("resource")
    public void register() {
        LuckPerms luckPerms = LuckPermsProvider.get();
        EventBus eventBus = luckPerms.getEventBus();

        eventBus.subscribe(this.plugin, UserDataRecalculateEvent.class, this::onPermissionChange);
    }

    private void onPermissionChange(UserDataRecalculateEvent event) {
        UUID uuid = event.getUser().getUniqueId();
        Player player = Bukkit.getPlayer(uuid);

        if (player != null && flyHandler.isFlying(uuid)) {
            player.getScheduler().execute(plugin, () -> {
                if (!player.hasPermission("domfly.use")) {
                    flyHandler.forceDisableFlight(player, true);
                }
            }, null, 0);
        }
    }
}
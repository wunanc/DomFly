package top.wunanc.domfly.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.wunanc.domfly.DomFly;
import top.wunanc.domfly.handler.Fly;

public class PapiExpansion extends PlaceholderExpansion {

    private final DomFly plugin;
    private final Fly fly;

    public PapiExpansion(DomFly plugin, Fly fly) {
        this.plugin = plugin;
        this.fly = fly;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "domfly";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Wunanc";
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {

        return switch (params.toLowerCase()) {
            case "flying" -> {
                if (player == null) {
                    yield "false";
                }
                yield String.valueOf(fly.isFlying(player.getUniqueId()));
            }
            case "flying_players" -> String.valueOf(fly.getFlyingPlayerCount());
            default -> null;
        };
    }
}
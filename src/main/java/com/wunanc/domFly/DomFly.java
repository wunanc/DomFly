package com.wunanc.DomFly;

import com.wunanc.DomFly.command.CommandRegistry;
import com.wunanc.DomFly.core.Context;
import com.wunanc.DomFly.listener.FlightListener;
import com.wunanc.DomFly.service.*;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Map;

public final class DomFly extends JavaPlugin {

    private Context context;
    private FlightService flightService;

    @Override
    public void onEnable() {
        this.context = new Context(this);
        ConfigManager configManager = new ConfigManager(context);
        LandingService landingService = new LandingService();
        TerritoryService territoryService = new DominionTerritoryService(this, configManager);
        this.flightService = new FlightService(
                context,
                territoryService,
                landingService,
                configManager
        );
        getServer().getPluginManager().registerEvents(new FlightListener(flightService, configManager), this);
        var cmd = getCommand("DomFly");
        if (cmd != null) {
            CommandRegistry registry = new CommandRegistry(context, flightService);
            cmd.setExecutor(registry);
            cmd.setTabCompleter(registry);
        }
        context.log("system.startup", Map.of());
    }
    @Override
    public void onDisable() {
        if (flightService != null) {
            flightService.disableAll();
        }
        context.log("system.shutdown", Map.of());
    }
}

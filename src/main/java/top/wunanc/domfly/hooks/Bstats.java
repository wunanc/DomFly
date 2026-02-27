package top.wunanc.domfly.hooks;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import top.wunanc.domfly.handler.Fly;

public class Bstats {
    private final Metrics metrics;
    private final Fly fly;
    public Bstats(Metrics metrics, Fly fly) {
        this.metrics = metrics;
        this.fly = fly;
    }

    public void FlightTable() {
        metrics.addCustomChart(new SingleLineChart("FlyingPlayers", fly::getFlyingPlayerCount));
    }
}
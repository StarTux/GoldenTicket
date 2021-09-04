package com.cavetale.goldenticket;

import com.cavetale.core.util.Json;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class GoldenTicketPlugin extends JavaPlugin {
    protected GoldenTicketCommand goldenTicketCommand = new GoldenTicketCommand(this);
    protected AdminCommand adminCommand = new AdminCommand(this);
    protected EventListener eventListener = new EventListener(this);
    protected Save save;

    @Override
    public void onEnable() {
        goldenTicketCommand.enable();
        adminCommand.enable();
        eventListener.enable();
        load();
        long delay = 20L * 60L * 10L;
        Bukkit.getScheduler().runTaskTimer(this, () -> {
                for (var player : Bukkit.getOnlinePlayers()) {
                    if (save.members.containsKey(player.getUniqueId())) {
                        goldenTicketCommand.notify(player);
                    }
                }
            }, delay, delay);
    }

    @Override
    public void onDisable() {
    }

    protected void load() {
        File file = new File(getDataFolder(), "save.json");
        save = Json.load(file, Save.class, Save::new);
    }

    protected void save() {
        getDataFolder().mkdirs();
        File file = new File(getDataFolder(), "save.json");
        Json.save(file, save, true);
    }
}

package ru.primer.mc;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("promocode").setExecutor(new Commands());

    }

    @Override
    public void onDisable() {
    }
}

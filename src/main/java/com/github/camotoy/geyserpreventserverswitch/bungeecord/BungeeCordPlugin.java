package com.github.camotoy.geyserpreventserverswitch.bungeecord;

import com.github.camotoy.geyserpreventserverswitch.common.PreventServerSwitch;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Level;

public final class BungeeCordPlugin extends Plugin {

    private PreventServerSwitch preventer;

    @Override
    public void onEnable() {

        this.preventer = new PreventServerSwitch(getDataFolder());

        if (!preventer.getConfig().isUseFloodgate() && getProxy().getPluginManager().getPlugin("Geyser-BungeeCord") == null) {
            getLogger().log(Level.SEVERE, "Geyser-BungeeCord not found! Disabling...");
            onDisable();
            return;
        } else if (preventer.getConfig().isUseFloodgate() && getProxy().getPluginManager().getPlugin("floodgate") == null) {
            getLogger().log(Level.SEVERE, "Floodgate not found! Disabling...");
            onDisable();
            return;
        }

        getProxy().getPluginManager().registerListener(this, new Events(this));
    }

    @Override
    public void onDisable() {
        getProxy().getPluginManager().unregisterListeners(this);
        preventer.disable();
    }

    public PreventServerSwitch getPreventer() {
        return this.preventer;
    }
}

package com.github.camotoy.geyserpreventserverswitch.bungeecord;

import com.github.camotoy.geyserpreventserverswitch.common.DataHandler;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Level;

public final class BungeeCordPreventServerSwitch extends Plugin {

    private DataHandler dataHandler;

    @Override
    public void onEnable() {

        boolean useFloodgate;
        if (getProxy().getPluginManager().getPlugin("floodgate") != null) {
            useFloodgate = true;
        } else if (getProxy().getPluginManager().getPlugin("Geyser-BungeeCord") != null) {
            useFloodgate = false;
        } else {
            getLogger().log(Level.SEVERE, "Floodgate or Geyser not found! Disabling...");
            onDisable();
            return;
        }

        this.dataHandler = new DataHandler(getDataFolder(), useFloodgate);

        getProxy().getPluginManager().registerListener(this, new Events(this));
        getProxy().getPluginManager().registerCommand(this, new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
        getProxy().getPluginManager().unregisterListeners(this);
        dataHandler.disable();
        dataHandler = null;
    }

    public DataHandler getDataHandler() {
        return this.dataHandler;
    }
}

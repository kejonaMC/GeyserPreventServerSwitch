package com.github.camotoy.geyserpreventserverswitch.velocity;

import com.github.camotoy.geyserpreventserverswitch.common.DataHandler;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "geyserpreventserverswitch",
        name = "GeyserPreventServerSwitch",
        version = "1.2-SNAPSHOT",
        description = "Prevent Geyser/Floodgate players from connecting to specific subservers.",
        url = "https://github.com/Camotoy/GeyserPreventServerSwitch",
        authors = {"Camotoy"},
        dependencies = {@Dependency(id = "geyser", optional = true), @Dependency(id = "floodgate", optional = true)})
public class VelocityPreventServerSwitch {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private Logger logger;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    private DataHandler dataHandler;

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {

        boolean useFloodgate;
        if (proxyServer.getPluginManager().isLoaded("floodgate")) {
            useFloodgate = true;
        } else if (proxyServer.getPluginManager().isLoaded("geyser")) {
            useFloodgate = false;
        } else {
            logger.error("Floodgate or Geyser not found! Disabling...");
            shutdown();
            return;
        }

        this.dataHandler = new DataHandler(dataDirectory.toFile(), useFloodgate);

        proxyServer.getEventManager().register(this, new Events(this, logger, proxyServer));
        proxyServer.getCommandManager().register("pssreload", new ReloadCommand(this, logger));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        shutdown();
    }

    public void shutdown() {
        dataHandler.disable();
        dataHandler = null;
    }

    public DataHandler getDataHandler() {
        return this.dataHandler;
    }
}

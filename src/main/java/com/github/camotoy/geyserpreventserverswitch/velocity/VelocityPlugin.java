package com.github.camotoy.geyserpreventserverswitch.velocity;

import com.github.camotoy.geyserpreventserverswitch.common.PreventServerSwitch;
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

@Plugin(id = "GeyserPreventServerSwitch",
        name = "GeyserPreventServerSwitch",
        version = "1.2-SNAPSHOT",
        description = "Prevent Geyser/Floodgate players from connecting to specific subservers.",
        url = "https://github.com/Camotoy/GeyserPreventServerSwitch",
        authors = {"Camotoy"},
        dependencies = {@Dependency(id = "geyser", optional = true), @Dependency(id = "floodgate", optional = true)})
public class VelocityPlugin {

    @Inject
    private ProxyServer server;

    @Inject
    private Logger logger;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    private PreventServerSwitch preventer;

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {

        this.preventer = new PreventServerSwitch(dataDirectory.toFile());

        // todo: make sure at least one of geyser and floodgate are running

        // todo: event logic
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        preventer.disable();
    }

    public PreventServerSwitch getPreventer() {
        return this.preventer;
    }
}

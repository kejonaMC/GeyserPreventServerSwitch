package com.github.camotoy.geyserpreventserverswitch.velocity;

import com.github.camotoy.geyserpreventserverswitch.common.Config;
import com.github.camotoy.geyserpreventserverswitch.common.Utils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.util.List;

public class Events {

    private final VelocityPreventServerSwitch plugin;
    private final Logger logger;
    private final ProxyServer proxyServer;

    public Events(VelocityPreventServerSwitch plugin, Logger logger, ProxyServer proxyServer) {
        this.plugin = plugin;
        this.logger = logger;
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onServerPreConnectEvent(ServerPreConnectEvent event) {
        Player player = event.getPlayer();

        Config config = plugin.getPreventer().getConfig();
        if (!Utils.isBedrockPlayer(player.getUniqueId(), config)) {
            return;
        }

        RegisteredServer target = event.getOriginalServer(); // the original target server, not the source server.
        if (plugin.getPreventer().getProhibitedServers().contains(target.getServerInfo().getName())) {
            if (player.hasPermission("geyserpreventserverswitch.server.bypass") || player.hasPermission("geyserpreventserverswitch.server.bypass." + target.getServerInfo().getName())) {
                return;
            }
            // Send the rejection message if available
            if (config.getMessage() != null && !config.getMessage().equals("")) {
                player.sendMessage(Component.text(config.getMessage()));
            }
            // Get the server they are originating from
            RegisteredServer source = null;
            if (player.getCurrentServer().isPresent()) {
                source = player.getCurrentServer().get().getServer();
            }

            if (source == null) {
                // This is their first time joining - Go to the first fallback server.
                List<String> serverConnectOrder = proxyServer.getConfiguration().getAttemptConnectionOrder();
                if (!serverConnectOrder.isEmpty()) {
                    for (String possibleTargetName : serverConnectOrder) {
                        if (!proxyServer.getServer(possibleTargetName).isPresent()) {
                            logger.error("Server listed in the connection priority list is not registered?: " + possibleTargetName);
                            continue;
                        }
                        RegisteredServer possibleTarget = proxyServer.getServer(possibleTargetName).get();

                        if (!plugin.getPreventer().getProhibitedServers().contains(possibleTargetName) || player.hasPermission("geyserpreventserverswitch.server.bypass." + possibleTargetName)) {
                            event.setResult(ServerPreConnectEvent.ServerResult.allowed(possibleTarget));
                            return;
                        }
                    }
                    player.disconnect(Component.text("Sorry, there are no servers available to you!"));
                } else {
                    // Velocity doesn't seem to define the priorities in the config (at least not in a standalone section), so the priority list should realistically never be empty.
                    event.setResult(ServerPreConnectEvent.ServerResult.denied());
                    throw new IllegalStateException("Failed to get the server priority list!");
                }
            } else {
                // The player is already on a server
                event.setResult(ServerPreConnectEvent.ServerResult.denied());
            }
        }
    }
}

package com.github.camotoy.geyserpreventserverswitch.bungeecord;

import com.github.camotoy.geyserpreventserverswitch.common.Config;
import com.github.camotoy.geyserpreventserverswitch.common.Utils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class Events implements Listener {

    private final BungeeCordPreventServerSwitch plugin;

    protected Events(BungeeCordPreventServerSwitch plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (plugin.getDataHandler().getProhibitedServers().contains(event.getTarget().getName())) {
            if (player.hasPermission("geyserpreventserverswitch.server.bypass") || player.hasPermission("geyserpreventserverswitch.server.bypass." + event.getTarget().getName())) {
                return;
            }
            if (Utils.isBedrockPlayer(player.getUniqueId(), plugin.getDataHandler().isUseFloodgate)) {

                Config config = plugin.getDataHandler().getConfig();
                if (config.getMessage() != null && !config.getMessage().equals("")) {
                    player.sendMessage(new TextComponent(config.getMessage()));
                }

                if (player.getServer() == null) {
                    // This is their first time joining
                    // Try and find an available server in the connection priority list - no reason to iterate through the actual server list because bungeecord doesn't with java players
                    List<String> serverPriorityOrder = player.getPendingConnection().getListener().getServerPriority();
                    if (!serverPriorityOrder.isEmpty()) {
                        for (String possibleTargetName : serverPriorityOrder) {
                            ServerInfo possibleTarget = plugin.getProxy().getServerInfo(possibleTargetName);
                            if (possibleTarget == null) {
                                plugin.getLogger().severe("Server listed in the connection priority list is not registered?: " + possibleTargetName);
                                continue;
                            }

                            if (!plugin.getDataHandler().getProhibitedServers().contains(possibleTargetName) || player.hasPermission("geyserpreventserverswitch.server.bypass." + possibleTargetName)) {
                                event.setTarget(possibleTarget);
                                return;
                            }
                        }
                        event.setCancelled(true);
                        player.disconnect(new TextComponent("Sorry, there are no servers available to you!"));
                    } else {
                        // BungeeCord injects 'lobby' into the order list in the config if the user defines it empty
                        // Any servers in the order list must be listed in the server list - So, this should never happen.
                        throw new IllegalStateException("Server connection priority list was empty!");
                    }
                } else {
                    // The player is already on a server
                    event.setCancelled(true);
                }
            }
        }
    }
}

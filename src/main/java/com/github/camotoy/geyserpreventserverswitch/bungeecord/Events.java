package com.github.camotoy.geyserpreventserverswitch.bungeecord;

import com.github.camotoy.geyserpreventserverswitch.common.Config;
import com.github.camotoy.geyserpreventserverswitch.common.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {

    private final BungeeCordPlugin plugin;

    public Events(BungeeCordPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (plugin.getPreventer().getProhibitedServers().contains(event.getTarget().getName())) {
            if (event.getPlayer().hasPermission("geyserpreventserverswitch.server.bypass") || event.getPlayer().hasPermission("geyserpreventserverswitch.server.bypass." + event.getTarget().getName())) {
                return;
            }
            Config config = plugin.getPreventer().getConfig();
            if (Utils.isBedrockPlayer(event.getPlayer().getUniqueId(), config)) {
                // Send the message if available
                if (config.getMessage() != null && !config.getMessage().equals("")) {
                    event.getPlayer().sendMessage(new TextComponent(config.getMessage()));
                }
                if (event.getPlayer().getServer() == null) {
                    // Go to the first fallback server.
                    if (!event.getPlayer().getPendingConnection().getListener().getServerPriority().isEmpty()) {
                        event.setTarget(plugin.getProxy().getServers().get(event.getPlayer().getPendingConnection().getListener().getServerPriority().get(0)));
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

}

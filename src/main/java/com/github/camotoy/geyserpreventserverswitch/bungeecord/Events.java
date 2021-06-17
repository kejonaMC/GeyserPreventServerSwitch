package com.github.camotoy.geyserpreventserverswitch.bungeecord;

import com.github.camotoy.geyserpreventserverswitch.common.Config;
import com.github.camotoy.geyserpreventserverswitch.common.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {

    private final BungeeCordPreventServerSwitch plugin;

    public Events(BungeeCordPreventServerSwitch plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        Config config = plugin.getDataHandler().getConfig();
        if (!Utils.isBedrockPlayer(event.getPlayer().getUniqueId(), plugin.getDataHandler().isUseFloodgate)) {
            return;
        }

        if (plugin.getDataHandler().getProhibitedServers().contains(event.getTarget().getName())) {
            if (event.getPlayer().hasPermission("geyserpreventserverswitch.server.bypass") || event.getPlayer().hasPermission("geyserpreventserverswitch.server.bypass." + event.getTarget().getName())) {
                return;
            }

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

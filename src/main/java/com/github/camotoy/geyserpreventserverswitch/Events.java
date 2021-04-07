package com.github.camotoy.geyserpreventserverswitch;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {

    private final GeyserPreventServerSwitch plugin;

    public Events(GeyserPreventServerSwitch plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (plugin.getProhibitedServers().contains(event.getTarget().getName())) {
            if (event.getPlayer().hasPermission("geyserpreventserverswitch.server.bypass") || event.getPlayer().hasPermission("geyserpreventserverswitch.server.bypass." + event.getTarget().getName())) {
                return;
            }
            if (plugin.isBedrockPlayer(event.getPlayer().getUniqueId())) {
                if (plugin.getConfig().getMessage() != null && !plugin.getConfig().getMessage().equals("")) {
                    event.getPlayer().sendMessage(new TextComponent(plugin.getConfig().getMessage()));
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

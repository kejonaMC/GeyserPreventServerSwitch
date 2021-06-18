package com.github.camotoy.geyserpreventserverswitch.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

public class ReloadCommand implements SimpleCommand {

    private final VelocityPreventServerSwitch plugin;
    private final Logger logger;

    protected ReloadCommand(VelocityPreventServerSwitch plugin, Logger logger) {
        this.plugin = plugin;
        this.logger = logger;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (source instanceof Player) {
            logger.info(((Player) invocation.source()).getUsername() + " triggered a configuration reload.");
        }
        if (plugin.getDataHandler().loadConfig()) {
            source.sendMessage(Component.text("[GeyserPreventServerSwitch] Reloaded the configuration."));
        } else {
            source.sendMessage(Component.text("[GeyserPreventServerSwitch] Failed to reload the configuration! Check the server console for further info."));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("preventserverswitch.reload");
    }
}

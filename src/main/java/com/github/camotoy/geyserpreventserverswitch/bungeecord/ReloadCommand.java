package com.github.camotoy.geyserpreventserverswitch.bungeecord;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

    private BungeeCordPreventServerSwitch plugin;

    protected ReloadCommand(BungeeCordPreventServerSwitch plugin) {
        super("pssreload", "preventserverswitch.reload");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            plugin.getLogger().info(commandSender.getName() + " triggered a configuration reload.");
        }
        if (plugin.getDataHandler().loadConfig()) {
            commandSender.sendMessage(new TextComponent("[GeyserPreventServerSwitch] Reloaded the configuration."));
        } else {
            commandSender.sendMessage(new TextComponent("[GeyserPreventServerSwitch] Failed to reload the configuration! Check the server console for further info."));
        }
    }
}

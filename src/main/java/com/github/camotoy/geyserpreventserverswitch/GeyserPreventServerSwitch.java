package com.github.camotoy.geyserpreventserverswitch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.md_5.bungee.api.plugin.Plugin;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.GeyserImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public final class GeyserPreventServerSwitch extends Plugin {

    private Config config;

    /**
     * A list of all prohibited servers, in a List.
     */
    private List<String> prohibitedServers;

    @Override
    public void onEnable() {

        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        try {
            if (!getDataFolder().exists())
                getDataFolder().mkdir();
            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                configFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(configFile);
                InputStream input = GeyserPreventServerSwitch.class.getResourceAsStream("/config.yml"); // resources need leading "/" prefix

                byte[] bytes = new byte[input.available()];

                input.read(bytes);

                for(char c : new String(bytes).toCharArray()) {
                    fos.write(c);
                }

                fos.flush();
                input.close();
                fos.close();
            }

            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            config = objectMapper.readValue(configFile, Config.class);
        } catch (IOException ex) {
            getLogger().log(Level.WARNING, "Failed to read config!", ex);
            ex.printStackTrace();
        }

        if (!config.isUseFloodgate() && getProxy().getPluginManager().getPlugin("Geyser-BungeeCord") == null) {
            getLogger().log(Level.SEVERE, "Geyser-BungeeCord not found! Disabling...");
            onDisable();
            return;
        } else if (config.isUseFloodgate() && getProxy().getPluginManager().getPlugin("floodgate") == null) {
            getLogger().log(Level.SEVERE, "Floodgate not found! Disabling...");
            onDisable();
            return;
        }

        prohibitedServers = Arrays.asList(config.getProhibitedServers());

        getProxy().getPluginManager().registerListener(this, new Events(this));
    }

    @Override
    public void onDisable() {
        getProxy().getPluginManager().unregisterListeners(this);
        config = null;
        prohibitedServers = null;
    }

    /**
     * Determines if a player is from Bedrock
     * @param uuid the UUID to determine
     * @return true if the player is from Bedrock
     */
    public boolean isBedrockPlayer(UUID uuid) {
        if (!config.isUseFloodgate()) {
            return GeyserImpl.getInstance().connectionByUuid(uuid) != null;
        } else {
            return FloodgateApi.getInstance().isFloodgatePlayer(uuid);
        }
    }

    public Config getConfig() {
        return this.config;
    }

    public List<String> getProhibitedServers() {
        return this.prohibitedServers;
    }
}

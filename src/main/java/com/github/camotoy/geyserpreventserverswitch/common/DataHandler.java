package com.github.camotoy.geyserpreventserverswitch.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class DataHandler {

    private Config config;

    /**
     * A list of all prohibited servers, in a List.
     */
    private List<String> prohibitedServers;

    public final boolean isUseFloodgate;

    public DataHandler(File dataFolder, boolean isUseFloodgate) {
        this.isUseFloodgate = isUseFloodgate;

        // Load the config
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            File configFile = new File(dataFolder, "config.yml");
            if (!configFile.exists()) {
                configFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(configFile);
                InputStream input = this.getClass().getResourceAsStream("/config.yml"); // resources need leading "/" prefix

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
            System.out.println("[GeyserPreventServerSwitch] Failed to read config!");
            ex.printStackTrace();
        }

        // Get all the prohibited servers
        prohibitedServers = Arrays.asList(config.getProhibitedServers());
    }

    public Config getConfig() {
        return this.config;
    }

    /**
     * @return A list of all prohibited servers, in a List.
     */
    public List<String> getProhibitedServers() {
        return this.prohibitedServers;
    }

    public void disable() {
        this.config = null;
        this.prohibitedServers = null;
    }
 }

package com.github.camotoy.geyserpreventserverswitch.common;

import org.geysermc.connector.GeyserConnector;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class Utils {

    /**
     * Determines if a player is from Bedrock
     * @param uuid the UUID to determine
     * @param config the Config to use
     * @return true if the player is from Bedrock
     */
    public static boolean isBedrockPlayer(UUID uuid, Config config) {
        if (!config.isUseFloodgate()) {
            return GeyserConnector.getInstance().getPlayerByUuid(uuid) != null;
        } else {
            return FloodgateApi.getInstance().isFloodgatePlayer(uuid);
        }
    }
}

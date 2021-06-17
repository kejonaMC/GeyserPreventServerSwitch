package com.github.camotoy.geyserpreventserverswitch.common;

import org.geysermc.connector.GeyserConnector;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class Utils {

    /**
     * Determines if a player is from Bedrock
     * @param uuid the UUID to determine
     * @param useFloodgate whether or not to use the floodgate API. Should be true if Floodgate is installed.
     * @return true if the player is from Bedrock
     */
    public static boolean isBedrockPlayer(UUID uuid, boolean useFloodgate) {
        if (useFloodgate) {
            return FloodgateApi.getInstance().isFloodgatePlayer(uuid);
        } else {
            if (GeyserConnector.getInstance() == null) {
                throw new NullPointerException("Connector instance returned null!");
            }
            return GeyserConnector.getInstance().getPlayerByUuid(uuid) != null;
        }
    }
}

package com.github.camotoy.geyserpreventserverswitch.common;

import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.api.GeyserApi;

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
            return GeyserApi.api().isBedrockPlayer(uuid);
        }
    }
}

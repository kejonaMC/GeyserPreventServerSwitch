package com.github.camotoy.geyserpreventserverswitch.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    @JsonProperty("prohibited-servers")
    private String[] prohibitedServers;

    @JsonProperty("message")
    private String message;

    @JsonProperty("use-floodgate")
    private boolean useFloodgate;

    public String[] getProhibitedServers() {
        return this.prohibitedServers;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isUseFloodgate() {
        return this.useFloodgate;
    }
}

package com.github.doctormacc.geyserpreventserverswitch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    @Getter
    @JsonProperty("prohibited-servers")
    private String[] prohibitedServers;

    @Getter
    @JsonProperty("message")
    private String message;

    @Getter
    @JsonProperty("use-floodgate")
    private boolean useFloodgate;

}

package com.dvcoders.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

/**
 * @author Jake Loo (03 July, 2015)
 */
public class AppConfiguration extends Configuration {
    @JsonProperty("app_name")
    String applicationName;

    @JsonProperty("env")
    String env;

    public String getApplicationName() {
        return applicationName;
    }

    public String getEnv() {
        return env;
    }

}

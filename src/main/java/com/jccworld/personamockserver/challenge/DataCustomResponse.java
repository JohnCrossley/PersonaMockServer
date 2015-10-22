package com.jccworld.personamockserver.challenge;

import com.jccworld.personamockserver.route.config.Config;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class DataCustomResponse implements CustomResponse {
    private final Config config;
    private final String responseBody;

    public DataCustomResponse(final Config config, final String responseBody) {
        this.config = config;
        this.responseBody = responseBody;
    }

    public DataCustomResponse() {
        this.config = null;
        this.responseBody = null;
    }

    public Config getConfig() {
        return config;
    }

    public String getResponseBody() {
        return responseBody;
    }
}

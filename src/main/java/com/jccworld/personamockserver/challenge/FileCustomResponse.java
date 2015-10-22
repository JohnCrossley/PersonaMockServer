package com.jccworld.personamockserver.challenge;

/**
 * https://github.com/JohnCrossley/PersonaMockServer
 */
public class FileCustomResponse implements CustomResponse {
    private final String responseDataFile;
    private final String responseConfigFile;

    public FileCustomResponse(final String responseDataFile, final String responseConfigFile) {
        this.responseDataFile = responseDataFile;
        this.responseConfigFile = responseConfigFile;
    }

    public FileCustomResponse() {
        this.responseDataFile = null;
        this.responseConfigFile = null;
    }

    public String getResponseDataFile() {
        return responseDataFile;
    }

    public String getResponseConfigFile() {
        return responseConfigFile;
    }

}

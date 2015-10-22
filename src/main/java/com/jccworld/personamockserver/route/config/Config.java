package com.jccworld.personamockserver.route.config;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class Config {
    private int status = HttpURLConnection.HTTP_OK;
    private Map<String,String> headers = new HashMap<String, String>();
    private long delayMillis = 0;

    public void setStatus(final int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setHeader(final String key, final String value) {
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setDelayMillis(final long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public long getDelayMillis() {
        return delayMillis;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{status = " + status + ", headers = " + headers + " delayMillis = " + delayMillis + "}";
    }
}

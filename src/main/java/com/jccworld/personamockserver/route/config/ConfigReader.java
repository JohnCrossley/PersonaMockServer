package com.jccworld.personamockserver.route.config;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;
import spark.utils.IOUtils;

import java.io.FileInputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class ConfigReader {
    public static final String HEADERS = "headers";
    public static final String DELAY_MILLIS = "delayMillis";

    private static final String STATUS = "status";

    private final Logger logger = LoggerFactory.getLogger(ConfigReader.class);

    public Config loadConfig(final String file) throws ConfigException {
        String source;
        FileInputStream inputStream = null;
        try {
            try {
                inputStream = new FileInputStream(file);
                source = IOUtils.toString(inputStream);

            } finally {
                inputStream.close();

            }
        } catch (Exception e) {
            throw new ConfigException(e);
        }

        Config config = new Config();

        JSONObject obj = new JSONObject(source);

        if (obj.has(STATUS)) {
            config.setStatus(obj.getInt(STATUS));
        }

        if (obj.has(HEADERS)) {

            JSONArray array = obj.getJSONArray(HEADERS);
            for (int i = 0; i < array.length(); i++) {

                JSONObject headersObj = array.getJSONObject(i);
                if (headersObj.keys() != null) {
                    for (Object o : headersObj.keySet()) {
                        String key = o.toString();
                        String value = headersObj.getString(key);
                        config.setHeader(key, value);
                    }
                }
            }
        }

        if (obj.has(DELAY_MILLIS)) {
            config.setDelayMillis(obj.getLong(DELAY_MILLIS));
        }

        return config;
    }

    public void apply(final Response response, final Config config) throws ConfigException {
        logger.debug("Sending Response:      " + config);
        response.status(config.getStatus());
        for(final String headerKey : config.getHeaders().keySet()) {
            response.header(headerKey, config.getHeaders().get(headerKey));
        }

        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await(config.getDelayMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new ConfigException(e);
        }
    }
}

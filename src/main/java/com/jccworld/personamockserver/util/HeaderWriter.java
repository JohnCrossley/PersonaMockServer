package com.jccworld.personamockserver.util;

import com.jccworld.personamockserver.PersonaDataFilePicker;
import com.jccworld.personamockserver.Settings;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class HeaderWriter {

    /**
     * Delay responding to the client for N milliseconds
     */
    public static final String PMS_DELAY_KEY = "PMS_Delay";

    /**
     * HTTP Status code to send back to the client
     */
    public static final String PMS_STATUS_KEY = "PMS_Status";

    public static void setHeaders(final String file, final String persona, final PersonaDataFilePicker personaDataFilePicker,
                                  final HttpServletResponse response) throws IOException {

        System.out.println("[PMS]   HeaderWriter.setHeaders() file: " + file + " persona: " + persona + " ");
        if (file != null) {
            final Properties properties = new Properties();

            final String absolutePath = personaDataFilePicker.createLocalFilePath(persona, file);
            if (absolutePath == null) {
                throw new FileNotFoundException(file + " does not exist!");
            }

            final InputStream inputStream = new FileInputStream(absolutePath);
            properties.load(inputStream);

            final Enumeration e = properties.propertyNames();
            while (e.hasMoreElements()) {
                final String name = (String) e.nextElement();

                if (name.equals(PMS_DELAY_KEY)) {
                    handleDelay(Integer.parseInt(properties.getProperty(name)));
                } else if (name.equals(PMS_STATUS_KEY)) {
                    response.setStatus(Integer.parseInt(properties.getProperty(name)));
                } else {
                    response.addHeader(name, properties.getProperty(name));
                }
            }
        }
    }

    private static void handleDelay(final int delay) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            System.out.println("[PMS]   delaying response: " + delay + "ms");
            countDownLatch.await(delay, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // NOT FATAL
        }
    }
}

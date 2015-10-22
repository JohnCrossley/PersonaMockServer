package com.jccworld.personamockserver.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;

import javax.servlet.ServletOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.IllegalFormatException;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class OutputWriter {

    private final Logger logger = LoggerFactory.getLogger(OutputWriter.class);

    public void write(final String filePath, final Response response) throws IOException {
        try {
            final FileInputStream inputStream = new FileInputStream(filePath);

            final ServletOutputStream outputStream = response.raw().getOutputStream();

            int c;
            StringBuilder stringBuilder = new StringBuilder();
            while ((c = inputStream.read()) != -1) {
                stringBuilder.append((char) c);
                outputStream.write(c);
            }

            logger.trace("Sending Response Body: " + stringBuilder.toString());

            outputStream.close();

        } catch(IOException e) {
            throw new IOException("Failed to write response: " + e.getMessage(), e);
        }
    }

    public void writeString(final String content, final Response response) throws IOException {
        logger.trace("Sending Response Body: " + content);

        try {
            response.raw().getOutputStream().print(content);
        } catch (IOException e) {
            throw new IOException("Failed to write response: " + e.getMessage(), e);
        }
    }
}

package com.jccworld.personamockserver.responder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.externalStaticFileLocation;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class CatchAll extends Route {
    public static final String PATH = "/*";
    private static final Object CONTROL_TO_JETTY = null;

    private final Logger logger = LoggerFactory.getLogger(CatchAll.class);
    private final List<String> ignoredPaths;
    private final String baseUrl;

    public CatchAll(final String baseUrl) {
        super(PATH);
        this.baseUrl = baseUrl;
        this.ignoredPaths = new ArrayList<>(0);
    }

    public CatchAll(final String baseUrl, final List<String> ignoredPaths) {
        super(PATH);
        this.baseUrl = baseUrl;
        this.ignoredPaths = ignoredPaths;
    }

    @Override
    public Object handle(final Request request, final Response response) {
        String url = baseUrl + request.pathInfo();

        for(final String mappedUrl : ignoredPaths) {
            if (url.contains(mappedUrl)) {
                logger.debug("Returning mapped content for: " + url);
                return CONTROL_TO_JETTY;
            }
        }

        logger.info("Failed to find explicit mapping for url: ``" + url + "''");
        logger.info("Payload: " + request.body());

        return "<html>\n" +
                "  <body>\n" +
                "    <pre>Persona Mock Server doesn't support this URL: " + url + " !</pre>\n" +
                "  </body>\n" +
                "</html>";
    }
}

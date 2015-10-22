package com.jccworld.personamockserver;

import com.jccworld.personamockserver.io.OutputWriter;
import com.jccworld.personamockserver.responder.RouteResponder;
import com.jccworld.personamockserver.route.persona.LoginTest;
import com.jccworld.personamockserver.route.persona.PersonaSessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class RouteWrapper extends Route {
    private final static String NOTHING = "";

    private final Logger logger = LoggerFactory.getLogger(RouteWrapper.class);
    private final LoginTest loginTest;
    private final PersonaSessionHandler personaSessionHandler;
    private final RouteResponder routeResponder;
    private final OutputWriter outputWriter;

    public RouteWrapper(final LoginTest loginTest,
                        final PersonaSessionHandler personaSessionHandler,
                        final RouteResponder routeResponder,
                        final OutputWriter outputWriter) {
        super(routeResponder.getHttpPath());
        this.loginTest = loginTest;
        this.personaSessionHandler = personaSessionHandler;
        this.routeResponder = routeResponder;
        this.outputWriter = outputWriter;
    }

    @Override
    public Object handle(final Request request, final Response response) {
        logger.info("REQUEST --------------------------------------------------------------------------------------------");
        logger.info("Url:                   " + request.url());

        try {
            loginTest.handleLoginRequest(personaSessionHandler, request);
            loginTest.handleLogoutRequest(personaSessionHandler, request);

            routeResponder.run(request, response);

        } catch(Exception e) {
            try {
                //attempt to send error as response
                response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
                outputWriter.writeString(request.url() + " - " + e.getMessage(), response);
            } catch (IOException ioe) {
                logger.error("FATAL: " + ioe.getMessage(), ioe);
            }
        }

        logger.info("----------------------------------------------------------------------------------------------------");

        return NOTHING;
    }
}

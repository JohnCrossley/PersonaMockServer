package com.jccworld.personamockserver.responder;

import com.jccworld.personamockserver.io.OutputWriter;
import com.jccworld.personamockserver.ReadException;
import com.jccworld.personamockserver.RouteException;
import com.jccworld.personamockserver.route.config.Config;
import com.jccworld.personamockserver.route.config.ConfigException;
import com.jccworld.personamockserver.route.config.ConfigReader;
import com.jccworld.personamockserver.route.persona.Persona;
import com.jccworld.personamockserver.route.persona.PersonaReader;
import com.jccworld.personamockserver.route.persona.PersonaSessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class SimpleRouteResponder implements RouteResponder {
    private final Logger logger = LoggerFactory.getLogger(SimpleRouteResponder.class);
    private final String httpPath;
    private final String responseDataFile;
    private final String responseConfigFile;
    private final ConfigReader configReader;
    private final OutputWriter outputWriter;
    private final PersonaReader personaReader;
    private final PersonaSessionHandler personaSessionHandler;

    /**
     * Respond to a httpPath with responseDataFile with responseDataConfig settings.
     * @param httpPath
     * @param responseDataFile
     * @param responseConfigFile
     * @param configReader
     * @param outputWriter
     * @param personaReader
     * @param personaSessionHandler
     */
    public SimpleRouteResponder(final String httpPath,
                                final String responseDataFile,
                                final String responseConfigFile,
                                final ConfigReader configReader,
                                final OutputWriter outputWriter,
                                final PersonaReader personaReader,
                                final PersonaSessionHandler personaSessionHandler) {
        this.httpPath = httpPath;
        this.responseDataFile = responseDataFile;
        this.responseConfigFile = responseConfigFile;
        this.configReader = configReader;
        this.outputWriter = outputWriter;
        this.personaReader = personaReader;
        this.personaSessionHandler = personaSessionHandler;
    }

    public String getHttpPath() {
        return httpPath;
    }

    @Override
    public void run(final Request request, final Response response) throws RouteException {
        final Persona persona = new Persona(personaSessionHandler, request);
        debug(persona);

        final String filePath;
        try {
            filePath = (responseDataFile != null) ? personaReader.resolveFilePath(persona, responseDataFile) : null;

            if (responseConfigFile != null) {
                final String configFilePath;
                configFilePath = personaReader.resolveFilePath(persona, responseConfigFile);

                Config config = configReader.loadConfig(configFilePath);

                configReader.apply(response, config);
            }

            if (responseDataFile != null) {
                outputWriter.write(filePath, response);
            }

        } catch (Exception e) {
            throw new RouteException(e);
        }
    }

    private void debug(final Persona persona) {
        logger.debug("httpPath:              " + httpPath);
        logger.debug("responseDataFile:      " + responseDataFile);
        logger.debug("responseConfigFile:    " + responseConfigFile);
        logger.debug("persona:               " + persona);
    }
}

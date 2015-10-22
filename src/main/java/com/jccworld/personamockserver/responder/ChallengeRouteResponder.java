package com.jccworld.personamockserver.responder;

import com.jccworld.personamockserver.io.OutputWriter;
import com.jccworld.personamockserver.ReadException;
import com.jccworld.personamockserver.RouteException;
import com.jccworld.personamockserver.challenge.Challenge;
import com.jccworld.personamockserver.challenge.CustomResponse;
import com.jccworld.personamockserver.challenge.DataCustomResponse;
import com.jccworld.personamockserver.challenge.FileCustomResponse;
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
public class ChallengeRouteResponder implements RouteResponder {
    private final Logger logger = LoggerFactory.getLogger(ChallengeRouteResponder.class);

    private final String httpPath;
    private final Challenge challenge;
    private final ConfigReader configReader;
    private final OutputWriter outputWriter;
    private final PersonaReader personaReader;
    private final PersonaSessionHandler personaSessionHandler;

    public ChallengeRouteResponder(final String httpPath,
                                   final Challenge challenge,
                                   final ConfigReader configReader,
                                   final OutputWriter outputWriter,
                                   final PersonaReader personaReader,
                                   final PersonaSessionHandler personaSessionHandler) {
        this.httpPath = httpPath;
        this.challenge = challenge;
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
        CustomResponse customResponse = getCustomResponse(request, response);
        checkInstance(customResponse);

        debug(customResponse, persona);

        try {
            Config config = getConfig(customResponse, persona);

            if (customResponse instanceof FileCustomResponse) {
                final String responseDataFile = ((FileCustomResponse) customResponse).getResponseDataFile();

                String filePath = (responseDataFile != null) ? personaReader.resolveFilePath(persona, responseDataFile) : null;

                configReader.apply(response, config);

                outputWriter.write(filePath, response);

            } else if (customResponse instanceof DataCustomResponse) {
                final String content = ((DataCustomResponse) customResponse).getResponseBody();

                configReader.apply(response, config);

                outputWriter.writeString(content, response);
            }

        } catch (Exception e) {
            throw new RouteException(e);
        }
    }

    private void debug(final CustomResponse customResponse, final Persona persona) {
        logger.debug("httpPath:              " + httpPath);
        logger.debug("persona:               " + persona);

        if (customResponse instanceof FileCustomResponse) {
            logger.debug("responseDataFile:      " + ((FileCustomResponse) customResponse).getResponseDataFile());
            logger.debug("responseConfigFile:    " + ((FileCustomResponse) customResponse).getResponseConfigFile());

        } else if (customResponse instanceof DataCustomResponse){
            logger.debug("responseData:          { raw data }");
            logger.debug("responseConfig:        { raw data }");
        }
    }

    public CustomResponse getCustomResponse(final Request request, final Response response) throws RouteException {
        CustomResponse customResponse;
        try {
            customResponse = challenge.perform(request, response);
        } catch (Exception e) {
            throw new RouteException("Challenge: ''" + challenge.getClass().getSimpleName() + "'' has encountered an error: " + e.getMessage(), e);
        }
        return customResponse;
    }

    public Config getConfig(final CustomResponse customResponse, final Persona persona) throws RouteException, ReadException, ConfigException {
        if (customResponse instanceof FileCustomResponse) {

            String configFile = ((FileCustomResponse) customResponse).getResponseConfigFile();
            if (configFile == null) {
                return new Config();

            } else {
                String filePath = personaReader.resolveFilePath(persona, configFile);

                return configReader.loadConfig(filePath);
            }

        } else if (customResponse instanceof DataCustomResponse) {
            return ((DataCustomResponse) customResponse).getConfig();
        }

        throw new RuntimeException("Not reachable");//@see checkInstance
    }

    private void checkInstance(final CustomResponse customResponse) throws RouteException {
        if (!(customResponse instanceof FileCustomResponse) && !(customResponse instanceof DataCustomResponse)) {
            if (customResponse == null) {
                throw new RouteException("Unsupported CustomResponse: instance is null");
            } else {
                throw new RouteException("Unsupported CustomResponse instance: " + customResponse.getClass());
            }
        }
    }
}

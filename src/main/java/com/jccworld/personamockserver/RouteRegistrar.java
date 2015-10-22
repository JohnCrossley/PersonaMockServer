package com.jccworld.personamockserver;

import com.jccworld.personamockserver.challenge.Challenge;
import com.jccworld.personamockserver.io.OutputWriter;
import com.jccworld.personamockserver.responder.CatchAll;
import com.jccworld.personamockserver.responder.ChallengeRouteResponder;
import com.jccworld.personamockserver.responder.SimpleRouteResponder;
import com.jccworld.personamockserver.route.config.ConfigReader;
import com.jccworld.personamockserver.route.persona.LoginTest;
import com.jccworld.personamockserver.route.persona.PersonaReader;
import com.jccworld.personamockserver.route.persona.PersonaSessionHandler;

import java.util.List;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class RouteRegistrar {

    private final ConfigReader configReader;
    private final OutputWriter outputWriter;
    private final PersonaReader personaReader;
    private final PersonaSessionHandler personaSessionHandler;
    private final LoginTest loginTest;

    public RouteRegistrar(final ConfigReader configReader,
                          final OutputWriter outputWriter,
                          final PersonaReader personaReader,
                          final PersonaSessionHandler personaSessionHandler,
                          final LoginTest loginTest) {
        if (configReader == null) {
            throw new IllegalArgumentException("configReader cannot be null");
        }
        if (outputWriter == null) {
            throw new IllegalArgumentException("outputWriter cannot be null");
        }
        if (personaReader == null) {
            throw new IllegalArgumentException("personaReader cannot be null");
        }
        if (personaSessionHandler == null) {
            throw new IllegalArgumentException("personaSessionHandler cannot be null");
        }
        if (loginTest == null) {
            throw new IllegalArgumentException("loginTest cannot be null");
        }

        this.configReader = configReader;
        this.outputWriter = outputWriter;
        this.personaReader = personaReader;
        this.personaSessionHandler = personaSessionHandler;
        this.loginTest = loginTest;
    }

    public void registerGet(final String httpPath,
                            final String responseDataFile,
                            final String responseConfigFile) {
        get(new RouteWrapper(loginTest, personaSessionHandler, new SimpleRouteResponder(httpPath, responseDataFile, responseConfigFile, configReader, outputWriter, personaReader, personaSessionHandler), outputWriter));
    }

    public void registerGet(final String httpPath,
                            final Challenge challenge) {
        get(new RouteWrapper(loginTest, personaSessionHandler, new ChallengeRouteResponder(httpPath, challenge, configReader, outputWriter, personaReader, personaSessionHandler), outputWriter));
    }

    public void registerPost(final String httpPath,
                             final String responseDataFile,
                             final String responseConfigFile) {
        post(new RouteWrapper(loginTest, personaSessionHandler, new SimpleRouteResponder(httpPath, responseDataFile, responseConfigFile, configReader, outputWriter, personaReader, personaSessionHandler), outputWriter));
    }

    public void registerPost(final String httpPath,
                             final Challenge challenge) {
        post(new RouteWrapper(loginTest, personaSessionHandler, new ChallengeRouteResponder(httpPath, challenge, configReader, outputWriter, personaReader, personaSessionHandler), outputWriter));
    }

    public void registerCatchAll(final String baseUrl) {
        get(new CatchAll(baseUrl));
        post(new CatchAll(baseUrl));
    }

    public void registerExternalStaticFileLocation(final String path) {
        externalStaticFileLocation(path);
    }

    public void registerCatchAll(final String baseUrl,
                                 final List<String> ignoredPaths) {
        get(new CatchAll(baseUrl, ignoredPaths));
        post(new CatchAll(baseUrl, ignoredPaths));
    }
}

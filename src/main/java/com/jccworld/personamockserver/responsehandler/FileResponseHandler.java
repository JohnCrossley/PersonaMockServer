package com.jccworld.personamockserver.responsehandler;

import com.jccworld.personamockserver.PersonaDataFilePicker;
import com.jccworld.personamockserver.Route;
import com.jccworld.personamockserver.persona.PersonaExtractor;
import com.jccworld.personamockserver.util.HeaderWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The default responder.  Looks for <code>response_headers</code> and <code>response_body</code> in the route
 * configuration file and streams them back to the user.
 */
public class FileResponseHandler implements ResponseHandler {
    public static final String RESPONSE_HEADERS_KEY = "response_headers";
    public static final String RESPONSE_BODY_KEY = "response_body";

    @Override
    public void dispatch(final PersonaDataFilePicker personaDataFilePicker, final PersonaExtractor personaExtractor, final Route route,
                         final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String headerFile = route.getProperties().getProperty(RESPONSE_HEADERS_KEY);
        final String bodyFile = route.getProperties().getProperty(RESPONSE_BODY_KEY);
        final String persona = personaExtractor.extract(route, request);

        HeaderWriter.setHeaders(headerFile, persona, personaDataFilePicker, response);

        System.out.println("[PMS]   FileResponseHandler.dispatch() writing response...");

        final String absolutePath = personaDataFilePicker.createLocalFilePath(persona, bodyFile);
        if (absolutePath == null) {
            throw new FileNotFoundException(bodyFile + " does not exist!");
        }

        final Path path = FileSystems.getDefault().getPath(absolutePath);
        Files.copy(path, response.getOutputStream());
    }
}

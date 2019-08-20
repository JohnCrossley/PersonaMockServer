package com.jccworld.personamockserver.responsehandler;

import com.jccworld.personamockserver.PersonaDataFilePicker;
import com.jccworld.personamockserver.Route;
import com.jccworld.personamockserver.persona.Persona;
import com.jccworld.personamockserver.util.HeaderWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileResponseHandler implements ResponseHandler {
    public static final String RESPONSE_HEADERS = "response_headers";
    public static final String RESPONSE_BODY = "response_body";

    @Override
    public void dispatch(final HttpServletRequest request, final HttpServletResponse response, final Persona persona, final Route route) throws IOException {
        final String headerFile = route.getProperties().getProperty(RESPONSE_HEADERS);
        final String bodyFile = route.getProperties().getProperty(RESPONSE_BODY);

        final PersonaDataFilePicker personaDataFilePicker = new PersonaDataFilePicker();

        HeaderWriter.setHeaders(headerFile, persona, personaDataFilePicker, request, response);

        final Path path = FileSystems.getDefault().getPath(personaDataFilePicker.createLocalFilePath(request, persona.extract(request), bodyFile));
        Files.copy(path, response.getOutputStream());
    }
}

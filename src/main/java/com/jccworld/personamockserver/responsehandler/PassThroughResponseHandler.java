package com.jccworld.personamockserver.responsehandler;

import com.jccworld.personamockserver.PersonaDataFilePicker;
import com.jccworld.personamockserver.Route;
import com.jccworld.personamockserver.persona.PersonaExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Passes the request through the HTTP stack ignoring the {@link com.jccworld.personamockserver.RouteFilter}.
 * Use this if you want to use the path for a custom WebSocket/Servlet service, or you want the resource to be directly
 * returned by the container.  I.e. a file.
 */
public class PassThroughResponseHandler implements ResponseHandler {
    @Override
    public void dispatch(final PersonaDataFilePicker personaDataFilePicker, final PersonaExtractor personaExtractor, final Route route,
                         final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        // NO-OP
    }
}

package com.jccworld.personamockserver.responsehandler;

import com.jccworld.personamockserver.PersonaDataFilePicker;
import com.jccworld.personamockserver.Route;
import com.jccworld.personamockserver.persona.PersonaExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * Implement {@link ResponseHandler} for custom responses to {@link Route} requests.
 * I.e. searching the request body for tokens and responding with different responses based upon those tokens.
 */
public interface ResponseHandler {

    void dispatch(final PersonaDataFilePicker personaDataFilePicker,
                  final PersonaExtractor personaExtractor,
                  final Route route,
                  final HttpServletRequest request,
                  final HttpServletResponse response)
            throws IOException;

}

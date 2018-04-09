package com.jccworld.personmockserver.responsehandler;

import com.jccworld.personmockserver.Route;
import com.jccworld.personmockserver.persona.Persona;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ResponseHandler {

    void dispatch(final HttpServletRequest request,
                  final HttpServletResponse response,
                  final Persona persona,
                  final Route route)
            throws IOException;

}

package com.jccworld.personamockserver.responsehandler;

import com.jccworld.personamockserver.Route;
import com.jccworld.personamockserver.persona.Persona;

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

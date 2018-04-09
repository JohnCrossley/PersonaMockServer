package com.jccworld.personmockserver.persona;

import javax.servlet.http.HttpServletRequest;

public interface Persona {

    Persona NO_OP = httpServletRequest -> {
        //NO-OP
        return null;
    };

    /**
     * @param httpServletRequest
     * @return persona or null if none set
     */
    String extract(HttpServletRequest httpServletRequest);

}


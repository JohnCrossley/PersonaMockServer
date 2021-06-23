package com.jccworld.personamockserver.persona;

import com.jccworld.personamockserver.Route;

import javax.servlet.http.HttpServletRequest;

/**
 * Override this class and set the FQN to <code>persona_extractor_class</code> in <code>web.xml</code>.
 */
public interface PersonaExtractor {

    /**
     *
     * @param route
     * @param httpServletRequest
     * @return persona or null if none set.  The persona maps to a folder containing all the endpoint data located in
     * <code>WEB-INF/personas</code> or user chosen personas path (specified in web.xml using <code>data_root</code>).
     */
    String extract(final Route route, final HttpServletRequest httpServletRequest);

    PersonaExtractor NO_OP = (route, httpServletRequest) -> null;

}


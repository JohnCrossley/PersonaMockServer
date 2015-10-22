package com.jccworld.personamockserver.route.persona;

import spark.Session;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class PersonaSessionHandler {

    private String loggedInPersona;

    public void login(final Session session, final String persona) {
        loggedInPersona = persona;
        session.attribute(Persona.PERSONA_PARAM, persona);
    }

    public void logout(final Session session) {
        loggedInPersona = null;
        session.removeAttribute(Persona.PERSONA_PARAM);
    }

    public String getLoggedInPersona() {
        return loggedInPersona;
    }

    public boolean isLoggedIn(final Session session) {
        return session.attribute(Persona.PERSONA_PARAM) != null;
    }

}

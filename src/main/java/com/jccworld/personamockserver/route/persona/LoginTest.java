package com.jccworld.personamockserver.route.persona;

import spark.Request;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public interface LoginTest {

    void handleLoginRequest(final PersonaSessionHandler personaSessionHandler,
                            final Request request);

    void handleLogoutRequest(final PersonaSessionHandler personaSessionHandler,
                             final Request request);

    LoginTest NO_OP = new LoginTest() {
        @Override
        public void handleLoginRequest(final PersonaSessionHandler personaSessionHandler, final Request request) {
            //NO-OP
        }

        @Override
        public void handleLogoutRequest(final PersonaSessionHandler personaSessionHandler, final Request request) {
            //NO-OP
        }
    };

}

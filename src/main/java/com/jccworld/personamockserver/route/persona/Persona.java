package com.jccworld.personamockserver.route.persona;

import spark.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 *
 * Order for checking persona.  Note, if at any point a persona is resolved, it will not search further.  I.e. POST overrides header.
 *
 * 0. Try POST value.  If more than one, work back from last specified to first.
 * 1. Try GET value.  If more than one, work back from last specified to first.
 * 2. Try Header value.
 * 3. Try Session cookie
 * 4. Try "default"
 * 5. Fail and complain loudly
 */
public class Persona {

    public static final String PERSONA_PARAM = "persona";
    public static final String DEFAULT_PERSONA = "default";
    public static final String EMPTY = "{empty}";

    private String session;
    private String header;
    private List<String> getPost = new ArrayList<>();

    public Persona(final PersonaSessionHandler personaSessionHandler, final Request request) {
        if (personaSessionHandler.isLoggedIn(request.session())) {
            session = personaSessionHandler.getLoggedInPersona();
        }

        if (request.headers(PERSONA_PARAM) != null) {
            header = request.headers(PERSONA_PARAM);
        }

        //spark queryParams is broken: NPE at spark.QueryParamsMap.values(QueryParamsMap.java:224)
        final Map<String, String[]> parameterMap = request.raw().getParameterMap();
        if (parameterMap.containsKey(PERSONA_PARAM)) {
            String[] values = parameterMap.get(PERSONA_PARAM);
            for(String s : values) {
                getPost.add(0, s);
            }
        }
    }

    public String[] list() {
        final List<String> list = new ArrayList<>();

        list.addAll(getPost);
        if (header != null) {
            list.add(header);
        }
        if (session != null) {
            list.add(session);
        }
        list.add(DEFAULT_PERSONA);

        return list.toArray(new String[list.size()]);
    }

    @Override
    public String toString() {
        StringBuilder getPostBuilder = new StringBuilder();

        if (!getPost.isEmpty()) {
            for(int i = 0; i < getPost.size(); i++) {
                getPostBuilder.append(getPost.get(i) + (i < getPost.size() - 1 ? ", " : ""));
            }
        }

        return  getClass().getSimpleName() + "{" +
                "get/post: " + (getPost.size() == 0 ? EMPTY : getPostBuilder) + ", " +
                "header: "   + (header == null ? EMPTY : header) + ", " +
                "session: "  + (session == null ? EMPTY : session) +
                "}";
    }
}

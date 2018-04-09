package com.jccworld.personmockserver;

import com.jccworld.personmockserver.persona.Persona;
import com.jccworld.personmockserver.responsehandler.FileResponseHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class RouteFilter implements Filter {
    private static final String PATH_REGEX = "path_regex";
    private static final String HANDLER_CLASS = "handler_class";
    private static final String DEFAULT_HANDLER_CLASS = FileResponseHandler.class.getName();
    private static final String DELAY = "delay";

    private static final String DEFINITIONS_FOLDER = "definitions";

    private Persona persona;

    final List<Route> routes = new ArrayList<>();

    public void init(final FilterConfig config) throws ServletException {
        final String definitionsPath = config.getServletContext().getRealPath(DEFINITIONS_FOLDER);
        final File definitions = new File(definitionsPath);

        final String[] configFiles = definitions.list();
        for(String configFile : configFiles) {
            try {
                routes.add(parse(new File(definitionsPath + File.separator + configFile)));
                } catch (Exception e) {
                System.out.println("[JCC] FAIL:      " + e.getMessage());
                e.printStackTrace();
            }
        }

        final String personaClassName = config.getInitParameter("persona_class");
        if (personaClassName == null) {
            System.out.println("[JCC]   Without a persona_class configured, the ResponseHandlers will need to handle persona state.");
            persona = Persona.NO_OP;
        } else {
            try {
                Class<Persona> personaClass = (Class<Persona>) Class.forName(personaClassName);
                persona = personaClass.newInstance();
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }

        System.out.println("[JCC]   SERVER READY! ***********************************************");
    }

    private Route parse(final File file) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        final InputStream inputStream = new FileInputStream(file);

        final Properties properties = new Properties();
        properties.load(inputStream);
        inputStream.close();

        final String pathRegex = properties.getProperty(PATH_REGEX);
        final String responseHandlerClassName = properties.getProperty(HANDLER_CLASS, DEFAULT_HANDLER_CLASS);

        final Route route = Route.Factory.create(pathRegex, properties, responseHandlerClassName);

        System.out.println("[JCC] Loaded route: " + route.getPath() + " / " + route.getResponseHandler().getClass().getName());

        return route;
    }

    public void destroy() {
    }

    public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain) throws ServletException, IOException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) resp;
        final String uri = request.getRequestURI();

        //support for METHOD=ALL|GET|POST|PUT|HEAD

        System.out.println("[JCC] NEW REQUEST: *******************************************************************************");
        System.out.println("[JCC]   URI: " + uri + " method: " + ((HttpServletRequest) req).getMethod());

        final List<Route> routes = getRoutesForUri(uri);
        if (routes.size() == 0) {
            System.out.println("[JCC]   no routes found for: " + uri);
            resp.getWriter().write("no routes found for: " + uri);

        } else if (routes.size() > 1) {
            System.out.println("[JCC] found too many matches: " + uri + " count: " + routes.size());
            resp.getWriter().write("found too many routes: " + routes.size());

        } else {
            final Route route = Route.Factory.clone(routes.get(0));

            handleDelay(route);

            System.out.println("[JCC]   found one starting point " + route.getPath().toString());
            route.getResponseHandler().dispatch(request, response, persona, route);
        }
    }

    private void handleDelay(final Route route) throws ServletException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            String delayString = route.getProperties().getProperty(DELAY);
            if (delayString != null) {
                System.out.println("[JCC]   delay: " + delayString);
                int delay = Integer.parseInt(delayString);
                countDownLatch.await(delay, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }

    private List<Route> getRoutesForUri(String uri) {
        final List<Route> matches = new ArrayList<>();
        for (Route route : routes) {
            if (route.getPath().matcher(uri).find()) {
                matches.add(route);
            }
        }

        System.out.println("[JCC] Routes matching -------------------------------------------------------------------");
        for(final Route route : matches) {
            System.out.println("[JCC] --> " + route.getPath());
        }
        System.out.println("[JCC] -----------------------------------------------------------------------------------");

        return matches;
    }
}

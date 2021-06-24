package com.jccworld.personamockserver;

import com.jccworld.personamockserver.persona.PersonaExtractor;
import com.jccworld.personamockserver.responsehandler.PassThroughResponseHandler;

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

import static com.jccworld.personamockserver.Route.*;

@SuppressWarnings("unused")
public class RouteFilter implements Filter {
    public static final String INIT_PARAM_PERSONA_EXTRACTOR_CLASS = "persona_extractor_class";
    public static final String INIT_PARAM_DATA_ROOT = "data_root";

    private PersonaExtractor personaExtractor;
    private static PersonaDataFilePicker personaDataFilePicker;
    private static Settings settings = null;

    private final List<Route> routes = new ArrayList<>();

    public void init(final FilterConfig config) throws ServletException {
        System.out.println("[PMS] SERVER IS STARTING............................................................................");
        settings = new Settings(config.getInitParameter(INIT_PARAM_DATA_ROOT));
        personaDataFilePicker = new PersonaDataFilePicker(settings);

        final File definitions = new File(settings.definitionsRoot);

        final String[] configFiles = definitions.list();
        System.out.println("[PMS] Loading routes (" + (configFiles == null ? 0 : configFiles.length) + ")");

        if (configFiles != null) {
            for (final String configFile : configFiles) {
                try {
                    final Route route = parse(new File(settings.definitionsRoot + File.separator + configFile));
                    routes.add(route);
                    System.out.println("[PMS]    Loaded route: " + route.getPath());
                } catch (Exception e) {
                    System.out.println("[PMS] ** Failed route: " + configFile +  "  with: " + e + " **");
                    e.printStackTrace(System.err);
                }
            }
        }

        final String personaClassName = config.getInitParameter(INIT_PARAM_PERSONA_EXTRACTOR_CLASS);
        if (personaClassName == null) {
            System.out.println("[PMS] ** Without a " + INIT_PARAM_PERSONA_EXTRACTOR_CLASS + " configured, the ResponseHandlers will need to handle persona state. **");
            personaExtractor = PersonaExtractor.NO_OP;
        } else {
            try {
                Class<PersonaExtractor> personaClass = (Class<PersonaExtractor>) Class.forName(personaClassName);
                personaExtractor = personaClass.newInstance();
            } catch (Exception e) {
                System.out.println("[PMS] ** Cannot use " + INIT_PARAM_PERSONA_EXTRACTOR_CLASS + " because: " + e + " **");
                throw new ServletException(e);
            }
        }

        System.out.println("[PMS] SERVER READY! ********************************************************************************");
    }

    private Route parse(final File file) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        final InputStream inputStream = new FileInputStream(file);

        final Properties properties = new Properties();
        properties.load(inputStream);
        inputStream.close();

        final String pathRegex = properties.getProperty(PATH_REGEX);
        final String responseHandlerClassName = properties.getProperty(HANDLER_CLASS, DEFAULT_HANDLER_CLASS);

        if (pathRegex == null) {
            throw new IllegalArgumentException("Route `path` is null");
        }

        final Route route = Route.Factory.create(file.getPath(), pathRegex, properties, responseHandlerClassName);

        return route;
    }

    public void destroy() {
        System.out.println("[PMS] SERVER SHUTDOWN! *****************************************************************************");
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain) throws ServletException, IOException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) resp;
        final String uri = request.getRequestURI();

        //support for METHOD=ALL|GET|POST|PUT|HEAD

        System.out.println("[PMS] NEW REQUEST: *******************************************************************************");
        System.out.println("[PMS]   URI: " + uri + " method: " + ((HttpServletRequest) req).getMethod());

        try {
            final List<Route> routes = getRoutesForUri(uri);
            if (routes.size() == 0) {
                System.out.println("[PMS]   no routes found for: " + uri);

                response.setStatus(500);
                response.getWriter().write("no routes found for: " + uri);

            } else if (routes.size() > 1) {
                System.out.println("[PMS]   found too many matches: " + uri + " (" + routes.size() + ")");

                response.setStatus(500);
                response.getWriter().write("found too many matches: " + uri + " (" + routes.size() + ")");
            } else {
                //1 matching route
                final Route route = Factory.clone(routes.get(0));
                System.out.println("[PMS]   found one starting point: " + route.getPath().toString());

                if (isPassThrough(route)) {
                    System.out.println("[PMS]   passing through to: " + route.getResponseHandler());

                    chain.doFilter(request, response);
                } else {
                    System.out.println("[PMS]   standard route using response handler: " + route.getResponseHandler());

                    route.getResponseHandler().dispatch(personaDataFilePicker, personaExtractor, route, request, response);
                }
            }
        } catch (Exception e) {
            System.out.println("[PMS] ** FATAL: " + e);

            response.setStatus(500);
            response.getWriter().write("FATAL: " + e);

            e.printStackTrace(System.err);
        }

        System.out.println("[PMS] END REQUEST  *******************************************************************************");
    }

    private boolean isPassThrough(final Route route) {
        return route.getResponseHandler().getClass() == PassThroughResponseHandler.class;
    }

    private List<Route> getRoutesForUri(final String uri) {
        final List<Route> matches = new ArrayList<>();
        for (Route route : routes) {
            if (route.getPath().matcher(uri).find()) {
                matches.add(route);
            }
        }

        System.out.println("[PMS] Routes matching -------------------------------------------------------------------");
        for (final Route route : matches) {
            System.out.println("[PMS] --> " + route.getPath() + "    (" + route.getFilename() + ")");
        }
        System.out.println("[PMS] -----------------------------------------------------------------------------------");

        return matches;
    }

    public static Settings getSettings() {
        return settings;
    }

    public static PersonaDataFilePicker getPersonaDataFilePicker() {
        return personaDataFilePicker;
    }
}

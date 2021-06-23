package com.jccworld.personamockserver;

import com.jccworld.personamockserver.responsehandler.FileResponseHandler;
import com.jccworld.personamockserver.responsehandler.ResponseHandler;

import java.util.Properties;
import java.util.regex.Pattern;

public class Route {
    public static final String PATH_REGEX = "path_regex";
    public static final String HANDLER_CLASS = "handler_class";
    public static final String DEFAULT_HANDLER_CLASS = FileResponseHandler.class.getName();

    private final String filename;
    private final Pattern path;
    private final Properties properties;
    private final ResponseHandler responseHandler;

    private Route(final String filename, final Pattern path, final Properties properties, final ResponseHandler responseHandler) {
        this.filename = filename;
        this.path = path;
        this.properties = properties;
        this.responseHandler = responseHandler;
    }

    public String getFilename() {
        return filename;
    }

    public Pattern getPath() {
        return path;
    }

    public Properties getProperties() {
        return properties;
    }

    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }

    static class Factory {
        static Route create(final String filename, final String pathPattern, final Properties properties, final String responseHandlerClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
            return new Route(filename, Pattern.compile(pathPattern), properties, create(responseHandlerClassName));
        }

        private static ResponseHandler create(final String responseHandlerClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            final Class<?> clazz = Class.forName(responseHandlerClassName);
            final Class<ResponseHandler> responseHandlerClass = (Class<ResponseHandler>) clazz;
            return responseHandlerClass.newInstance();
        }

        public static Route clone(Route route) {
            try {
                //original succeeded - the clone will be the same - these exceptions won't be raised
                return new Route(
                        route.filename,
                        Pattern.compile(route.path.pattern()),
                        (Properties) route.getProperties().clone(),
                        route.responseHandler.getClass().newInstance()
                );

            } catch (Exception e) {
                e.printStackTrace(System.err);
                return null;
            }
        }
    }
}

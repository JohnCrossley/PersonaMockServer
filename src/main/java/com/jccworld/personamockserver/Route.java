package com.jccworld.personamockserver;

import com.jccworld.personamockserver.responsehandler.ResponseHandler;

import java.util.Properties;
import java.util.regex.Pattern;

public class Route {
    private final Pattern path;
    private final Properties properties;
    private final ResponseHandler responseHandler;

    private Route(final Pattern path, final Properties properties, final ResponseHandler responseHandler) {
        this.path = path;
        this.properties = properties;
        this.responseHandler = responseHandler;
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
        static Route create(final String pathPattern, final Properties properties, final String responseHandlerClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
            return new Route(Pattern.compile(pathPattern), properties, create(responseHandlerClassName));
        }

        private static ResponseHandler create(String responseHandlerClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            final Class<?> clazz = Class.forName(responseHandlerClassName);
            final Class<ResponseHandler> responseHandlerClass = (Class<ResponseHandler>) clazz;
            return responseHandlerClass.newInstance();
        }

        public static Route clone(Route route) {
            try {
                //original succeeded - the clone will be the same - these exceptions won't be raised
                return new Route(
                        Pattern.compile(route.getPath().pattern()),
                        (Properties) route.getProperties().clone(),
                        route.responseHandler.getClass().newInstance()
                );

            } catch (Exception e) {
                System.out.println("[JCC] FATAL: clone failed: " + e.getMessage());
                e.printStackTrace();

                return null;
            }
        }
    }
}

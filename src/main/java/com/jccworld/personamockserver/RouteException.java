package com.jccworld.personamockserver;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class RouteException extends Exception {

    public RouteException(final String message, final Throwable e) {
        super(message, e);
    }

    public RouteException(final String message) {
        super(message);
    }

    public RouteException(final Throwable e) {
        super(e);
    }

}

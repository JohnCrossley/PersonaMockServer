package com.jccworld.personamockserver.responder;

import com.jccworld.personamockserver.RouteException;
import spark.Request;
import spark.Response;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public interface RouteResponder {

    String getHttpPath();
    void run(final Request request, final Response response) throws RouteException;

}

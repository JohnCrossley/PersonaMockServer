package com.jccworld.personamockserver.challenge;

import spark.Request;
import spark.Response;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public interface Challenge {

    CustomResponse perform(final Request request, final Response response) throws Exception;

}

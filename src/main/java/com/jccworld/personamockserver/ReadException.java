package com.jccworld.personamockserver;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class ReadException extends Exception {

    public ReadException(final Exception e) {
        super(e);
    }

}

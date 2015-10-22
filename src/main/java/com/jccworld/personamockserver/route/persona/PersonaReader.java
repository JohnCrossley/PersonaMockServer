package com.jccworld.personamockserver.route.persona;

import com.jccworld.personamockserver.ReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static java.io.File.separator;

/**
 * @author johncrossley
 * @see <a href="https://github.com/JohnCrossley/PersonaMockServer">https://github.com/JohnCrossley/PersonaMockServer</a>
 */
public class PersonaReader {
    private static final String DEBUG_MESSAGE_TEMPLATE = "Attempted to resolve persona ``%s'' on path ``%s''...%s";

    private final Logger logger = LoggerFactory.getLogger(PersonaReader.class);
    private final String personaPath;

    public PersonaReader(final String personaPath) {
        if (personaPath == null) {
            throw new IllegalArgumentException("personaPath cannot be null");
        }

        this.personaPath = personaPath;
    }

    public String resolveFilePath(final Persona persona, final String file) throws ReadException {
        for(String personaName : persona.list()) {
           File personaFile = new File(personaPath + separator + personaName + separator + file);
            if (personaFile.exists()) {
                logger.debug(String.format(DEBUG_MESSAGE_TEMPLATE, personaName, personaFile.getAbsolutePath(), "Found"));
                return personaFile.getPath();
            } else {
                logger.debug(String.format(DEBUG_MESSAGE_TEMPLATE, personaName, personaFile.getAbsolutePath(), "Not found!"));
            }
        }

        //none of the personas match, not even the ``default''.

        if (persona.list().length == 1) {
            throw new ReadException(new FileNotFoundException("File not found under default persona: ``" + Persona.DEFAULT_PERSONA + "''." +
                    "  No overriding persona in use."));
        } else {
            throw new ReadException(new FileNotFoundException("File not found under persona: " + persona + ", " +
                    "nor in fallback persona: ``" + Persona.DEFAULT_PERSONA + "''."));
        }
    }
}

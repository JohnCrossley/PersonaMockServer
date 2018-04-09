package com.jccworld.personmockserver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class PersonaDataFilePicker {
    public String createLocalFilePath(final HttpServletRequest request, final String persona, final String filename) {
        final String personaDirectory = request.getServletContext().getRealPath("personas/" + persona) + File.separator + filename;
        final String defaultDirectory = request.getServletContext().getRealPath("personas/default") + File.separator + filename;

        if (pathExists(personaDirectory)) {
            System.out.println("[JCC]   [" + request.getRequestURI() + "] persona `" + persona + "' found");
            return personaDirectory;
        } else if (pathExists(defaultDirectory)) {
            System.out.println("[JCC]   [" + request.getRequestURI() + "] persona `" + persona + "' missing but default found");
            return defaultDirectory;
        } else {
            System.out.println("[JCC]   [" + request.getRequestURI() + "] persona `" + persona + "' and default missing !!");
            return null;
        }
    }

    private boolean pathExists(final String path) {
        return new File(path).exists();
    }
}

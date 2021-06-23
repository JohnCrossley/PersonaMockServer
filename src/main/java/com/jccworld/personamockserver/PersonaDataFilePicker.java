package com.jccworld.personamockserver;

import java.io.File;

public class PersonaDataFilePicker {
    private final Settings settings;

    public PersonaDataFilePicker(Settings settings) {
        this.settings = settings;
    }

    public String createLocalFilePath(final String persona, final String filename) {
        final String personaFilePath = settings.getPersona(persona, filename);
        final String defaultFilePath = settings.getDefault(filename);

        if (pathExists(personaFilePath)) {
            System.out.println("[PMS]   persona `" + persona + "' found [" + personaFilePath + "]");
            return personaFilePath;
        } else if (pathExists(defaultFilePath)) {
            System.out.println("[PMS]   persona `" + persona + "' missing, but default found [" + defaultFilePath + "]");
            return defaultFilePath;
        } else {
            System.out.println("[PMS]   persona `" + persona + "' and default missing !!  No file to serve! :(");
            return null;
        }
    }

    private boolean pathExists(final String path) {
        return new File(path).exists();
    }
}

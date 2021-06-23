package com.jccworld.personamockserver;

import java.io.File;

public class Settings {
    private static final String DEFINITIONS_FOLDER = "definitions";
    private static final String PERSONAS_FOLDER = "personas";
    private static final String DEFAULT_FOLDER = "default";

    public final String definitionsRoot;
    public final String personasRoot;

    public Settings(String root) {
        if (root == null) {
            System.out.println("[PMS]  Settings.init() " + RouteFilter.INIT_PARAM_DATA_ROOT + " not set, using `WEB-INF` as root.   Classes located in: " + getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            root = getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("/classes/", "");
        }

        definitionsRoot = root + File.separator + DEFINITIONS_FOLDER;
        personasRoot = root + File.separator + PERSONAS_FOLDER;

        System.out.println("[PMS]    Settings.init() definitionsRoot: " + definitionsRoot);
        System.out.println("[PMS]    Settings.init() personasRoot:    " + personasRoot);
    }

    String getPersona(String persona, String filename) {
        return personasRoot + File.separator + persona + File.separator + filename;
    }

    String getDefault(String filename) {
        return getPersona(DEFAULT_FOLDER, filename);
    }
}

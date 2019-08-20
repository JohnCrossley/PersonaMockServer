package com.jccworld.personamockserver.util;

import com.jccworld.personamockserver.persona.Persona;
import com.jccworld.personamockserver.PersonaDataFilePicker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class HeaderWriter {
    public static void setHeaders(final String file, final Persona persona, final PersonaDataFilePicker personaDataFilePicker,
                                  final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (file != null) {
            final Properties properties = new Properties();
            final InputStream inputStream = new FileInputStream(personaDataFilePicker.createLocalFilePath(request, persona.extract(request), file));
            properties.load(inputStream);

            final Enumeration e = properties.propertyNames();
            while (e.hasMoreElements()) {
                final String name = (String) e.nextElement();
                response.addHeader(name, properties.getProperty(name));
            }
        }
    }
}

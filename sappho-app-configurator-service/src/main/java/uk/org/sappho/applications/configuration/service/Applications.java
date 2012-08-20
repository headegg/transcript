package uk.org.sappho.applications.configuration.service;

import com.google.gson.Gson;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

public class Applications extends WorkingCopy {

    private String environment;

    public Applications(String environment) throws ConfigurationException {

        super();
        versionedWorkingCopy.update(environment);
        this.environment = environment;
    }

    public String[] get() {

        String[] applications = new File(directory, environment).list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile() && name.endsWith(".json");
            }
        });
        for (int i = 0; i < applications.length; i++) {
            String name = applications[i];
            applications[i] = name.substring(0, name.length() - 5);
        }
        return applications;
    }
}

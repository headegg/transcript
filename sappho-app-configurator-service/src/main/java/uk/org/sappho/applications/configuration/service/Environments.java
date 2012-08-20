package uk.org.sappho.applications.configuration.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Vector;

public class Environments extends WorkingCopy {

    public Environments() throws ConfigurationException {

        super();
        versionedWorkingCopy.update(".");
    }

    public String[] get() {

        return new File(directory).list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory() && !name.startsWith(".");
            }
        });
    }
}

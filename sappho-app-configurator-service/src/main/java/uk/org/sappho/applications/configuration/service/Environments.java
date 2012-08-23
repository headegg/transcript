package uk.org.sappho.applications.configuration.service;

import java.io.File;
import java.io.FilenameFilter;

public class Environments {

    public String[] getAll() throws ConfigurationException {

        return WorkingCopy.getInstance().getFile(".", new WorkingCopyContext()).list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory() && !name.startsWith(".");
            }
        });
    }
}

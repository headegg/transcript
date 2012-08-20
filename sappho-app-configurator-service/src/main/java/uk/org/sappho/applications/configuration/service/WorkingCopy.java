package uk.org.sappho.applications.configuration.service;

import uk.org.sappho.applications.configuration.service.vcs.SubversionWorkingCopy;

import java.io.File;

public class WorkingCopy {

    protected VersionedWorkingCopy versionedWorkingCopy = null;
    protected String directory;

    public WorkingCopy() throws ConfigurationException {

        directory = System.getProperty("working.copy.path");
        if (directory == null) {
            throw new ConfigurationException("System property working.copy.path not specified");
        }
        File file = new File(directory, ".svn");
        if (file.isDirectory()) {
            versionedWorkingCopy = new SubversionWorkingCopy(directory);
        }
        if (versionedWorkingCopy == null) {
            throw new ConfigurationException("Directory " + directory + " is not under version control");
        }
    }
}

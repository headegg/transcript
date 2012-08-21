package uk.org.sappho.applications.configuration.service;

import uk.org.sappho.applications.configuration.service.vcs.SubversionWorkingCopy;

import java.io.File;

public class WorkingCopy {

    private static final WorkingCopy instance = new WorkingCopy();
    private final SubversionWorkingCopy subversionWorkingCopy = new SubversionWorkingCopy();
    private final String workingCopyPath = System.getProperty("working.copy.path");

    public File getFile(String name) throws ConfigurationException {

        if (workingCopyPath == null) {
            throw new ConfigurationException("System property working.copy.path not specified");
        }
        File file = new File(workingCopyPath, ".svn");
        if (file.exists() && file.isDirectory()) {
            subversionWorkingCopy.update(workingCopyPath, name);
        } else {
            throw new ConfigurationException("Directory " + workingCopyPath + " is not under version control");
        }
        file = new File(workingCopyPath, name);
        if (!file.exists()) {
            throw new ConfigurationException("Requested object " + name + " does not exist");
        }
        return file;
    }

    public static WorkingCopy getInstance() {

        return instance;
    }

    private WorkingCopy() {
    }
}

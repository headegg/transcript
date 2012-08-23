package uk.org.sappho.applications.configuration.service;

import uk.org.sappho.applications.configuration.service.vcs.SubversionWorkingCopy;

import java.io.File;

public class WorkingCopy {

    private static final WorkingCopy instance = new WorkingCopy();
    private final SubversionWorkingCopy subversionWorkingCopy = new SubversionWorkingCopy();
    private final String workingCopyPath = System.getProperty("working.copy.path");

    synchronized public File getFile(String name, WorkingCopyContext workingCopyContext) throws ConfigurationException {

        if (workingCopyPath == null) {
            throw new ConfigurationException("System property working.copy.path not specified");
        }
        File file = new File(workingCopyPath, name);
        if (file.exists()) {
            File svnDirectory = new File(workingCopyPath, ".svn");
            if (svnDirectory.exists() && svnDirectory.isDirectory()) {
                subversionWorkingCopy.update(workingCopyPath, workingCopyContext);
            } else {
                throw new ConfigurationException("Directory " + workingCopyPath + " is not under version control");
            }
        } else {
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

/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.inject.Inject;

import java.io.File;
import java.io.FilenameFilter;

public class Environments {

    private WorkingCopy workingCopy;

    @Inject
    public Environments(WorkingCopy workingCopy) {

        this.workingCopy = workingCopy;
    }

    public String[] getAll() throws ConfigurationException {

        return workingCopy.getFile(".", null).list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory() && !name.startsWith(".");
            }
        });
    }
}

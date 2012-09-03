/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import com.google.inject.Inject;

import java.io.File;
import java.io.FilenameFilter;

public class Applications {

    private WorkingCopy workingCopy;

    @Inject
    public Applications(WorkingCopy workingCopy) {

        this.workingCopy = workingCopy;
    }

    public String[] getAll(String environment) throws ConfigurationException {

        String[] applications = workingCopy.getFile(environment, null).list(new FilenameFilter() {
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

/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.services.transcript.registry;

import com.google.inject.Inject;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class Applications {

    private WorkingCopy workingCopy;

    @Inject
    public Applications(WorkingCopy workingCopy) {

        this.workingCopy = workingCopy;
    }

    public String[] getApplicationNames(String environment) throws ConfigurationException {

        String[] applications = workingCopy.getUpToDatePath(environment).list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile() && name.endsWith(".json");
            }
        });
        if (applications != null) {
            for (int i = 0; i < applications.length; i++) {
                String name = applications[i];
                applications[i] = name.substring(0, name.length() - 5);
            }
            Arrays.sort(applications);
        } else {
            applications = new String[0];
        }
        return applications;
    }
}

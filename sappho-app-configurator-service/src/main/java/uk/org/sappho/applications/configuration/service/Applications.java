/**
 *** This software is licensed under the GNU Affero General Public License, version 3.
 *** See http://www.gnu.org/licenses/agpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.configuration.service;

import java.io.File;
import java.io.FilenameFilter;

public class Applications {

    private String environment;

    public Applications(String environment) {

        this.environment = environment;
    }

    public String[] getAll() throws ConfigurationException {

        String[] applications = WorkingCopy.getInstance().getFile(environment, new WorkingCopyContext()).list(new FilenameFilter() {
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

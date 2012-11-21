/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.service.registry;

import com.google.inject.Inject;
import uk.org.sappho.applications.transcript.service.TranscriptException;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Environments {

    private WorkingCopy workingCopy;
    private Properties properties;

    private static final Pattern ALL = Pattern.compile("^[^\\.].*$");
    private static final Pattern IGNORE = Pattern.compile("^(\\.svn|\\.|\\.\\.)$");

    @Inject
    public Environments(WorkingCopy workingCopy, Properties properties) {

        this.workingCopy = workingCopy;
        this.properties = properties;
    }

    public String[] getEnvironmentNames() throws TranscriptException {

        return getEnvironmentNames(ALL);
    }

    public String[] getEnvironmentNames(final Pattern pattern) throws TranscriptException {

        String[] environments = workingCopy.getUpToDatePath("").list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory() && pattern.matcher(name).matches() &&
                        !IGNORE.matcher(name).matches();
            }
        });
        if (environments != null) {
            Arrays.sort(environments);
        } else {
            environments = new String[0];
        }
        return environments;
    }
}

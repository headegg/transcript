/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.applications.transcript.restful.form;

import uk.org.sappho.applications.transcript.service.registry.Properties;
import uk.org.sappho.applications.transcript.service.vcs.VersionControlSystemParameters;

public class PropertiesFromForm {

    private final VersionControlSystemParameters versionControlSystemParameters;
    private final Properties properties;

    public PropertiesFromForm(VersionControlSystemParameters versionControlSystemParameters, Properties properties) {

        this.versionControlSystemParameters = versionControlSystemParameters;
        this.properties = properties;
    }

    public String put(String environment, String application, String key, String value, String commitMessage,
                      String acknowledgeUrl, String errorUrl) {

        String redirectUrl;
        versionControlSystemParameters.setCommitMessage(commitMessage);
        try {
            properties.put(environment, application, key, value);
            redirectUrl = acknowledgeUrl;
        } catch (Throwable throwable) {
            redirectUrl = errorUrl;
        }
        return redirectUrl;
    }
}
